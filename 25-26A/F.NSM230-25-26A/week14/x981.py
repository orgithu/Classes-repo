import aes
import os
import time

#CTR mode
def ctrPrngHex(K: str, V: str, nBytes: int) -> str:
    out = ""
    blocks = (nBytes + 15) // 16
    start = int(V, 16)
    for i in range(blocks):
        cnt = start + i #V = (V + 1)
        cnt = cnt % (2 ** 128) #_ mod 2^128
        countHex = hex(cnt)[2:].zfill(32)
        block = aes.AES(K, countHex) #output_block = E(K, V)
        blockBytes = bytes(block)
        blockHex = blockBytes.hex()
        out += blockHex #temp += output_block
    return out[: nBytes * 2]

#OFB mode
def ofbPrngHex(keyHex: str, vHex: str, nBytes: int) -> str:
    out = ""
    current = vHex
    blocks = (nBytes + 15) // 16
    for _ in range(blocks):
        block = aes.AES(keyHex, current) #V = E(K, V)
        blockBytes = bytes(block)
        blockHex = blockBytes.hex()
        out += blockHex #temp += V
        current = blockHex
    return out[: nBytes * 2]

def ctrRand(mod:int) -> int:
    """return 0-mod integer using x981 ctr mode."""
    seed=os.urandom(32)
    K = seed[:16].hex()
    V = seed[16:].hex()
    return (int(ctrPrngHex(K,V,64),16)) % mod

"""
if __name__ == "__main__":
    start = time.time_ns()
    seed = os.urandom(32)
    K = seed[:16].hex()
    V = seed[16:].hex()
    ctrHex = ctrPrngHex(K, V, 64)
    ofbHex = ofbPrngHex(K, V, 64)
    print("\nCTR PRNG 64 bytes (hex):", ctrHex)
    print("\nOFB PRNG 64 bytes (hex):", ofbHex)
    ctrInt = int(ctrHex,16)
    ofbInt = int(ofbHex,16)
    print("\nCTR In decimal",ctrInt)
    print("\nOFB In decimal",ofbInt)
    print("\nRandom number 1-100 ctr",ctrInt%100)
    print("Random number 1-100 ofb",ofbInt%100)
    end = time.time_ns()
    duration = end-start
    print("\nduation:",duration/10**9,'second')"""