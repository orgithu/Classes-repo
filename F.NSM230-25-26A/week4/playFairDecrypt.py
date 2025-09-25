# Function to convert the string to lowercase
def toLowerCase(plain):
    plain = list(plain)
    for i in range(len(plain)):
        if ord(plain[i]) > 64 and ord(plain[i]) < 91:
            plain[i] = chr(ord(plain[i]) + 32)
    return ''.join(plain)

# Function to remove all spaces in a string
def removeSpaces(plain):
    return ''.join([c for c in plain if c != ' '])

# Function to generate the 5x5 key square
def generateKeyTable(key, keyT):
    n = len(key)

    keyT[:] = [['' for _ in range(5)] for _ in range(5)]

    hashArr = [0] * 26

    for i in range(n):
        if key[i] != 'j':
            hashArr[ord(key[i]) - 97] = 2

    hashArr[ord('j') - 97] = 1

    i = j = 0

    for k in range(n):
        if hashArr[ord(key[k]) - 97] == 2:
            hashArr[ord(key[k]) - 97] -= 1
            keyT[i][j] = key[k]
            j += 1
            if j == 5:
                i += 1
                j = 0

    for k in range(26):
        if hashArr[k] == 0:
            keyT[i][j] = chr(k + 97)
            j += 1
            if j == 5:
                i += 1
                j = 0

# Function to search for the characters of a digraph
# in the key square and return their position
def search(keyT, a, b, arr):
    if a == 'j':
        a = 'i'
    elif b == 'j':
        b = 'i'

    for i in range(5):
        for j in range(5):
            if keyT[i][j] == a:
                arr[0] = i
                arr[1] = j
            elif keyT[i][j] == b:
                arr[2] = i
                arr[3] = j

# Function to decrypt
def decrypt(string, keyT):
    n = len(string)
    string = list(string)
    arr = [0] * 4
    for i in range(0, n, 2):
        search(keyT, string[i], string[i + 1], arr)
        if arr[0] == arr[2]:
            string[i] = keyT[arr[0]][(arr[1] - 1 + 5) % 5]
            string[i + 1] = keyT[arr[0]][(arr[3] - 1 + 5) % 5]
        elif arr[1] == arr[3]:
            string[i] = keyT[(arr[0] - 1 + 5) % 5][arr[1]]
            string[i + 1] = keyT[(arr[2] - 1 + 5) % 5][arr[1]]
        else:
            string[i] = keyT[arr[0]][arr[3]]
            string[i + 1] = keyT[arr[2]][arr[1]]
    return ''.join(string)

# Function to call decrypt
def decryptByPlayfairCipher(string, key):
    keyT = []
    key = removeSpaces(key)
    key = toLowerCase(key)
    string = toLowerCase(string)
    string = removeSpaces(string)
    generateKeyTable(key, keyT)
    return decrypt(string, keyT)

key = "Monarchy"
#string = "gatlmzclrqtx"
#print("Key text:", key)
#print("Plain text:", string)
#string = decryptByPlayfairCipher(string, key)
#print("Decipherred text:", string)

with open("C:/Others/Cryptograph/CryptoG/week4/playFair_cipher.txt", "r") as f:
    ciphertext = f.read()

decrypted = decryptByPlayfairCipher(ciphertext, key)

with open("C:/Others/Cryptograph/CryptoG/week4/playFair_decrypted.txt", "w") as f:
    f.write(decrypted)