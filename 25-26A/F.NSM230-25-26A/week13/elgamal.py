import x981
import time

def egcd(a: int, b: int):
    if a == 0:
        return b, 0, 1
    gcd, x1, y1 = egcd(b % a, a)
    x = y1 - (b // a) * x1
    y = x1
    return gcd, x, y

def inv(a: int, m: int) -> int:
    a, xa, _ = egcd(a % m, m)
    if a != 1:
        raise ValueError("no inverse")
    return xa % m

def abn(a: int, b: int, n: int) -> int:
    result = 1
    a = a % n
    if b < 0:
        raise ValueError("no negative")
    while b > 0:
        if b & 1:
            result = (result * a) % n
        a = (a * a) % n
        b >>= 1
    return result

def tupleToStr(cipher) -> str:
    strC = ''
    for c1,c2 in cipher:
        strC+='('+hex(c1).removeprefix('0x')+','+hex(c2).removeprefix('0x')+')'+'/'
    return strC

def strToTuple(strC:str):
    cipher = []
    ct = strC.split('/')
    for i in ct:
        p = i.strip()
        if p.startswith('(') and p.endswith(')'):
            p = p[1:-1]
        try:
            aHex, bHex = p.split(',')
        except:
            continue
        c1 = int(aHex, 16)
        c2 = int(bHex, 16)
        cipher.append((c1, c2))
    return cipher

def keygen(q: int, a: int):
    xa = x981.ctrRand(q - 1)
    ya = abn(a, xa, q)
    return q, a, ya, xa

def encrypt(plaintext, q: int, a: int, ya: int):
    k = x981.ctrRand(q - 1)
    c1 = abn(a, k, q)
    K = abn(ya, k, q)
    cipher = []
    for i in plaintext:
        c2 = (ord(i) * K) % q
        cipher.append((c1, c2))
    return tupleToStr(cipher)

def decrypt(cipher, q: int, xa: int) -> str:
    chars = []
    cipher = strToTuple(cipher)
    for c1, c2 in cipher:
        K = abn(c1, xa, q)
        invK = inv(K, q)
        m = (c2 * invK) % q
        chars.append(chr(m))
    return ''.join(chars)

if __name__ == '__main__':
    start = time.time_ns()
    q = 1000000007
    a = 5
    q, a, ya, xa = keygen(q, a)
    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week13/plain.txt","r") as f:
        plaintext = f.read()
    ciphertext = encrypt(plaintext,q,a,ya)

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week13/cipher.txt","w") as f:
        f.write(ciphertext)

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week13/cipher.txt","r") as f:
        ciphertext1 = f.read()
    decrypted = decrypt(ciphertext1,q,xa)

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week13/decrypted.txt","w") as f:
        f.write(decrypted)
    end = time.time_ns()
    duration = end - start
    print("same?", plaintext == decrypted)
    print("duration", duration / 10**9,"second")