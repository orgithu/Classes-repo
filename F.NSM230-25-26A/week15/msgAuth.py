import simpleHash
import aes
import time

def strToBit(pt:str) -> str:
    bytePT = pt.encode('utf-8')
    temp = "".join(format(b, "08b") for b in bytePT)
    return temp

def encryptHash(H:str, K:str) -> str:
    temp = aes.AES(K,H)
    enHash = ''
    for b in temp:
        enHash += hex(b)[2:].rjust(2, '0')
    return enHash

def decryptHash(enH:str, K:str) -> str:
    temp = aes.AES_de(K, aes.st_2_16(enH))
    deHash = ''
    for b in temp:
        deHash += hex(b)[2:].rjust(2, '0')
    return deHash

if __name__ == "__main__":
    start = time.time_ns()

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/plain.txt","r") as f:
        pt = f.read()
        plainBit = strToBit(pt)

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/plain.txt","r") as f:
        M = f.read()

    H = simpleHash.SimpleHashTwo(plainBit,128)
    K = "0123456789abcdef0123456789abcdef"
    enHash = encryptHash(H,K) #E(K,H(M))

    MH = M + '#' + enHash

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/authed.txt","w") as f:
        f.write(MH)
    
    seperate = MH.split('#')
    deHash = decryptHash(seperate[1],K)
    afterHash = simpleHash.SimpleHashTwo(strToBit(seperate[0]),128)
    print("same?",deHash == afterHash)

    end = time.time_ns()
    dur = end - start
    print("time:", dur / 10**9, 's')