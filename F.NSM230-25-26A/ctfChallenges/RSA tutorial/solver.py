# Solver for RSA tutorial CTF: e = 2^16, p and q known
# Computes 2^16-th roots by repeated modular square roots and CRT

p = 79565304973649738046890929641550086406229645142982116252431882783628570446741
q = 104895446414749804110599905404014579424417002368568255490767700901764221803853
c = 4540356813631057206329938934275504497042552943607683102015080934372428231345551929844529058302190596843384780497234278626232722159254772622184794355722055

n = p * q
k = 16  # since e = 65536 = 2^16

# Tonelli-Shanks modular square root implementation returning a list of roots (0, or 2 roots)
def mod_sqrt(a, pprime):
    a %= pprime
    if a == 0:
        return [0]
    # Legendre symbol
    ls = pow(a, (pprime - 1) // 2, pprime)
    if ls != 1:
        return []
    # factor p-1 = q * 2^s
    q = pprime - 1
    s = 0
    while q % 2 == 0:
        q //= 2
        s += 1
    if s == 1:
        r = pow(a, (pprime + 1) // 4, pprime)
        return list({r % pprime, (-r) % pprime})
    # find z a quadratic non-residue
    z = 2
    while pow(z, (pprime - 1) // 2, pprime) != pprime - 1:
        z += 1
    m = s
    c = pow(z, q, pprime)
    t = pow(a, q, pprime)
    r = pow(a, (q + 1) // 2, pprime)
    while True:
        if t == 1:
            return list({r % pprime, (-r) % pprime})
        # find least i, 0 < i < m, such that t^(2^i) == 1
        t2 = t
        i = None
        for j in range(1, m):
            t2 = pow(t2, 2, pprime)
            if t2 == 1:
                i = j
                break
        if i is None:
            return []
        b = pow(c, 1 << (m - i - 1), pprime)
        r = (r * b) % pprime
        t = (t * b * b) % pprime
        c = pow(b, 2, pprime)
        m = i

# Repeatedly take modular square roots k times to get all x such that x^(2^k) == value (mod prime)
def all_2k_roots(value, prime, k):
    roots = {value % prime}
    for i in range(k):
        new_roots = set()
        for v in roots:
            rs = mod_sqrt(v, prime)
            for r in rs:
                new_roots.add(r % prime)
        roots = new_roots
        if not roots:
            break
    return sorted(roots)

# Compute v2 (2-adic valuation) of p-1 and q-1 to estimate branching

def v2(x):
    cnt = 0
    while x % 2 == 0:
        x //= 2
        cnt += 1
    return cnt

print("v2(p-1)", v2(p-1))
print("v2(q-1)", v2(q-1))

roots_p = all_2k_roots(c, p, k)
roots_q = all_2k_roots(c, q, k)

print(f"Found {len(roots_p)} roots modulo p and {len(roots_q)} roots modulo q")

# CRT combine each pair and test for plausible flag bytes
inv_p_mod_q = pow(p, -1, q)

candidates = []
limit = 1000000
count = 0
for rp in roots_p:
    for rq in roots_q:
        # x ≡ rp (mod p), x ≡ rq (mod q)
        x = (rp + p * ((rq - rp) * inv_p_mod_q % q)) % n
        # convert to bytes
        blen = (x.bit_length() + 7) // 8
        try:
            b = x.to_bytes(blen, 'big')
        except OverflowError:
            continue
        # heuristics: printable and contains 'flag' or starts with b'FLAG' or b'flag{' etc.
        if b.startswith(b'flag') or b.startswith(b'FLAG') or b.startswith(b'CTF') or b.find(b'flag') != -1 or b.find(b'flag{') != -1:
            candidates.append(b)
        # Also check if bytes are mostly printable
        if sum(1 for ch in b if 32 <= ch < 127) / max(1, len(b)) > 0.9:
            candidates.append(b)
        count += 1
        if count > limit:
            break
    if count > limit:
        break

# deduplicate and print plausible candidates
seen = set()
for cnd in candidates:
    if cnd in seen:
        continue
    seen.add(cnd)
    print("Candidate:", cnd)

if not candidates:
    print("No obvious candidate found; consider printing all CRT combinations or adjusting heuristics.")
else:
    print(f"Total promising candidates: {len(seen)}")
