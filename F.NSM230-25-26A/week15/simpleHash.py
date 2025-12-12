import time
def bitStrToBlock(bitStr:str, n:int) -> list:
    blocks = []
    i = 0
    while i < len(bitStr):
        block = bitStr[i:i+n]
        blocks.append(block)
        i += n
    return blocks

def ShiftByN(bitStr:str, n:int) -> str:
    return bitStr[n:]+bitStr[:n]

def SimpleHashOne(plainBit:str, n:int) -> str:
    while(len(plainBit) % n) != 0:
        plainBit += '0'
    blocks = bitStrToBlock(plainBit,n)
    hash = 0
    for i in blocks:
        hash ^= int(i,2)
    hexHash = hex(int(format(hash,"0"+str(n)+"b"),2)).removeprefix('0x')
    if len(hexHash) < n/4:
        hexHash += '0'
    return hexHash

def SimpleHashTwo(plainBit:str, n:int) -> str:
    while(len(plainBit) % n) != 0:
        plainBit += '0'
    blocks = bitStrToBlock(plainBit,n)
    hash = 0
    shift = 1
    for i in blocks:
        hash ^= int(i,2)
        hash ^= int(ShiftByN(i,shift), 2)
        shift += 1
    hexHash = hex(int(format(hash,"0"+str(n)+"b"),2)).removeprefix('0x')
    if len(hexHash) < n/4:
        hexHash += '0'
    return hexHash

if __name__ == "__main__":
    start = time.time_ns()
    with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week15/plain.txt","rb") as f:
        plaintext = f.read()
        plainBit = "".join(format(b, "08b") for b in plaintext)
    hashOne = SimpleHashOne(plainBit, 48)
    hashTwo = SimpleHashTwo(plainBit, 48)
    print("hashOne:",hashOne)
    print("hashTwo:",hashTwo)
    end = time.time_ns()
    dur = end - start
    print("hashing time:", dur / 10**9, 's')