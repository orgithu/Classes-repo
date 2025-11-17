def mypow(a, b, n):
    f = 1
    a %= n
    b_bin = bin(b)[2:]
    for i in b_bin:
        f = (f * f) % n
        if i == '1':
            f = (f * a) % n
    return f
while True:
    a = int(input("enter a: "))
    b = int(input("enter b: "))
    n = int(input("enter n: "))
    #print(a,"^",b,"mod",n,"=",mypow(a,b,n))
    print("mypow: ", mypow(a,b,n))
    print("pow:   ", pow(a,b,n))