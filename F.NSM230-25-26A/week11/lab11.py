# Source - https://stackoverflow.com/q
# Posted by Siddharth Dhingra
# Retrieved 2025-11-10, License - CC BY-SA 3.0

def seedLCG(initVal):
    global rand
    rand = initVal

def lcg():
    a = 7 ** 5
    c = 0
    m = 2 ** 31 - 1
    global rand
    rand = (a*rand + c) % m
    return rand
"""
seedLCG(1)

for i in range(10000):
    print(lcg())
"""
a = 7**5
n = 383 * 503
s = 101355
k = 0
X = s**2 % n
for i in range(10000):
    X = X**2%n
    Bi = X%2
    k=k+Bi
    if k%1000==0:
        print(X,Bi,k)
print("fin",X,Bi,k)