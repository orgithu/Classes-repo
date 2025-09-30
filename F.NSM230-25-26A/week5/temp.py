
import os

def generate_key(length):
    # Generate truly random bytes
    return os.urandom(length)
print("key: ", generate_key(1))