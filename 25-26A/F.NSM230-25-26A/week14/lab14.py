"""
P=(x1,y1)
Q=(x2,y2)
X=P+Q,P!=Q
"""
# ---------------------------------------------------------------------------
# Quick reference (variables, keys, and formulas)
#
# Context: this file implements elliptic-curve point addition and scalar
# multiplication over a prime field F_p for a short Weierstrass curve
#
# Curve equation (mod q):
#   y^2 = x^3 + a*x + b   (mod q)
#
# Variables (how they're used in this script):
# - q : prime modulus for the finite field F_p
# - a, b : curve coefficients (integers mod q)
# - P = (x1, y1) and Q = (x2, y2) : points on the curve (tuples)
# - G : generator/base point (a point on the curve) used as starting
#       point for scalar multiplication (the code uses parameter name G
#       in `mult` function; currently P/ Q examples are provided below)
# - n : integer scalar (private key in ECC schemes)
# - nG : scalar multiplication of G by n — the public key (point)
#
# Public / Private key relationship (ECC):
# - Private key: n (an integer in [1, order-1])
# - Public key:  Pub = n * G  (point on the curve computed by scalar mult)
#
# Formulas used for point addition / doubling (all operations mod q):
# 1) Point addition P + Q (P != Q)
#    λ = (y2 - y1) * inverse_mod(x2 - x1, q)   (mod q)
#    x3 = λ^2 - x1 - x2                          (mod q)
#    y3 = λ*(x1 - x3) - y1                      (mod q)
#
# 2) Point doubling P + P (P == Q)
#    λ = (3*x1^2 + a) * inverse_mod(2*y1, q)    (mod q)
#    x3 = λ^2 - 2*x1                             (mod q)
#    y3 = λ*(x1 - x3) - y1                       (mod q)
#
# Notes on implementation details in this file:
# - `GCD` implements a simple recursive gcd used to simplify numerator/denominator
#   before computing modular inverses. That helps when numerator/denominator
#   share a common factor.
# - `pow(d, -1, q)` is used to compute modular inverse of d mod q (Python 3.8+).
# - `abn` style fast exponentiation is not used here; point operations rely on
#   Python's built-in `pow` for inverses.
# - `mult(na, G, a, b, q)` performs naive repeated addition. For efficiency
#   in real use, replace with double-and-add (binary method) or other windowed
#   scalar multiplication algorithms.
# - This code assumes the points provided lie on the curve; no validation is
#   performed before operations.
#
# Example ECC key generation (conceptual):
#  - Choose curve (q, a, b) and base point G with known order r
#  - Private key: n = random integer in [1, r-1]
#  - Public key:  Pub = n * G
#
# ECC ElGamal encryption (conceptual outline):
#  - Sender picks random k and computes C1 = k * G
#  - Sender computes shared = k * Pub (point); message M is encoded as a
#    point or numeric and combined with shared to produce C2
#  - Ciphertext = (C1, C2); receiver uses private key n to compute n*C1 = shared
#    and recover M
#
# ---------------------------------------------------------------------------
"""
EC DH key exch
EC elgamal en/de
3.user interface:
    na * G
    for EC(P,Q)
    EC(P+Q)
    EC(P+P)
"""
def gcd(x, y):
    """Greatest common divisor (recursive)."""
    if y != 0:
        x = gcd(y, x % y)
    return x

def point_add(P, Q, q):
    """Compute slope for P + Q where P != Q (returns lambda).

    Uses numerator/denominator reduction before modular inverse.
    """
    x1, y1 = P
    x2, y2 = Q

    num = (y2 - y1) % q
    den = (x2 - x1) % q

    g = gcd(num, den)
    if g == den:
        lam = num // den
    else:
        den_r = den // g
        num_r = num // g
        lam = (pow(den_r, -1, q) * num_r) % q

    return lam

def point_double(P, a, q):
    """Compute slope for 2P (point doubling)."""
    x1, y1 = P
    num = (3 * (x1 ** 2) + a) % q
    den = (2 * y1) % q
    g = gcd(num, den)
    if g == den:
        lam = num // den
    else:
        den_r = den // g
        num_r = num // g
        lam = (pow(den_r, -1, q) * num_r) % q

    return lam

def add_points(P, Q, a, b, q):
    """Add two points P and Q on the curve (handles P==Q doubling).

    Returns the resulting point R = P + Q.
    """
    if P[0] == Q[0] and P[1] == Q[1]:
        lam = point_double(P, a, q)
    else:
        lam = point_add(P, Q, q)

    x1, y1 = P
    x2, y2 = Q

    x3 = (lam ** 2 - x1 - x2) % q
    y3 = (lam * (x1 - x3) - y1) % q
    return (x3, y3)

def scalar_mult(n, G, a, b, q):
    """Naive scalar multiplication: compute n * G by repeated addition.

    Note: this is O(n) and inefficient for large n; use double-and-add
    for performance in real applications.
    """
    P = G
    for _ in range(1, n):
        P = add_points(P, G, a, b, q)
    return P


P = (56, 248)
Q = (30, 36)
q = 257
a = 0
b = -4
print(add_points(P, Q, a, b, q))
print(add_points(P, P, a, b, q))
