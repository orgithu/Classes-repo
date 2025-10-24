import base64
import sys
from pathlib import Path

ENCRYPTED_B64 = (
    "TwMdLyVaRRsOCCZ3WA0aHwUndzccEBkMNmk2FhwGVW09DQ0fDld+ZgwcEg9XfisLHQpVIRh7VEtH"
    "ED0qehccOAINMXkKDUMYBi8sUREaBQ4/dUsbHA8QfHVLEQcGBXw="
)

def xor_decrypt(cipher_bytes: bytes, key_bytes: bytes) -> bytes:
    if not key_bytes:
        raise ValueError("Key must not be empty.")
    out = bytearray(len(cipher_bytes))
    klen = len(key_bytes)
    for i, b in enumerate(cipher_bytes):
        out[i] = b ^ key_bytes[i % klen]
    return bytes(out)

def decrypt_from_b64(b64string: str, key: str) -> str:
    cipher = base64.b64decode(b64string)
    plain_bytes = xor_decrypt(cipher, key.encode("utf-8"))
    try:
        return plain_bytes.decode("utf-8")
    except UnicodeDecodeError:
        return plain_bytes.decode("latin-1", errors="replace")
    
def main(key):
    try:
        plaintext = decrypt_from_b64(ENCRYPTED_B64, key)
    except Exception as e:
        print("Error:", e)
        sys.exit(1)
    return plaintext
if __name__ == "__main__":
    count = 0
    for i in range(57):
        plaintext = main(chr(i + ord('A')))
        print(plaintext)
        print("----------------------------------------------------------------------")
    #print("Number of pattern found: ", count)
