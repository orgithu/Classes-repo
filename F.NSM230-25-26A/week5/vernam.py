import random

def generate_key(plaintext_length):
    key = ""
    for _ in range(plaintext_length):
        key += random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ')
    return key

def encrypt(plaintext, key):
    ciphertext = ""
    for i in range(len(plaintext)):
        p = plaintext[i]
        k = key[i]
        ciphertext += chr(ord(p) ^ ord(k))
    return ciphertext

def decrypt(ciphertext, key):
    decrypted_text = ""
    for i in range(len(ciphertext)):
        c = ciphertext[i]
        k = key[i]
        decrypted_text += chr(ord(c) ^ ord(k))
    return decrypted_text


with open("C:/Others/Cryptograph/CryptoG/week5/plain.txt", "r") as f:
    plaintext = f.read()
key = generate_key(len(plaintext))

ciphertext = encrypt(plaintext, key)

with open("C:/Others/Cryptograph/CryptoG/week5/vernam_cipher.txt", "w") as f:
    f.write(ciphertext)

with open("C:/Others/Cryptograph/CryptoG/week5/vernam_cipher.txt", "r") as f:
    encrypted_data = f.read()

decrypted_text = decrypt(encrypted_data, key)

with open("C:/Others/Cryptograph/CryptoG/week5/vernam_decrypted.txt", "w") as f:
    f.write(decrypted_text)

with open("C:/Others/Cryptograph/CryptoG/week5/vernam_key.txt", "w") as f:
    f.write(key)