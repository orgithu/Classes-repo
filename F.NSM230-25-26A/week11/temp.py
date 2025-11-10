import datetime as dt
import sdes
def EDE(K1,K2,P):
    return sdes.encrypt(K1,sdes.decrypt(K2,))
#m1=E(K1,P)
#m2=D(K2,m1)
#m3=E(K1,m2)
def fZfil(t,n):
    s=bin(t)[2:]
    s=s[::-1]
    s=s+"0"*(n-len(s))
    s=s[::-1]
    return s

def XOR(a,b):
    aa=int(a,10)
    bb=int(b,10)
    cc=aa^bb
    T=fZfil(cc,8)
    return T

def DT():
    dt1=dt.datetime.now()
    t=str(dt1)
    t=t[-6:]
    print(t)
    t=int(t,10)%256
    T=fZfil(t,8)
    print(dt1,T)
    return T

IV=fZfil(3,8)
P='01110010'
K1='1010000010'
K2='1010000011'
#print(XOR(K1,K2))
#for i in range(10):
dti=DT()
print(dt.datetime.now(),dti)
for i in range(10):
    DT()






    
