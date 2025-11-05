import sdes

E = sdes.encrypt
D = sdes.decrypt

def twoSDES_En(P, K1, K2):
    X = E(P, K1)
    C = E(X, K2)
    return C

def threeSDES_En(P, K1, K2):
    A = E(P, K1)
    B = D(A, K2)
    C = E(B, K1)
    return C

def twoSDES_De(C, K1, K2):
    Y = D(C, K2)
    P = D(Y, K1)
    return P

def threeSDES_De(C, K1, K2):
    B = D(C, K1)
    A = E(B, K2)
    P = D(A, K1)
    return P

if __name__ == "__main__":
    K1 = '0000000001'
    K2 = '1000000000' 
    P = "hello world"
    twoC = twoSDES_En(P, K1, K2)
    threeC = threeSDES_En(P, K1, K2)
    print("2SDES Encrypted:", twoC)
    print("2SDES Decrypted:", twoSDES_De(twoC, K1, K2))
    print("3SDES Encrypted:", threeC)
    print("3SDES Decrypted:", threeSDES_De(threeC, K1, K2))
    print("1SDES Encrypted:", E(P, K1))
    print("1SDES Decrypted:", D(twoC, K2))
    