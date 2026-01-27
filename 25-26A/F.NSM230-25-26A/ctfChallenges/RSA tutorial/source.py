try:
	from Crypto.Util.number import getPrime, bytes_to_long as b2l
except Exception:
	from Cryptodome.Util.number import getPrime, bytes_to_long as b2l

# placeholder flag value (replace with the actual bytes when using this script)
flag = b"flag"

p = getPrime(256)
q = getPrime(256)
n = p * q
e = 65536

print(p)
print(q)
print(pow(b2l(flag), e, n))