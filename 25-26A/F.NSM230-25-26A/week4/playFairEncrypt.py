# Function to convert the string to lowercase
def toLowerCase(plain):
    n = len(plain)
    result = ""
    for i in range(n):
        if 64 < ord(plain[i]) < 91:
            result += chr(ord(plain[i]) + 32)
        else:
            result += plain[i]
    return result

# Function to remove all spaces in a string
def removeSpaces(plain):
    n = len(plain)
    temp = ""
    for i in range(n):
        if plain[i] != ' ':
            temp += plain[i]
    return temp

# Function to generate the 5x5 key square
def generateKeyTable(key, keyT):
    n = len(key)

    keyT.clear()
    for i in range(5):
        keyT.append([0]*5)

    hashMap = [0]*26

    for i in range(n):
        if key[i] != 'j':
            hashMap[ord(key[i]) - 97] = 2

    hashMap[ord('j') - 97] = 1

    i = 0
    j = 0

    for k in range(n):
        if hashMap[ord(key[k]) - 97] == 2:
            hashMap[ord(key[k]) - 97] -= 1
            keyT[i][j] = key[k]
            j += 1
            if j == 5:
                i += 1
                j = 0

    for k in range(26):
        if hashMap[k] == 0:
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
    if b == 'j':
        b = 'i'

    for i in range(5):
        for j in range(5):
            if keyT[i][j] == a:
                arr[0] = i
                arr[1] = j
            elif keyT[i][j] == b:
                arr[2] = i
                arr[3] = j

# Function to make the plain text length to be even
def prepare(string):
    if len(string) % 2 != 0:
        string += 'z'
    return string

# Function for performing the encryption
def encrypt(string, keyT):
    n = len(string)
    arr = [0]*4

    result = list(string)
    for i in range(0, n, 2):
        search(keyT, result[i], result[i+1], arr)

        if arr[0] == arr[2]:
            result[i] = keyT[arr[0]][(arr[1] + 1) % 5]
            result[i+1] = keyT[arr[0]][(arr[3] + 1) % 5]
        elif arr[1] == arr[3]:
            result[i] = keyT[(arr[0] + 1) % 5][arr[1]]
            result[i+1] = keyT[(arr[2] + 1) % 5][arr[1]]
        else:
            result[i] = keyT[arr[0]][arr[3]]
            result[i+1] = keyT[arr[2]][arr[1]]

    return ''.join(result)

# Function to encrypt using Playfair Cipher
def encryptByPlayfairCipher(string, key):
    keyT = []
    key = toLowerCase(removeSpaces(key))
    string = toLowerCase(removeSpaces(string))
    string = prepare(string)
    generateKeyTable(key, keyT)
    return encrypt(string, keyT)

key = "Monarchy"
#string = "instruments"
#print("Key text:", key)
#print("Plain text:", string)
#string = encryptByPlayfairCipher(string, key)
#print("Cipher text:", string)

with open("C:/Others/Cryptograph/CryptoG/week4/plain.txt", "r") as f:
    plaintext = f.read()

ciphertext = encryptByPlayfairCipher(plaintext, key)

with open("C:/Others/Cryptograph/CryptoG/week4/playFair_cipher.txt", "w") as f:
    f.write(ciphertext)