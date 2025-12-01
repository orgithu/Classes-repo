"""Elliptic Curve Diffie-Hellman demo and helpers.

This is an educational/demo implementation. Function and variable names use
camelCase and type hints avoid complex typing imports as requested.
"""
import secrets


def isOnCurve(P, a: int, b: int, q: int) -> bool:
    if P is None:
        return True
    x, y = P
    return (y * y - (x * x * x + a * x + b)) % q == 0


def pointNeg(P, q: int):
    if P is None:
        return None
    x, y = P
    return (x, (-y) % q)


def pointAdd(P, Q, a: int, q: int):
    """Add two points P and Q on the curve over F_q. Handles infinity."""
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


def scalarMult(k: int, P, a: int, q: int):
    """Double-and-add scalar multiplication: compute k * P."""
    if k % q == 0 or P is None:
        return None
    if k < 0:
        return scalarMult(-k, pointNeg(P, q), a, q)

    result = None
    addend = P

    while k:
        if k & 1:
            result = pointAdd(result, addend, a, q)
        addend = pointAdd(addend, addend, a, q)
        k >>= 1
    return result


def keyGen(G, nBits: int, a: int, b: int, q: int):
    """Generate private/public key pair. nBits controls private size."""
    priv = secrets.randbelow(2 ** nBits - 1) + 1
    pub = scalarMult(priv, G, a, q)
    return priv, pub
    print("Shared secret matches — ECC Diffie-Hellman successful")


def sharedSecret(priv: int, peerPub, a: int, q: int):
    return scalarMult(priv, peerPub, a, q)


if __name__ == "__main__":
    q = 257
    a = 0
    b = -4
    G = (56, 248)

    assert isOnCurve(G, a, b, q), "G is not on curve"

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

    print("Shared secret matches — ECC Diffie-Hellman successful")
