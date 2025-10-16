import os
from time import time
from Crypto.Util.number import getPrime
from Crypto.Util.number import bytes_to_long as b2l, long_to_bytes as l2b

flag = os.environ.get('flag', 'ctfmn{fake_flag_for_testing}')
size, bits = 4, 512

def encrypt(m, n, e):
    result = []

    for i in range(0, len(m), size):
        c = pow(b2l(m[i:i+size]), e, n)
        result.append(l2b(c).hex())

    return ':'.join(result)


p = getPrime(bits)
q = getPrime(bits)
n = p * q
e = 0x10001

start_at = time()
print(f'n = 0x{n:x}')

m = os.urandom(16)
c = encrypt(m, n, e)

print(f'c = {c}')
m_candidate = bytes.fromhex(input('m = '))

if time() - start_at > 1000:
    print('Error: Timeout')
    exit()

if m_candidate != m:
    print('Error: Incorrect')
    exit()

print('Flag:', flag)