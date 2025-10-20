from Crypto.Util.number import getPrime
from Crypto.Util.number import bytes_to_long as b2l

from secret import flag


p = getPrime(256)
q = getPrime(256)
n = p * q
e = 65536

print(p)
print(q)
print(pow(b2l(flag), e, n))