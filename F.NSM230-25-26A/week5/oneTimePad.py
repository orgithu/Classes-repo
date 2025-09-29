import os

def generate_key(length):
    # Generate truly random bytes
    return os.urandom(length)

def encrypt(plaintext_bytes, key):
    # XOR each byte with the key
    return bytes([p ^ k for p, k in zip(plaintext_bytes, key)])

def decrypt(ciphertext_bytes, key):
    # XOR again with the same key
    return bytes([c ^ k for c, k in zip(ciphertext_bytes, key)])

with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/plain.txt", "rb") as f:
    plaintext_bytes = f.read()
key = generate_key(len(plaintext_bytes))
ciphertext = encrypt(plaintext_bytes, key)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/otp_cipher.bin", "wb") as f:
    f.write(ciphertext)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/otp_key.bin", "wb") as f:
    f.write(key)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/otp_cipher.bin", "rb") as f:
    encrypted_data = f.read()
decrypted_text = decrypt(encrypted_data, key)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/otp_decrypted.txt", "wb") as f:
    f.write(decrypted_text)
