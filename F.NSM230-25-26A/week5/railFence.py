def encryptRailFence(text, key):
    rail = [['\n' for i in range(len(text))]
                for j in range(key)]
    
    dir_down = False
    row, col = 0, 0
    
    for i in range(len(text)):
        if (row == 0) or (row == key - 1):
            dir_down = not dir_down
        
        rail[row][col] = text[i]
        col += 1
        
        if dir_down:
            row += 1
        else:
            row -= 1

    result = []
    for i in range(key):
        for j in range(len(text)):
            if rail[i][j] != '\n':
                result.append(rail[i][j])
    return("" . join(result))
    

def decryptRailFence(cipher, key):
    rail = [['\n' for i in range(len(cipher))]
                for j in range(key)]
    
    dir_down = None
    row, col = 0, 0
    
    for i in range(len(cipher)):
        if row == 0:
            dir_down = True
        if row == key - 1:
            dir_down = False
        
        rail[row][col] = '*'
        col += 1
        
        if dir_down:
            row += 1
        else:
            row -= 1
            
    index = 0
    for i in range(key):
        for j in range(len(cipher)):
            if ((rail[i][j] == '*') and
            (index < len(cipher))):
                rail[i][j] = cipher[index]
                index += 1
        
    result = []
    row, col = 0, 0
    for i in range(len(cipher)):
        if row == 0:
            dir_down = True
        if row == key-1:
            dir_down = False
            
        if (rail[row][col] != '*'):
            result.append(rail[row][col])
            col += 1
            
        if dir_down:
            row += 1
        else:
            row -= 1
    return("".join(result))


#if __name__ == "__main__":
    print(encryptRailFence("attack at once", 2))
    print(encryptRailFence("GeeksforGeeks ", 3))
    print(encryptRailFence("defend the east wall", 3))
    
    print(decryptRailFence("GsGsekfrek eoe", 3))
    print(decryptRailFence("atc toctaka ne", 2))
    print(decryptRailFence("dnhaweedtees alf  tl", 3))

with open("C:/Others/Cryptograph/CryptoG/week5/plain.txt", "r") as f:
    plaintext = f.read()
key = 3
ciphertext = encryptRailFence(plaintext, key)

with open("C:/Others/Cryptograph/CryptoG/week5/railFence_cipher.txt", "w") as f:
    f.write(ciphertext)

with open("C:/Others/Cryptograph/CryptoG/week5/railFence_cipher.txt", "r") as f:
    encrypted_data = f.read()

decrypted_text = decryptRailFence(encrypted_data, key)

with open("C:/Others/Cryptograph/CryptoG/week5/railFence_decrypted.txt", "w") as f:
    f.write(decrypted_text)

with open("C:/Others/Cryptograph/CryptoG/week5/railFence_key.txt", "w") as f:
    f.write(key)