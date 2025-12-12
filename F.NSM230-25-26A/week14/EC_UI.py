def GCD(x, y): 
        if y!=0: 
                x=GCD(y,x%y) 
        return x

def PQ(P,Q,p):
    xQ=Q[0];yQ=Q[1]
    xP=P[0];yP=P[1]
    # lambda = (y_Q - y_P) * (x_Q - x_P)^{-1}  (mod p)  -- numerator for slope
    dtu=(yQ-yP)%p
    # denominator for slope lambda: (x_Q - x_P) (mod p)
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
    # lambda = (3*x_P^2 + a) * (2*y_P)^{-1}  (mod p)  -- numerator for doubling slope
    dtu=(3*(xP**2)+a)%p
    # denominator for doubling slope lambda: (2*y_P) (mod p)
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
    if P is None:
        return Q
    if Q is None:
        return P
    if P[0] == Q[0] and (P[1] + Q[1]) % p == 0:
        return None
    if P[0]==Q[0] and P[1]==Q[1]:
        L=PP(P,a,p)
    else:
        L=PQ(P,Q,p)
        
    xQ=Q[0];yQ=Q[1]
    xP=P[0];yP=P[1]
    
    # x_R = lambda^2 - x_P - x_Q  (mod p)
    xR = (L**2 - xP - xQ) % p
    # y_R = lambda*(x_P - x_R) - y_P  (mod p)
    yR = (L*(xP - xR) - yP) % p
    return (xR,yR)

def readPoint(prompt):
    s = input(prompt + " (format: x y): ")
    if not s:
        return None
    try:
        x_str, y_str = s.replace(',', ' ').split()
        return (int(x_str), int(y_str))
    except Exception:
        print('bad point format')
        return None

while True:
    print('\n 1) EC(P+Q)')
    print(' 2) EC(P+P)')
    print(' q) quit\n')
    cmd = input('Select option: ').strip()
    if cmd in ('q', 'quit', 'exit'):
        break
    if cmd not in ('1', '2'):
        print('unknown option')
        continue
    p, a, b = 23,9,17
    if cmd == '1':
        P = readPoint('Enter point P')
        Q = readPoint('Enter point Q')
        R = ECC_PQ(P, Q, a, b, p)
        print('P+Q =',R)
    else:
        P = readPoint('Enter point P')
        R = ECC_PQ(P, P, a, b, p)
        print("\nResult: 2P =",R)