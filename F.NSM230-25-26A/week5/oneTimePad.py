import random

abc = "abcdefghijklmnopqrstuvwxyz"

def generate_otp_key(length):
    return ''.join(random.choice(abc) for _ in range(length))


def otp_encrypt(plaintext, key):
    plaintext = plaintext.lower()
    key = key.lower()
    new_c = ""
    for i in range(len(plaintext)):
        eachletter = plaintext[i]
        if eachletter in abc:
            p = abc.index(eachletter)
            k = abc.index(key[i])
            c = (p + k) % 26
            new_c += abc[c]
        else:
            new_c += eachletter  # keep spaces/punctuation unchanged
    return new_c.upper()


def otp_decrypt(ciphertext, key):
    ciphertext = ciphertext.lower()
    key = key.lower()
    new_p = ""
    for i in range(len(ciphertext)):
        eachletter = ciphertext[i]
        if eachletter in abc:
            p = abc.index(eachletter)
            k = abc.index(key[i])
            c = (p - k) % 26
            new_p += abc[c]
        else:
            new_p += eachletter
    return new_p.upper()


# --- File usage ---
with open("C:/Others/Cryptograph/CryptoG/week5/plain.txt", "r") as f:
    plaintext = f.read().strip()

# Generate one-time pad key (same length as plaintext)
key = generate_otp_key(len(plaintext))

# Save the key for decryption later
with open("C:/Others/Cryptograph/CryptoG/week5/otp_key.txt", "w") as f:
    f.write(key)

# Encrypt
ciphertext = otp_encrypt(plaintext, key)
with open("C:/Others/Cryptograph/CryptoG/week5/otp_cipher.txt", "w") as f:
    f.write(ciphertext)

# Decrypt
with open("C:/Others/Cryptograph/CryptoG/week5/otp_cipher.txt", "r") as f:
    encrypted_data = f.read().strip()

with open("C:/Others/Cryptograph/CryptoG/week5/otp_key.txt", "r") as f:
    stored_key = f.read().strip()

decrypted_text = otp_decrypt(encrypted_data, stored_key)
with open("C:/Others/Cryptograph/CryptoG/week5/otp_decrypted.txt", "w") as f:
    f.write(decrypted_text)
