import time

# S-AES Constants and Tables
S_BOX = [
    [0x9, 0x4, 0xA, 0xB],
    [0xD, 0x1, 0x8, 0x5],
    [0x6, 0x2, 0x0, 0x3],
    [0xC, 0xE, 0xF, 0x7]
]
INV_S_BOX = [
    [0xA, 0x5, 0x9, 0xB],
    [0x1, 0x7, 0x8, 0xF],
    [0x6, 0x0, 0x2, 0x3],
    [0xC, 0x4, 0xD, 0xE]
]
MIX_COL_MATRIX = [
    [0x1, 0x4],
    [0x4, 0x1]
]
INV_MIX_COL_MATRIX = [
    [0x9, 0x2],
    [0x2, 0x9]
]
RCON = [0x80, 0x30]
def gfMult(a, b):
    p = 0
    while b > 0:
        if b & 1:
            p ^= a
        a <<= 1
        if a & 0x10:
            a ^= 0x13
        b >>= 1
    return p & 0xF
def stateToMatrix(state):
    return [
        [(state >> 12) & 0xF, (state >> 4) & 0xF],
        [(state >> 8) & 0xF, (state >> 0) & 0xF]
    ]
def matrixToState(matrix):
    return (matrix[0][0] << 12) | (matrix[1][0] << 8) | \
           (matrix[0][1] << 4) | (matrix[1][1] << 0)
def subNib(nibble, sBox):
    row = (nibble >> 2) & 0x3
    col = nibble & 0x3
    return sBox[row][col]
def rotNib(word):
    return ((word << 4) | (word >> 4)) & 0xFF
def keyExpansion(keyInt):
    w0 = (keyInt >> 8) & 0xFF
    w1 = keyInt & 0xFF
    temp = rotNib(w1)
    tempSubbed = (subNib((temp >> 4) & 0xF, S_BOX) << 4) | subNib(temp & 0xF, S_BOX)
    w2 = w0 ^ RCON[0] ^ tempSubbed
    w3 = w2 ^ w1
    temp = rotNib(w3)
    tempSubbed = (subNib((temp >> 4) & 0xF, S_BOX) << 4) | subNib(temp & 0xF, S_BOX)
    w4 = w2 ^ RCON[1] ^ tempSubbed
    w5 = w4 ^ w3
    k0 = (w0 << 8) | w1
    k1 = (w2 << 8) | w3
    k2 = (w4 << 8) | w5
    return k0, k1, k2
def encrypt(plaintextBin, keyBin):
    keyInt = int(keyBin.replace(' ', ''), 2)
    plaintextInt = int(plaintextBin.replace(' ', ''), 2)
    k0, k1, k2 = keyExpansion(keyInt)
    state = plaintextInt ^ k0
    sMatrix = stateToMatrix(state)
    sMatrix[0][0] = subNib(sMatrix[0][0], S_BOX)
    sMatrix[1][0] = subNib(sMatrix[1][0], S_BOX)
    sMatrix[0][1] = subNib(sMatrix[0][1], S_BOX)
    sMatrix[1][1] = subNib(sMatrix[1][1], S_BOX)
    sMatrix[1][0], sMatrix[1][1] = sMatrix[1][1], sMatrix[1][0]
    newMatrix = [[0, 0], [0, 0]]
    for i in range(2):
        for j in range(2):
            c0 = sMatrix[0][j]
            c1 = sMatrix[1][j]
            m0 = MIX_COL_MATRIX[i][0]
            m1 = MIX_COL_MATRIX[i][1]
            newMatrix[i][j] = gfMult(m0, c0) ^ gfMult(m1, c1)
    state = matrixToState(newMatrix)
    state ^= k1
    sMatrix = stateToMatrix(state)
    sMatrix[0][0] = subNib(sMatrix[0][0], S_BOX)
    sMatrix[1][0] = subNib(sMatrix[1][0], S_BOX)
    sMatrix[0][1] = subNib(sMatrix[0][1], S_BOX)
    sMatrix[1][1] = subNib(sMatrix[1][1], S_BOX)
    sMatrix[1][0], sMatrix[1][1] = sMatrix[1][1], sMatrix[1][0]
    state = matrixToState(sMatrix)
    ciphertext = state ^ k2
    return ciphertext
