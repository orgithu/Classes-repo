import string
import random
from collections import Counter

def generate_key():
    letters = list(string.ascii_lowercase)
    shuffled = letters.copy()
    random.shuffle(shuffled)
    key = dict(zip(letters, shuffled))
    return key

def mono_encrypt(plaintext, key):
    result = ""
    for char in plaintext:
        if char.islower():
            result += key[char]
        elif char.isupper():
            result += key[char.lower()].upper()
        else:
            result += char
    return result

def mono_decrypt(ciphertext, key):
    reverse_key = {}
    for k,v in key.items():
        reverse_key[v] = k
    result = ""
    for char in ciphertext:
        if char.islower():
            result += reverse_key[char]
        elif char.isupper():
            result += reverse_key[char.lower()].upper()
        else:
            result += char
    return result

def mono_brute(ciphertext):
    counts = Counter(c for c in ciphertext.lower() if c.isalpha())
    all_letters = list(string.ascii_lowercase)
    cipher_rank = [k for k, _ in counts.most_common()]
    for ch in all_letters:
        if ch not in cipher_rank:
            cipher_rank.append(ch)
    english_rank = list("etaoinshrdlcumwfgypbvkjxqz")
    brute_key = dict(zip(english_rank, cipher_rank))
    brutetext = mono_decrypt(ciphertext, brute_key)
    return brutetext

result = ""
key = generate_key()
print(key)
plaintext = "Hello how are you im under the water"
ciphertext = mono_encrypt(plaintext, key)
decryptedtext = mono_decrypt(ciphertext, key)
brutetext = mono_brute(ciphertext)
print("plaintext: ", plaintext)
print("ciphertext: ", ciphertext)
print("decrypted: ", decryptedtext)
print("brutetext: ", brutetext)