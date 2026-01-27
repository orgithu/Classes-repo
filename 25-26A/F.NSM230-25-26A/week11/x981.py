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

if __name__ == "__main__":
    """#example from book
    K = "cfb0ef3108d49cc4562d5810b0a9af60"
    V = "4c89af496176b728ed1e2ea8ba27f5a4"""
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
    print("\nduation:",duration/10**9,'second')