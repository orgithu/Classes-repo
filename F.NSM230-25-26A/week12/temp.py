"""
P(n) = (p-1)(q-1)
p,q prime numbers
n = p*q
e, gcd(P(n),e) = 1, 1<e<P(n)
e*d mod P(n) = 1
egcd table is used.
e*e^-1
write egcd and import to use 
d = pow(e,-1,P(n)) --> e^-1 mod P(n)
p and q must be big as possible. in practice, 7-11 digit prime numbers.
298x for example
"""
import string
st=list(string.ascii_lowercase+string.ascii_uppercase+"0123456789 ,;@?")

def egcd(a, b):
    if a == 0:
        return b, 0, 1
    g, x1, y1 = egcd(b % a, a)
    x = y1 - (b // a) * x1
    y = x1
    return g, x, y

def inv(a, m):
    g, x, _ = egcd(a, m)
    if g != 1:
        raise ValueError("no inverse")
    return x % m

def charToDecimal(char):
    return st.index(char)

def convertToChar(P): #len=4
    if len(str(P)) == 4:
        l = int(str(P)[2:])
        r = int(str(P)[:2])
        result = st[r]+st[l]
        list_result = list(result)
        return str(result)
    else:
        a = '0' + str(P)
        return convertToChar(a)

def mypow(a, b, n):
    f = 1
    a %= n
    b_bin = bin(b)[2:]
    for i in b_bin:
        f = (f * f) % n
        if i == '1':
            f = (f * a) % n
    return f

def rsa(x, k):
    return mypow(x, k[0], k[1])

def keys(p, q):
    e = 7
    n = p * q
    pn = (p - 1) * (q - 1)
    d = inv(e, pn)
    return [e, n], [d, n]

pub, priv = keys(7, 11) #key
m = 88
c = rsa(m, pub)
d = rsa(c, priv)
print(c, d)