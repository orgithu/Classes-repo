"""
Note for lab13:
321x elgamal example
toDo:
1. Diffie hellman key exchange:
    Darth, 3rd guy 319x
2. elgamal en/de-use diy rng (time duration) 
3. x9.81 rng-done
4. abn(a,b,n)-done
"""
def abn(a:int, b:int, n:int) -> int:
    f=1
    a%=n
    b_bin=bin(b)[2:]
    for i in b_bin:
        f=(f*f)%n
        if i=='1':
            f=(f*a)%n
    return f

if __name__ == "__main__":
    a = 3 #primitive root
    q = 353 #prime number

    xa = 97 #priv alice
    xb = 233 #prin bob

    ya = abn(a,xa,q) #pub alice
    yb = abn(a,xb,q) #pub bob

    #ka = abn(yb,xa,q) #secretK
    #kb = abn(ya,xb,q) #secretK

    #darth guy
    xd1 = 101
    xd2 = 103
    dq = 107
    da = 2
    yd1 = abn(da,xd1,dq)
    yd2 = abn(da, xd2, dq)
    dk2 = abn(ya,xd2,dq)
    dk1 = abn(yb,xd1,dq)

    k1 = abn(yd1,xb,q) #bob fake key
    k2 = abn(yd2,xa,q) #alice fake key


    print("alice, bob key match:",k1==k2,"k1:",k1,"k2:",k2)
    print("Darth:",dk1,dk2==dk1)
