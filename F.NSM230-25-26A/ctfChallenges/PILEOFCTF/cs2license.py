import base64

def xor_decrypt(data, key):
    out = []
    for i in range(len(data)):
        out.append(chr(data[i] ^ ord(key[i % len(key)])))
    return ''.join(out)

b64 = "IDsnRE0XAg4cAg03GRMHEAglAgEDDDdRCkcRVRxYCxo="
data = base64.b64decode(b64)
key = "haruulzangi"

print(xor_decrypt(data, key))
