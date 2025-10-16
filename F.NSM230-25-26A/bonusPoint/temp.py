#!/usr/bin/env python3
"""
blind_goat_tool.py

Usage:
    python3 blind_goat_tool.py [--mode listen|connect] [--timeout N]

Behavior:
 - Runs: ssh -p 2222 blind-goat.ctf.empasoft.tech
 - Parses the line "I will come back to you on port N in 5 seconds."
 - If --mode listen  (default): bind 0.0.0.0:N and wait for incoming connection (accept timeout optional).
 - If --mode connect: immediately run `ssh -p N blind-goat.ctf.empasoft.tech` (connect to the challenge host on port N).

Notes:
 - If your machine is behind NAT/firewall, `--mode listen` usually fails unless you have a public IP or forwarded port.
 - `--mode connect` may or may not do anything useful depending on what service (if any) is listening on the challenge host at that port.
"""

import argparse
import re
import shlex
import socket
import subprocess
import sys
import threading
import time

SSH_CMD_BASE = ["ssh", "-p", "2222", "blind-goat.ctf.empasoft.tech"]
PORT_REGEX = re.compile(r"port\s+([0-9]{1,5})", re.IGNORECASE)

def run_ssh_and_get_port(timeout=15):
    """Run initial ssh command and parse callback port. Returns (port, proc)."""
    try:
        proc = subprocess.Popen(SSH_CMD_BASE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True, bufsize=1)
    except FileNotFoundError:
        print("ssh binary not found. Install an SSH client or ensure it's on PATH.", file=sys.stderr)
        sys.exit(1)

    port = None
    start = time.time()
    while True:
        # timeout overall (prevent hanging forever)
        if timeout and (time.time() - start) > timeout:
            print("[error] Timed out waiting for ssh output.", file=sys.stderr)
            proc.kill()
            return None, None

        line = proc.stdout.readline()
        if line == "" and proc.poll() is not None:
            break
        if line:
            print("[ssh-out] " + line.rstrip())
            m = PORT_REGEX.search(line)
            if m:
                port = int(m.group(1))
                break

    if port is None:
        print("[error] Couldn't find callback port in ssh output.", file=sys.stderr)
        try:
            proc.kill()
        except Exception:
            pass
        return None, None

    return port, proc

def start_listener(port, accept_timeout=10):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    try:
        s.bind(("0.0.0.0", port))
    except Exception as e:
        print(f"[listener] Failed to bind 0.0.0.0:{port}: {e}", file=sys.stderr)
        s.close()
        return None, None
    s.listen(1)
    s.settimeout(accept_timeout)
    print(f"[listener] Listening on 0.0.0.0:{port} (accept timeout {accept_timeout}s)...")
    try:
        conn, addr = s.accept()
    except socket.timeout:
        print("[listener] Accept timed out. No incoming connection.", file=sys.stderr)
        s.close()
        return None, None
    print(f"[listener] Connection from {addr[0]}:{addr[1]}")
    return conn, s

def interactive_relay_conn(conn):
    def reader():
        try:
            while True:
                data = conn.recv(4096)
                if not data:
                    break
                try:
                    sys.stdout.write(data.decode(errors="replace"))
                except Exception:
                    sys.stdout.write(str(data))
                sys.stdout.flush()
        except Exception as e:
            print(f"[reader] {e}", file=sys.stderr)

    def writer():
        try:
            while True:
                l = sys.stdin.readline()
                if not l:
                    break
                conn.sendall(l.encode())
        except Exception as e:
            print(f"[writer] {e}", file=sys.stderr)

    rt = threading.Thread(target=reader, daemon=True)
    wt = threading.Thread(target=writer, daemon=True)
    rt.start(); wt.start()
    try:
        while rt.is_alive() and wt.is_alive():
            time.sleep(0.1)
    except KeyboardInterrupt:
        pass
    try:
        conn.shutdown(socket.SHUT_RDWR)
    except Exception:
        pass
    conn.close()

def run_connect_mode(port):
    """Run `ssh -p <port> blind-goat.ctf.empasoft.tech` and attach to its stdout/stderr."""
    cmd = ["ssh", "-p", str(port), "blind-goat.ctf.empasoft.tech"]
    print("[connect] Running:", " ".join(shlex.quote(x) for x in cmd))
    try:
        proc = subprocess.Popen(cmd)
    except FileNotFoundError:
        print("[error] ssh not found on PATH.", file=sys.stderr)
        return
    proc.wait()
    print("[connect] SSH process exited with code", proc.returncode)

def main():
    ap = argparse.ArgumentParser(description="Blind Goat helper: parse callback port and either listen or connect.")
    ap.add_argument("--mode", choices=("listen", "connect"), default="listen",
                    help="listen -> open local listener on callback port (default). connect -> run ssh to callback port.")
    ap.add_argument("--accept-timeout", type=int, default=12, help="listener accept timeout seconds")
    ap.add_argument("--ssh-output-timeout", type=int, default=15, help="max seconds to wait for ssh to print port")
    args = ap.parse_args()

    print("[main] Running initial SSH (port 2222) to obtain callback port...")
    port, ssh_proc = run_ssh_and_get_port(timeout=args.ssh_output_timeout)
    if not port:
        sys.exit(1)

    print(f"[main] Got port: {port}")

    if args.mode == "connect":
        # Immediately run ssh to that port on the challenge host
        run_connect_mode(port)
        # optionally print remaining output from initial ssh
        try:
            while True:
                line = ssh_proc.stdout.readline()
                if not line:
                    break
                print("[ssh-out] " + line.rstrip())
        except Exception:
            pass
        return

    # Otherwise mode == listen
    conn, server_sock = start_listener(port, accept_timeout=args.accept_timeout)
    if conn is None:
        print("[main] No incoming connection was accepted. If you're behind NAT/firewall, the callback cannot reach you.", file=sys.stderr)
        # drain initial ssh output
        try:
            while True:
                line = ssh_proc.stdout.readline()
                if not line:
                    break
                print("[ssh-out] " + line.rstrip())
        except Exception:
            pass
        if server_sock:
            server_sock.close()
        sys.exit(1)

    print("[main] Accepted connection. Starting interactive relay. Type to send; Ctrl+D to end.")
    interactive_relay_conn(conn)
    if server_sock:
        server_sock.close()

    # drain ssh output
    try:
        while True:
            line = ssh_proc.stdout.readline()
            if not line:
                break
            print("[ssh-out] " + line.rstrip())
    except Exception:
        pass

if __name__ == "__main__":
    main()
