#figure 11.4 a implementation
import simpleHash
import time
import msgAuth
import diyRSA


if __name__ == "__main__":
    start = time.time_ns()
    p = 1313131313131313131313131 #25 digit
    q = 1304313049130631309313099
    hashBit = 48
    PU,PR = diyRSA.keys(p, q)

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/plain.txt","r") as f:
        pt = f.read()
        plainBit = msgAuth.strToBit(pt)
        M = f.read()

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/plain.txt","r") as f:
        M = f.read()

    H = simpleHash.SimpleHashTwo(plainBit,hashBit)
    print("hash: ",H)
    enHash = diyRSA.encrypt(H,PR)
    MH = M + '#' + enHash

    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/signed.txt","w") as f:
        f.write(MH)
    
    seperate = MH.split('#')
    deHash = diyRSA.decrypt(seperate[1],PU)
    afterHash = simpleHash.SimpleHashTwo(msgAuth.strToBit(seperate[0]),hashBit)
    print("same?",deHash == afterHash)

    end = time.time_ns()
    dur = end - start
    print("time:", dur / 10**9, 's')