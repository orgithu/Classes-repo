#!/usr/bin/env python3
# leak_decode.py
import socket
import sys
import subprocess
import time
import re

K = sum(b'MUSTCTF')  # 550

def decode_leaks(leaks):
    data = bytes([l ^ K for l in leaks])
    try:
        text = data.decode('utf-8')
    except:
        text = data
    return data, text

def run_remote(host, port, timeout=8):
    leaks = []
    with socket.create_connection((host, port), timeout=timeout) as s:
        f = s.makefile('rwb', buffering=0)
        i = 0
        while True:
            # read until prompt or EOF
            line = f.readline()
            if not line:
                break
            line = line.decode(errors='ignore')
            if 'i =' in line:
                tosend = f"{i}\n".encode()
                f.write(tosend)
                # read response
                resp = f.readline()
                if not resp:
                    break
                resp = resp.decode(errors='ignore').strip()
                if resp.startswith('Leak:'):
                    val = int(resp.split('Leak:')[1].strip())
                    leaks.append(val)
                    print(f"[+] i={i} -> leak={val}")
                    i += 1
                    continue
                else:
                    print("[*] Non-leak response:", resp)
                    break
            else:
                # other banner lines; print optionally
                pass
    return leaks

def run_local(script_path):
    leaks = []
    p = subprocess.Popen([sys.executable, script_path],
                         stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                         text=True, bufsize=1)
    i = 0
    while True:
        out = p.stdout.readline()
        if out == '':
            break
        sys.stdout.write(out)
        if 'i =' in out:
            p.stdin.write(f"{i}\n")
            p.stdin.flush()
            resp = p.stdout.readline()
            if not resp:
                break
            print(resp.strip())
            if resp.startswith('Leak:'):
                val = int(resp.split('Leak:')[1].strip())
                leaks.append(val)
                i += 1
                continue
            else:
                break
    p.kill()
    return leaks

if __name__ == '__main__':
    if len(sys.argv) == 3 and ':' in sys.argv[1]:
        host_port = sys.argv[1]
        host,port = host_port.split(':')
        port = int(port)
        leaks = run_remote(host, port)
    elif len(sys.argv) == 2 and sys.argv[1] == 'local':
        leaks = run_local('firstHalf.py')
    elif len(sys.argv) == 3:
        # host port separate
        host = sys.argv[1]; port = int(sys.argv[2])
        leaks = run_remote(host, port)
    else:
        print("Usage: leak_decode.py host:port   OR   leak_decode.py host port   OR   leak_decode.py local")
        sys.exit(1)

    data, text = decode_leaks(leaks)
    print("Leaked raw bytes:", data)
    print("Leaked text (utf-8 attempt):", text)
    print("Total leaked bytes:", len(data))