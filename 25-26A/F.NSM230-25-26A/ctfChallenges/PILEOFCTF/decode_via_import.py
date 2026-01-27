#!/usr/bin/env python3
# decode_via_import.py
import importlib.util
import sys
import os

# load firstHalf.py as a module
module_path = os.path.join(os.path.dirname(__file__), 'firstHalf.py')
spec = importlib.util.spec_from_file_location('firstHalf', module_path)
fh = importlib.util.module_from_spec(spec)
spec.loader.exec_module(fh)

K = sum(b'MUSTCTF')
# fh.flag is bytes
n = len(fh.flag) // 2
leaks = []
for i in range(n):
    try:
        val = fh.leak(i)
    except Exception as e:
        print('error at', i, e)
        break
    leaks.append(val)
    print(f'i={i} leak={val}')

decoded = bytes([v ^ K for v in leaks])
print('\nDecoded bytes:', decoded)
try:
    print('Decoded text:', decoded.decode())
except Exception:
    pass
print('Total leaked bytes:', len(decoded))
