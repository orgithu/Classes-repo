import time
from collections import Counter

def lcg(a, c, m, seed, n):
    x = seed
    result = []
    for _ in range(n):
        x = (a * x + c) % m
        result.append(x)
    return result

a = 16807
c = 0
m = 2**31 - 1
seed = int(time.time() * 1000) % m
N = 10000
numbers = lcg(a, c, m, seed, N)
freq = Counter(numbers)

print("Seed:", seed)
print("Unique numbers:", len(freq))
print("Most common:", freq.most_common(10))
