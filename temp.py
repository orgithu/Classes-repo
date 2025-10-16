#!/usr/bin/env python3
"""
blind_goat_connectback.py

Runs:
    ssh -p 2222 blind-goat.ctf.empasoft.tech
parses the callback port and immediately runs:
    ssh -p <PORT> blind-goat.ctf.empasoft.tech

Usage:
    python3 blind_goat_connectback.py
"""

import re
import shlex
import subprocess
import sys
import time

INITIAL_SSH = ["ssh", "-p", "2222", "blind-goat.ctf.empasoft.tech"]
PORT_RE = re.compile(r"port\s+([0-9]{1,5})", re.IGNORECASE)
READ_TIMEOUT = 12  # seconds to wait for the port line

def get_callback_port():
    try:
        proc = subprocess.Popen(INITIAL_SSH, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True, bufsize=1)
    except FileNotFoundError:
        print("ssh not found. Install an SSH client or ensure it's on PATH.", file=sys.stderr)
        sys.exit(1)

    port = None
    start = time.time()
    # Read lines until we find the port or the process ends / timeout
    while True:
        if (time.time() - start) > READ_TIMEOUT:
            print("[error] timed out waiting for callback port.", file=sys.stderr)
            try: proc.kill()
            except Exception: pass
            return None

        line = proc.stdout.readline()
        if line == "" and proc.poll() is not None:
            break
        if line:
            # echo the initial ssh output for debugging
            print("[initial-ssh] " + line.rstrip())
            m = PORT_RE.search(line)
            if m:
                port = int(m.group(1))
                break

    if port is None:
        print("[error] callback port not found in ssh output.", file=sys.stderr)
        return None

    return port

def run_connect_ssh(port):
    cmd = ["ssh", "-p", str(port), "blind-goat.ctf.empasoft.tech"]
    print(f"[connect] running: {' '.join(shlex.quote(x) for x in cmd)}")
    # Use subprocess.call so the spawned ssh inherits the terminal for interactivity
    try:
        return subprocess.call(cmd)
    except FileNotFoundError:
        print("ssh not found when attempting to connect to callback port.", file=sys.stderr)
        return 2
    except KeyboardInterrupt:
        print("\n[connect] interrupted by user.")
        return 130

def main():
    print("[main] Starting initial SSH to obtain callback port...")
    port = get_callback_port()
    if not port:
        sys.exit(1)

    print(f"[main] Got callback port: {port}. Launching immediate connect-back SSH...")
    rc = run_connect_ssh(port)
    print(f"[main] connect-back ssh exited with code {rc}.")
    sys.exit(rc)

if __name__ == "__main__":
    main()
