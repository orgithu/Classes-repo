def generate_key(msg, key):
    key = list(key)
    if len(msg) == len(key):
        return key
    else:
        for i in range(len(msg) - len(key)):
            key.append(key[i % len(key)])
    return "".join(key)

def encrypt_vigenere(msg, key):
    encrypted_text = []
    key = generate_key(msg, key)
    for i in range(len(msg)):
        char = msg[i]
        if char.isupper():           
            #Ci=(Pi+Ki)mod26
            encrypted_char = chr((ord(char) + ord(key[i]) - 2 * ord('A')) % 26 + ord('A'))
        elif char.islower():
            encrypted_char = chr((ord(char) + ord(key[i]) - 2 * ord('a')) % 26 + ord('a'))
        else:
            encrypted_char = char
        encrypted_text.append(encrypted_char)
    return "".join(encrypted_text)

def decrypt_vigenere(msg, key):
    decrypted_text = []
    key = generate_key(msg, key)
    for i in range(len(msg)):
        char = msg[i]
        if char.isupper():
            #Pi=(Ci-Ki+26)mod26
            decrypted_char = chr((ord(char) - ord(key[i]) + 26) % 26 + ord('A'))
        elif char.islower():
            decrypted_char = chr((ord(char) - ord(key[i]) + 26) % 26 + ord('a'))
        else:
            decrypted_char = char
        decrypted_text.append(decrypted_char) 
    return "".join(decrypted_text)

key = "cryptographyisfun"
print("hellooworld")
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/plain.txt", "r") as f:
    plaintext = f.read()

ciphertext = encrypt_vigenere(plaintext, key)

with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/vigenere_cipher.txt", "w") as f:
    f.write(ciphertext)

with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/vigenere_cipher.txt", "r") as f:
    encrypted_data = f.read()

decrypted_text = decrypt_vigenere(encrypted_data, key)

with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/vigenere_decrypted.txt", "w") as f:
    f.write(decrypted_text)

with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/vigenere_key.txt", "w") as f:
    f.write(generate_key(plaintext, key))
