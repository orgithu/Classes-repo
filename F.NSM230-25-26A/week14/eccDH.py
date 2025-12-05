import x981
import time

def pointNeg(P, q):
    if P is None:
        return None
    x, y = P
    return (x, (-y) % q)


def pointAdd(P, Q, a, q):
    if P is None:
        return Q
    if Q is None:
        return P
    x1, y1 = P
    x2, y2 = Q
    if x1 == x2 and (y1 + y2) % q == 0:
        return None

    if P != Q:
        num = (y2 - y1) % q
        den = (x2 - x1) % q
        lam = (num * pow(den, -1, q)) % q
    else:
        num = (3 * x1 * x1 + a) % q
        den = (2 * y1) % q
        lam = (num * pow(den, -1, q)) % q

    x3 = (lam * lam - x1 - x2) % q
    y3 = (lam * (x1 - x3) - y1) % q
    return (x3, y3)


def scalarMult(k, P, a, q):

    result = None
    addend = P

    while k:
        if k & 1:
            result = pointAdd(result, addend, a, q)
        addend = pointAdd(addend, addend, a, q)
        k >>= 1
    return result

def keyGen(G, nBits, a, b, q):
    priv = x981.ctrRand(2 ** nBits)+1
    pub = scalarMult(priv, G, a, q)
    return priv, pub
 
def sharedSecret(priv, peerPub, a, q):
    return scalarMult(priv, peerPub, a, q)

if __name__ == "__main__":
    start = time.time_ns()
    q = 257
    a = 0
    b = -4
    G = (2, 2)

    alicePriv, alicePub = keyGen(G, nBits=10, a=a, b=b, q=q)
    bobPriv, bobPub = keyGen(G, nBits=10, a=a, b=b, q=q)

    print("Alice priv:", alicePriv)
    print("Alice pub:", alicePub)
    print("Bob priv:", bobPriv)
    print("Bob pub:", bobPub)

    K1 = sharedSecret(alicePriv, bobPub, a, q)
    K2 = sharedSecret(bobPriv, alicePub, a, q)

    print("Alice computed K:", K1)
    print("Bob computed   K:", K2)
    end = time.time_ns()
    dur = end - start
    print(dur/10**9,'s')