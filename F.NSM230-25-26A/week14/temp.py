def GCD(x, y): 
    if y!=0: 
        x=GCD(y,x%y) 
    return x

def PQ(P,Q,p):
    xQ=Q[0];yQ=Q[1]
    xP=P[0];yP=P[1]
    dtu=(yQ-yP)%p
    dtd=(xQ-xP)%p

    gcd=GCD(dtu,dtd)
    if dtu%dtd!=0:
        if gcd==dtd:
            dt=dtu//dtd
        else:
            dtd=dtd//gcd
            dtu=dtu//gcd
            dt=(pow(dtd,-1,p)*dtu)%p
    else:
        dt=(dtu//dtd)%p
    return dt

a=1;b=1
def PP(P,a,p):
    xP=P[0];yP=P[1]
    dtu=(3*(xP**2)+a)%p
    dtd=(2*yP)%p
    
    gcd=GCD(dtu,dtd)
    if gcd==dtd:
        dt=dtu//dtd
    else:
        dtd=dtd//gcd
        dtu=dtu//gcd
        dt=(pow(dtd,-1,p)*dtu)%p
    return dt

def ECC_PQ(P,Q,a,b,p):
    if P[0]==Q[0] and P[1]==Q[1]:
        L=PP(P,a,p)
    else:
        L=PQ(P,Q,p)
        
    xQ=Q[0];yQ=Q[1]
    xP=P[0];yP=P[1]
    
    xR = (L**2 - xP - xQ) % p
    yR = (L*(xP - xR) - yP) % p
    return (xR,yR)

p=23;a=0;b=1
P = (3,10); Q = (9,7)
print(ECC_PQ(P,Q,a,b,p))
print(ECC_PQ(P,P,a,b,p))
P2 = (20, 20); P3 = (14, 14); P5 = (13, 10);
a=9;p=23;b=17
print('5p=',ECC_PQ(P2,P3,a,b,p))