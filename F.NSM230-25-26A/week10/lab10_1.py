import sdes
import random

def randomBit(length):
    bitString = ""
    for _ in range(length):
        bitString += str(random.randint(0, 1))
    return bitString
E = sdes.encrypt
D = sdes.decrypt

if __name__ == "__main__":
    P = "hello world"
    K1 = randomBit(10)
    K2 = randomBit(10)
    C = E(E(P, K1), K2)
    print("Plaintext:", P)
    print("Ciphertext:", C)
    print("Actual K1:", K1)
    print("Actual K2:", K2)
    
    encrypted = {}
    for i in range(1024):
        k1 = '{0:010b}'.format(i)
        temp = E(P, k1)
        encrypted[temp] = k1
    for j in range(1024):
        k2 = '{0:010b}'.format(j)
        temp = D(C, k2)
        if temp in encrypted:
            found_k1 = encrypted[temp]
            print("Found keys! K1 =", found_k1, "K2 =", k2)
            break