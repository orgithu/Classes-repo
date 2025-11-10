import sdes

def decimalToBinary(n, b):
    bnr = bin(n).replace("0b", "")
    return bnr.rjust(b, '0')

def xor(str1, str2):
    xr1 = int(str1, 2) ^ int(str2, 2)
    return bin(xr1).replace('0b', '').zfill(len(str1))

def pad_binary(data_bits, block_size=8):
    remainder = len(data_bits) % block_size
    if remainder == 0:
        return data_bits  # No padding needed
    padding_len = block_size - remainder
    padding_byte = decimalToBinary(padding_len, block_size)
    return data_bits + padding_byte * padding_len

def unpad_binary(padded_data_bits, block_size=8):
    if len(padded_data_bits) % block_size != 0:
        raise ValueError("Invalid data length")
    if len(padded_data_bits) == 0:
        return padded_data_bits
    last_block = padded_data_bits[-block_size:]
    padding_len = int(last_block, 2)
    if padding_len == 0 or padding_len > block_size:
        # If padding_len == 0, means no padding was added
        return padded_data_bits
    # Check padding
    for i in range(1, padding_len + 1):
        if padded_data_bits[-i * block_size: -(i-1) * block_size] != decimalToBinary(padding_len, block_size):
            raise ValueError("Invalid padding")
    return padded_data_bits[:-padding_len * block_size]

def cbc_encrypt(plaintext, key, iv_bin):
    # Convert plaintext to binary
    data_bits = ''.join(decimalToBinary(ord(c), 8) for c in plaintext)
    padded_data = pad_binary(data_bits)
    
    ciphertext_blocks = []
    current_iv = iv_bin
    for i in range(0, len(padded_data), 8):
        block = padded_data[i:i+8]
        xored = xor(block, current_iv)
        encrypted = sdes.SDES(key, xored, 'en')
        ciphertext_blocks.append(encrypted)
        current_iv = encrypted
    
    # Convert to hex for output
    ciphertext_hex = ''.join(format(int(block, 2), '02x') for block in ciphertext_blocks)
    return ciphertext_hex

def cbc_decrypt(ciphertext_hex, key, iv_bin):
    # Convert hex to binary blocks
    ciphertext_blocks = []
    for i in range(0, len(ciphertext_hex), 2):
        byte_hex = ciphertext_hex[i:i+2]
        block = decimalToBinary(int(byte_hex, 16), 8)
        ciphertext_blocks.append(block)
    
    plaintext_bits = []
    current_iv = iv_bin
    for block in ciphertext_blocks:
        decrypted = sdes.SDES(key, block, 'de')
        xored_back = xor(decrypted, current_iv)
        plaintext_bits.append(xored_back)
        current_iv = block
    
    # Unpad and convert to string
    padded_bits = ''.join(plaintext_bits)
    data_bits = unpad_binary(padded_bits)
    plaintext = ''.join(chr(int(data_bits[i:i+8], 2)) for i in range(0, len(data_bits), 8))
    return plaintext
#OFB

def ofb_encrypt(plaintext, key, nonce):
    # Convert plaintext to binary
    data_bits = ''.join(decimalToBinary(ord(c), 8) for c in plaintext)
    padded_data = pad_binary(data_bits)
    
    ciphertext_blocks = []
    current_nonce = nonce
    for i in range(0, len(padded_data), 8):
        block = padded_data[i:i+8]
        encrypted = sdes.SDES(key, current_nonce, 'en')
        xored = xor(block, encrypted)
        ciphertext_blocks.append(xored)
        current_nonce = encrypted
    
    # Convert to hex for output
    ciphertext_hex = ''.join(format(int(block, 2), '02x') for block in ciphertext_blocks)
    return ciphertext_hex
def ofb_decrypt(ciphertext_hex, key, nonce):
    # Convert hex to binary blocks
    ciphertext_blocks = []
    for i in range(0, len(ciphertext_hex), 2):
        byte_hex = ciphertext_hex[i:i+2]
        block = decimalToBinary(int(byte_hex, 16), 8)
        ciphertext_blocks.append(block)
    
    plaintext_bits = []
    current_nonce = nonce
    for block in ciphertext_blocks:
        encrypted = sdes.SDES(key, current_nonce, 'en')
        xored_back = xor(encrypted, block)
        plaintext_bits.append(xored_back)
        current_nonce = encrypted
    
    # Unpad and convert to string
    padded_bits = ''.join(plaintext_bits)
    data_bits = unpad_binary(padded_bits)
    plaintext = ''.join(chr(int(data_bits[i:i+8], 2)) for i in range(0, len(data_bits), 8))
    return plaintext
#CTR mode
def ctr_encrypt(plaintext, key, count=0):
    # Convert plaintext to binary
    data_bits = ''.join(decimalToBinary(ord(c), 8) for c in plaintext)
    padded_data = pad_binary(data_bits)
    
    ciphertext_blocks = []
    current_count = decimalToBinary(count, 8)
    for i in range(0, len(padded_data), 8):
        block = padded_data[i:i+8]
        encrypted = sdes.SDES(key, current_count, 'en')
        xored = xor(block, encrypted)
        ciphertext_blocks.append(xored)
        current_count = int(current_count, 2)
        current_count += 1
        current_count = decimalToBinary(current_count, 8)
    # Convert to hex for output
    ciphertext_hex = ''.join(format(int(block, 2), '02x') for block in ciphertext_blocks)
    return ciphertext_hex
def ctr_decrypt(ciphertext_hex, key, count=0):
    # Convert hex to binary blocks
    ciphertext_blocks = []
    for i in range(0, len(ciphertext_hex), 2):
        byte_hex = ciphertext_hex[i:i+2]
        block = decimalToBinary(int(byte_hex, 16), 8)
        ciphertext_blocks.append(block)
    
    plaintext_bits = []
    current_count = decimalToBinary(count, 8)
    for block in ciphertext_blocks:
        encrypted = sdes.SDES(key, current_count, 'en')
        xored_back = xor(encrypted, block)
        plaintext_bits.append(xored_back)
        current_count = int(current_count, 2)
        current_count += 1
        current_count = decimalToBinary(current_count, 8)    
    # Unpad and convert to string
    padded_bits = ''.join(plaintext_bits)
    data_bits = unpad_binary(padded_bits)
    plaintext = ''.join(chr(int(data_bits[i:i+8], 2)) for i in range(0, len(data_bits), 8))
    return plaintext

if __name__ == "__main__":
    iv_bin = "01010101"  # 8-bit IV
    nonce = iv_bin
    count = 0
    K = "0011001100"    # 10-bit key
    P = "hello world"
    
    # CBC
    C_hex = cbc_encrypt(P, K, iv_bin)
    print("CBC Ciphertext (hex):", C_hex)
    decrypted = cbc_decrypt(C_hex, K, iv_bin)
    print("CBC Decrypted:", repr(decrypted))

    # OFB
    ofbHex = ofb_encrypt(P, K, nonce)
    print("OFB Ciphertext", ofbHex)
    ofbDecrypted = ofb_decrypt(ofbHex, K, nonce)
    print("OFB Decrypted", ofbDecrypted)
    
    # CTR
    ctrHex = ctr_encrypt(P, K, count)
    print("CTR Ciphertext", ctrHex)
    ctrDecrypted = ctr_decrypt(ctrHex, K, count)
    print("CTR Decrypted", ctrDecrypted)
        