def decrypt(ciphertextInt, keyBin):
    keyInt = int(keyBin.replace(' ', ''), 2)
    k0, k1, k2 = keyExpansion(keyInt)
    state = ciphertextInt
    state ^= k2
    sMatrix = stateToMatrix(state)
    sMatrix[1][0], sMatrix[1][1] = sMatrix[1][1], sMatrix[1][0]
    sMatrix[0][0] = subNib(sMatrix[0][0], INV_S_BOX)
    sMatrix[1][0] = subNib(sMatrix[1][0], INV_S_BOX)
    sMatrix[0][1] = subNib(sMatrix[0][1], INV_S_BOX)
    sMatrix[1][1] = subNib(sMatrix[1][1], INV_S_BOX)
    state = matrixToState(sMatrix)
    state ^= k1
    sMatrix = stateToMatrix(state)
    newMatrix = [[0, 0], [0, 0]]
    for i in range(2):
        for j in range(2):
            c0 = sMatrix[0][j]
            c1 = sMatrix[1][j]
            m0 = INV_MIX_COL_MATRIX[i][0]
            m1 = INV_MIX_COL_MATRIX[i][1]
            newMatrix[i][j] = gfMult(m0, c0) ^ gfMult(m1, c1)
    state = matrixToState(newMatrix)
    sMatrix = stateToMatrix(state)
    sMatrix[1][0], sMatrix[1][1] = sMatrix[1][1], sMatrix[1][0]
    sMatrix[0][0] = subNib(sMatrix[0][0], INV_S_BOX)
    sMatrix[1][0] = subNib(sMatrix[1][0], INV_S_BOX)
    sMatrix[0][1] = subNib(sMatrix[0][1], INV_S_BOX)
    sMatrix[1][1] = subNib(sMatrix[1][1], INV_S_BOX)
    plaintext = matrixToState(sMatrix)
    plaintext ^= k0
    return plaintext
def binaryFormatSplit(integer):
    s = "{:016b}".format(integer)
    return "{} {} {} {}".format(s[0:4], s[4:8], s[8:12], s[12:16])

if __name__ == "__main__":
    """
    6.2Create software that can encrypt and decrypt using S-AES. Test data: A binary
    plaintext of 0110 1111 0110 1011 encrypted with a binary key of 1010 0111 0011 1011
    should give a binary ciphertext of 0000 0111 0011 1000. Decryption should work
    correspondingly.
    """
    start = time.time_ns()
    plaintextBin = "0110 1111 0110 1011"
    keyBin = "1010 0111 0011 1011"
    expectedCiphertextBin = "0000 0111 0011 1000"

    print("S-AES implementation\n")
    print("Plaintext bin:", plaintextBin)
    print("Key binary:", keyBin)
    print("Expected CT bin:", expectedCiphertextBin)

    ciphertextInt = encrypt(plaintextBin, keyBin)
    ciphertextFormatted = binaryFormatSplit(ciphertextInt)

    print("\nEncrypted:", ciphertextFormatted)

    encryptionStatus = "matches expected binary" if ciphertextFormatted == expectedCiphertextBin else "failed"
    print("Status:", encryptionStatus)

    decryptedInt = decrypt(ciphertextInt, keyBin)
    decryptedFormatted = binaryFormatSplit(decryptedInt)

    print("\nDecrypted:", decryptedFormatted)

    decryptionStatus = "matches plaintext binary" if decryptedFormatted == plaintextBin else "failed"
    print("Status:", decryptionStatus)
    end = time.time_ns()
    dur = end - start
    print("\nduration: ",dur/10**9,"second")