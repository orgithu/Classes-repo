def myXor(a, b):
    n = ord(a)
    na = bin(n)[2:]
    n1 = ord(b)
    nb = bin(n1)[2:]
    xorab = ''
    # Ensure binary strings are of the same length for comparison if needed,
    # though in the original image the loop depends on len(na).
    # If the intent was to compare equal-length binary strings,
    # padding might be needed here too.
    # For now, following the original image's logic based on len(na)
    # as the XOR operation is for two characters 'a' and 'o' that result in different length bin values.
    # We will assume na and nb are derived from single characters and their lengths are sufficient for the loop.

    # Pad the shorter binary string if lengths differ, to ensure proper XOR logic across positions
    max_len = max(len(na), len(nb))
    na = na.zfill(max_len)
    nb = nb.zfill(max_len)

    for i in range(max_len): # Use max_len to iterate over the padded strings
        if na[i] == nb[i]:
            xorab += '0'
        else:
            xorab += '1'
    return xorab

input4 = [14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7]
st = 'hello world'

for eachletter in st:
    n = ord(eachletter)
    nb = bin(n)[2:]
    nb = '0' * (8 - len(nb)) + nb  # Pad to 8 bits
    lb = nb[0:4]
    rb = nb[4:]
    lbn = int(lb, 2)
    rbn = int(rb, 2)
    lc = input4[lbn]
    rc = input4[rbn]
    print(eachletter, n, nb, len(nb), lb, rb, lbn, rbn, ' --- ', lc, rc)

a, b = 'a', 'o'
newxor = myXor(a, b)
print(a, b, newxor, ord(a) ^ ord(b))

# P = 5
# c = input4[P] # This part is commented out in your input
