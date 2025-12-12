#!/usr/bin/env python3
import socket
import sys

HOST = '139.162.5.230'
PORT = 10168
K = sum(b'MUSTCTF')  # 550

def fetch_leaks(host, port, max_leaks=64, timeout=60):
    leaks = []
    s = socket.create_connection((host, port), timeout=timeout)
    s.settimeout(timeout)
    buf = b''
    try:
        # read until prompt and interact
        while len(leaks) < max_leaks:
            # receive until we see 'i ='
            while b'i =' not in buf:
                data = s.recv(4096)
                if not data:
                    return leaks
                buf += data
            # send next index
            i = len(leaks)
            tosend = f"{i}\n".encode()
            s.sendall(tosend)
            # read response line by line
            # assume server sends newline-terminated lines
            # drain until we find 'Leak:' or prompt again
            respbuf = b''
            while True:
                data = s.recv(4096)
                if not data:
                    break
                respbuf += data
                if b'Leak:' in respbuf or b'Unexpected' in respbuf or b'i =' in respbuf:
                    break
            # parse leak
            if b'Leak:' in respbuf:
                # find the number after Leak:
                try:
                    txt = respbuf.decode(errors='ignore')
                    after = txt.split('Leak:')[1].splitlines()[0].strip()
                    val = int(after)
                    print(f"[+] i={i} -> leak={val}")
                    leaks.append(val)
                    buf = b''
                    continue
                except Exception as e:
                    print('parse error', e)
                    break
            else:
                print('no Leak found, server response:')
                try:
                    print(respbuf.decode(errors='ignore'))
                except:
                    print(repr(respbuf))
                break
    finally:
        s.close()
    return leaks

if __name__ == '__main__':
    leaks = fetch_leaks(HOST, PORT, max_leaks=32, timeout=60)
    if not leaks:
        print('No leaks retrieved')
        sys.exit(1)
    data = bytes([v ^ K for v in leaks])
    print('\nDecoded bytes:', data)
    try:
        print('Decoded text:', data.decode())
    except:
        pass
    print('Total leaked bytes:', len(data))
    # print as printable escape
    print('Printable:', ''.join((chr(b) if 32 <= b < 127 else '?') for b in data))
