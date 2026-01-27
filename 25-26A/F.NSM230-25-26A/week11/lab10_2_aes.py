import aes

#CBC mode
def cbc_encrypt(P, K, IV):
    padded = aes.pad(P)
    ciphertext = ""
    current_iv = IV
    for i in range(0, len(padded), 16):
        block = padded[i:i+16]
        block_hex = ''.join(f'{ord(c):02x}' for c in block)
        xored = aes.xor_hex(block_hex, current_iv)
        encrypted = aes.AES(K, xored)
        encrypted_hex = ''.join(f'{b:02x}' for b in encrypted)
        ciphertext += encrypted_hex
        current_iv = encrypted_hex
    return ciphertext

def cbc_decrypt(C, K, IV):
    plaintext = ""
    current_iv = IV
    for i in range(0, len(C), 32):
        block_hex = C[i:i+32]
        decrypted = aes.AES_de(K, aes.st_2_16(block_hex))
        decrypted_hex = ''.join(f'{b:02x}' for b in decrypted)
        xored = aes.xor_hex(decrypted_hex, current_iv)
        plaintext += ''.join(chr(int(xored[j:j+2],16)) for j in range(0,32,2))
        current_iv = block_hex
    return aes.unpad(plaintext)

# OFB mode
def ofb_encrypt(P, K, nonce):
    padded = aes.pad(P)
    ciphertext = ""
    current_nonce = nonce
    for i in range(0, len(padded), 16):
        block = padded[i:i+16]
        block_hex = ''.join(f'{ord(c):02x}' for c in block)
        keystream = aes.AES(K, current_nonce)
        keystream_hex = ''.join(f'{b:02x}' for b in keystream)
        xored = aes.xor_hex(block_hex, keystream_hex)
        ciphertext += xored
        current_nonce = keystream_hex
    return ciphertext

def ofb_decrypt(C, K, nonce):
    plaintext = ""
    current_nonce = nonce
    for i in range(0, len(C), 32):
        block_hex = C[i:i+32]
        keystream = aes.AES(K, current_nonce)
        keystream_hex = ''.join(f'{b:02x}' for b in keystream)
        xored = aes.xor_hex(block_hex, keystream_hex)
        plaintext += ''.join(chr(int(xored[j:j+2],16)) for j in range(0,32,2))
        current_nonce = keystream_hex
    return aes.unpad(plaintext)

# CTR mode
def ctr_encrypt(P, K, count=0):
    padded = aes.pad(P)
    ciphertext = ""
    current_count = count
    for i in range(0, len(padded), 16):
        block = padded[i:i+16]
        block_hex = ''.join(f'{ord(c):02x}' for c in block)
        count_hex = f'{current_count:032x}'
        keystream = aes.AES(K, count_hex)
        keystream_hex = ''.join(f'{b:02x}' for b in keystream)
        xored = aes.xor_hex(block_hex, keystream_hex)
        ciphertext += xored
        current_count += 1
    return ciphertext

def ctr_decrypt(C, K, count=0):
    plaintext = ""
    current_count = count
    for i in range(0, len(C), 32):
        block_hex = C[i:i+32]
        count_hex = f'{current_count:032x}'
        keystream = aes.AES(K, count_hex)
        keystream_hex = ''.join(f'{b:02x}' for b in keystream)
        xored = aes.xor_hex(block_hex, keystream_hex)
        plaintext += ''.join(chr(int(xored[j:j+2],16)) for j in range(0,32,2))
        current_count += 1
    return aes.unpad(plaintext)
"""
if __name__ == "__main__":
    IV = "fedcba9876543210fedcba9876543210"  # 16-byte IV in hex
    nonce = IV  # Use same for nonce
    K = "0123456789abcdef0123456789abcdef"  # 16-byte key in hex
    count = 0
    P = "hello world"
    
    # CBC
    C = cbc_encrypt(P, K, IV)
    print("CBC Ciphertext (hex):", C)
    decrypted = cbc_decrypt(C, K, IV)
    print("CBC Decrypted:", repr(decrypted))

    # OFB
    ofb_hex = ofb_encrypt(P, K, nonce)
    print("OFB Ciphertext (hex):", ofb_hex)
    ofb_decrypted = ofb_decrypt(ofb_hex, K, nonce)
    print("OFB Decrypted:", repr(ofb_decrypted))
    
    # CTR
    ctr_hex = ctr_encrypt(P, K, count)
    print("CTR Ciphertext (hex):", ctr_hex)
    ctr_decrypted = ctr_decrypt(ctr_hex, K, count)
    print("CTR Decrypted:", repr(ctr_decrypted))"""