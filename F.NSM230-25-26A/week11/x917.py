import datetime as dt
import sdes

def E_block(P8, key10):
    return sdes.SDES(key10, P8, 'en')

def D_block(C8, key10):
    return sdes.SDES(key10, C8, 'de')

def EDE(P8, K1, K2):
    A = E_block(P8, K1)
    B = D_block(A, K2)
    C = E_block(B, K1)
    return C

def fZfil(t, n):
    s = bin(t)[2:]
    return s.rjust(n, '0')

def XOR_bin(a, b):
    if len(a) != len(b):
        raise ValueError("XOR_bin: inputs must have same length")
    xr = int(a, 2) ^ int(b, 2)
    return format(xr, '0{}b'.format(len(a)))

def DT():
    now = dt.datetime.now()
    t = now.microsecond % 256
    return fZfil(t, 8)

def x9_17(K1, K2, Vi):
    tblock = DT()
    a = EDE(tblock, K1, K2)
    b = XOR_bin(Vi, a)
    Ri = EDE(b, K1, K2)
    d = XOR_bin(a, Ri)
    Vi1 = EDE(d, K1, K2)
    return Ri, Vi1

if __name__ == "__main__":
    IV = fZfil(3, 8)
    K1 = '1010000010'
    K2 = '1010000011'

    Ri, Vi1 = x9_17(K1, K2, IV)
    print("Ri (bin)  :", Ri)
    print("Vi1 (bin) :", Vi1)
    print("Ri        :", int(Ri, 2))
