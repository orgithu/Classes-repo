import x981
import time

Point = tuple

def invMod(a: int, p: int) -> int:
    a = a % p
    if a == 0:
        raise ValueError("no inverse")
    lm, hm = 1, 0
    low, high = a, p
    while low > 1:
        r = high // low
        nm = hm - lm * r
        new = high - low * r
        hm, lm = lm, nm
        high, low = low, new
    return lm % p

def isOnCurve(P: Point, A: int, B: int, p: int) -> bool:
    if P is None:
        return True
    x, y = P
    left = (y * y) % p
    right = (x * x * x + A * x + B) % p
    return left == right

def pointNeg(P: Point, p: int) -> Point:
    if P is None:
        return None
    x, y = P
    return (x, (-y) % p)

def pointAdd(P: Point, Q: Point, A: int, p: int) -> Point:
    if P is None:
        return Q
    if Q is None:
        return P
    x1, y1 = P
    x2, y2 = Q
    if x1 == x2 and (y1 + y2) % p == 0:
        return None
    if P != Q:
        num = (y2 - y1) % p
        den = (x2 - x1) % p
        s = (num * invMod(den, p)) % p
    else:
        num = (3 * x1 * x1 + A) % p
        den = (2 * y1) % p
        s = (num * invMod(den, p)) % p
    x3 = (s * s - x1 - x2) % p
    y3 = (s * (x1 - x3) - y1) % p
    return (x3, y3)

def scalarMult(k: int, P: Point, A: int, p: int) -> Point:
    if P is None or k % p == 0:
        return None
    if k < 0:
        return scalarMult(-k, pointNeg(P, p), A, p)
    R = None
    Q = P
    while k:
        if k & 1:
            R = pointAdd(R, Q, A, p)
        Q = pointAdd(Q, Q, A, p)
        k >>= 1
    return R

def findYforX(x: int, A: int, B: int, p: int):
    rhs = (x * x * x + A * x + B) % p
    for y in range(p):
        if (y * y) % p == rhs:
            return y
    return None

def findBasePoint(A: int, B: int, p: int) -> Point:
    for x in range(p):
        y = findYforX(x, A, B, p)
        if y is not None and y != 0:
            return (x, y)
    return None

def keyGen(A: int, B: int, p: int, G: Point):
    d = x981.ctrRand(p - 1) + 1
    Q = scalarMult(d, G, A, p)
    return d, Q

def encodeMessage(m: int, A: int, B: int, p: int) -> Point:
    # map byte m to x such that x % 256 == m
    x = m
    while x < p:
        y = findYforX(x, A, B, p)
        if y is not None:
            return (x, y)
        x = x + 256
    raise ValueError("cannot encode")

def decodeMessage(P: Point) -> int:
    if P is None:
        return 0
    x, _ = P
    return x % 256

def encryptPoint(Q: Point, M: Point, G: Point, A: int, p: int):
    k = x981.ctrRand(p - 1) + 1
    C1 = scalarMult(k, G, A, p)
    S = scalarMult(k, Q, A, p)
    C2 = pointAdd(M, S, A, p)
    return C1, C2

def decryptPoint(d: int, C1: Point, C2: Point, A: int, p: int) -> Point:
    S = scalarMult(d, C1, A, p)
    invS = pointNeg(S, p)
    M = pointAdd(C2, invS, A, p)
    return M

# --- serialization helpers ---
def pointToStr(P: Point) -> str:
    if P is None:
        return 'None'
    x, y = P
    return hex(x).removeprefix('0x') + ':' + hex(y).removeprefix('0x')

def strToPoint(s: str) -> Point:
    if s == 'None':
        return None
    xhex, yhex = s.split(':')
    return (int(xhex, 16), int(yhex, 16))

def tupleToStr(cipher) -> str:
    out = ''
    for c1, c2 in cipher:
        out += '(' + pointToStr(c1) + ',' + pointToStr(c2) + ')/'
    return out

def strToTuple(strC: str):
    cipher = []
    parts = strC.split('/')
    for p in parts:
        p = p.strip()
        if not p:
            continue
        if p.startswith('(') and p.endswith(')'):
            p = p[1:-1]
        try:
            a, b = p.split(',')
        except Exception:
            continue
        c1 = strToPoint(a)
        c2 = strToPoint(b)
        cipher.append((c1, c2))
    return cipher

def encryptText(plaintext: str, Q: Point, G: Point, A: int, B: int, p: int) -> str:
    k = x981.ctrRand(p - 1) + 1
    C1 = scalarMult(k, G, A, p)
    S = scalarMult(k, Q, A, p)
    cipher = []
    for ch in plaintext:
        m = ord(ch)
        M = encodeMessage(m, A, B, p)
        C2 = pointAdd(M, S, A, p)
        cipher.append((C1, C2))
    return tupleToStr(cipher)

def decryptText(cipherStr: str, d: int, A: int, B: int, p: int) -> str:
    cipher = strToTuple(cipherStr)
    chars = []
    for C1, C2 in cipher:
        M = decryptPoint(d, C1, C2, A, p)
        m = decodeMessage(M)
        chars.append(chr(m))
    return ''.join(chars)

if __name__ == "__main__":
    # Demo using the given example
    q = 257
    A = 0
    B = (-4) % q
    G = (2, 2)

    # Bob's keys
    nB = 101
    PB = scalarMult(nB, G, A, q)

    # Alice's message and choice
    Pm = (112, 26)
    k = 41

    # Alice computes
    C1 = scalarMult(k, G, A, q)
    kPB = scalarMult(k, PB, A, q)
    C2 = pointAdd(Pm, kPB, A, q)

    print('Curve: y^2 = x^3 + {}x + {} over F_{}'.format(A, B, q))
    print('G =', G)
    print('Bob priv nB =', nB)
    print('Bob pub PB =', PB)
    print('Alice message Pm =', Pm)
    print('Alice picks k =', k)
    print('kG =', C1)
    print('kPB =', kPB)
    print('C2 = Pm + kPB =', C2)

    # Bob decrypts
    recovered = pointAdd(C2, pointNeg(scalarMult(nB, C1, A, q), q), A, q)
    print('Bob computes C2 - nB*C1 =', recovered)
    print('Recovered equals Pm?', recovered == Pm)