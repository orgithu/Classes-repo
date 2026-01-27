def encrypt(p, k):
    result = ""
    for char in p:
        if char.isupper():
            result += chr((ord(char) - 65 + k) % 26 + 65)
        elif char.islower():
            result += chr((ord(char) - 97 + k) % 26 + 97)
        else:
            result += char
    return result

def decrypt(c, k):
    return encrypt(c, -k)

with open("C:/Others/Cryptograph/CryptoG/week4/plain.txt", "r") as f:
    plaintext = f.read()

key = 10
ciphertext = encrypt(plaintext, key)

with open("C:/Others/Cryptograph/CryptoG/week4/caesar_cipher.txt", "w") as f:
    f.write(ciphertext)

with open("C:/Others/Cryptograph/CryptoG/week4/caesar_cipher.txt", "r") as f:
    encrypted_data = f.read()

decrypted_text = decrypt(encrypted_data, key)

with open("C:/Others/Cryptograph/CryptoG/week4/caesar_decrypted.txt", "w") as f:
    f.write(decrypted_text)