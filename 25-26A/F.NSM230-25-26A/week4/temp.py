import string
import random

def caesar_brute(encrypted_p):
    for shift in range(26):
        decrypted_text = []
        for char in encrypted_p:
            if 'a' <= char <= 'z':
                d = (ord(char) - ord('a') - shift) % 26
                decrypted_text.append(chr(d + ord('a')))
            elif 'A' <= char <= 'Z':
                d = (ord(char) - ord('A') - shift) % 26
                decrypted_text.append(chr(d + ord('A')))
            else:
                decrypted_text.append(char)
        print(''.join(decrypted_text))
def generate_key():
    letters = list(string.ascii_lowercase)
    random.shuffle(letters)
    return ''.join(letters)  # return shuffled alphabet as a string

# Example usage
key = generate_key()
print(key)  # e.g., "qwertyuiopasdfghjklzxcvbnm"
