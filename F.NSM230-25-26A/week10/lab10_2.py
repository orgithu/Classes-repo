import os
import math
# You will need to install this library: pip install pycryptodome
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Util import Counter

# --- S-DES CONSTANTS ---
BLOCK_SIZE_SDES = 8  # S-DES operates on 8-bit blocks
BLOCK_SIZE_AES = 16  # AES operates on 128-bit blocks (16 bytes)

# Permutation tables for S-DES
p10=[3, 5, 2, 7, 4, 10, 1, 9, 8, 6]         # P10 permutation
p8=[6, 3, 7, 4, 8, 5, 10, 9]                # P8 permutation
ip8=[2, 6, 3, 1, 4, 8, 5, 7]                # Initial permutation (IP)
ep8=[4,1,2,3,2,3,4,1]                       # Expansion permutation (E/P)
pi4=[2,4,3,1]                               # P4 permutation
ip_1=[4,1,3,5,7,2,8,6]                      # Inverse IP (IP^-1)

# S-boxes for S-DES
s0=[['01','00','11','10'],
    ['11','10','01','00'],
    ['00','10','01','11'],
    ['11','01','11','10']]

s1=[['00','01','10','11'],
    ['10','00','01','11'],
    ['11','00','01','00'],
    ['10','01','00','11']]

# ====================================================================
# S-DES HELPER FUNCTIONS (Key Generation and Block Core)
# ====================================================================

def decimalToBinary(n, b):
    """Convert decimal to binary (padded to b bits)."""
    bnr = bin(n).replace("0b", "")
    return bnr.rjust(b, '0')

def ip(key, st):
    """General permutation function: rearranges bits according to table."""
    s = ''
    for i in key:
        s += st[i-1]
    return s

def logical_xor(str1, str2):
    """Bitwise XOR between two binary strings (ensures output is correct length)."""
    xr1 = int(str1, 2) ^ int(str2, 2)
    return bin(xr1).replace('0b','').zfill(len(str1))

def RC(str1):
    """Row/column decoder for S-box indexing."""
    if str1 == '01': return 1
    if str1 == '10': return 2
    if str1 == '11': return 3
    return 0

def dev5(key10, b):
    """Left-shift (LS) for both halves and apply P8 to generate subkey."""
    key5L = key10[0:5]
    key5R = key10[5:]
    # Perform circular left shift
    key5L = key5L[b:] + key5L[0:b]
    key5R = key5R[b:] + key5R[0:b]
    key10 = key5L + key5R
    # Apply P8 permutation to generate subkey
    k = ip(p8, key10)
    return k, key10

def key_generate(key10):
    """Generate two subkeys (K1, K2) from 10-bit key."""
    key10 = ip(p10, key10)      # Apply P10
    k1, key10 = dev5(key10, 1)   # First LS-1 → K1
    k2, key10 = dev5(key10, 2)   # Second LS-2 → K2
    return k1, k2

def rund(P, k):
    """One round of Feistel structure for S-DES."""
    L = P[0:4]             # Left 4 bits
    R = P[4:]              # Right 4 bits
    ep = ip(ep8, R)         # Expand and permute right half (8 bits)
    ep = logical_xor(ep, k) # XOR with subkey (8 bits)

    # S-box lookups
    s0r = RC(ep[0] + ep[3])
    s0c = RC(ep[1] + ep[2])
    s1r = RC(ep[4] + ep[7])
    s1c = RC(ep[5] + ep[6])

    # Get S-box outputs and apply P4 (4 bits)
    P4 = s0[s0r][s0c] + s1[s1r][s1c]
    P4 = ip(pi4, P4)

    # XOR with left half (4 bits)
    P4 = logical_xor(P4, L)

    # Concatenate (R becomes new left)
    P = R + P4
    return P

def SDES_block(key10, P, mode):
    """
    S-DES main encryption/decryption function for a single 8-bit block.
    P must be an 8-bit binary string.
    mode must be 'en' (encrypt) or 'de' (decrypt).
    """
    k1, k2 = key_generate(key10) # Generate subkeys
    if mode == 'de': k2, k1 = k1, k2  # Swap keys for decryption
    
    P = ip(ip8, P)               # Initial permutation (IP)
    P = rund(P, k1)              # Round 1
    P = rund(P, k2)              # Round 2
    P = P[4:] + P[0:4]            # Switch halves
    P = ip(ip_1, P)              # Apply inverse IP
    return P

# --- S-DES Padding Utilities ---

def pad_sdes(data_bits):
    """Pads binary string data for S-DES using PKCS#7-like scheme for 8-bit blocks."""
    padding_len = BLOCK_SIZE_SDES - (len(data_bits) % BLOCK_SIZE_SDES)
    if padding_len == BLOCK_SIZE_SDES:
        padding_len = BLOCK_SIZE_SDES
    # The padding value is the length of the padding, represented in binary
    padding_byte = decimalToBinary(padding_len, BLOCK_SIZE_SDES)
    return data_bits + padding_byte * padding_len

def unpad_sdes(padded_data_bits):
    """Unpads binary string data for S-DES."""
    # Read the last 8 bits to get the padding value
    last_block = padded_data_bits[-BLOCK_SIZE_SDES:]
    padding_len = int(last_block, 2)
    # Remove the last 'padding_len' bytes worth of bits
    # The length of the padding in bits is padding_len * BLOCK_SIZE_SDES
    total_padding_bits = padding_len * BLOCK_SIZE_SDES
    return padded_data_bits[:-total_padding_bits]


# ====================================================================
# S-DES MODE IMPLEMENTATIONS (CBC, OFB, CTR)
# ====================================================================

def cbc_encrypt_sdes(plaintext, key10, iv_bin):
    """Cipher Block Chaining (CBC) Encryption using S-DES."""
    data_bits = ''.join(decimalToBinary(ord(char), 8) for char in plaintext)
    padded_data = pad_sdes(data_bits)
    
    ciphertext = ""
    previous_ciphertext = iv_bin # IV is the initial vector for the first block
    
    for i in range(0, len(padded_data), BLOCK_SIZE_SDES):
        block = padded_data[i : i + BLOCK_SIZE_SDES]
        # CBC: XOR plaintext block with previous ciphertext block (or IV)
        xored_block = logical_xor(block, previous_ciphertext)
        # Encrypt the result using the S-DES core function
        cipher_block = SDES_block(key10, xored_block, 'en')
        
        ciphertext += cipher_block
        previous_ciphertext = cipher_block # Update for the next block
        
    return iv_bin + ciphertext # Prepend the IV (for decryption)

def cbc_decrypt_sdes(ciphertext_with_iv, key10):
    """Cipher Block Chaining (CBC) Decryption using S-DES."""
    iv_bin = ciphertext_with_iv[:BLOCK_SIZE_SDES]
    ciphertext = ciphertext_with_iv[BLOCK_SIZE_SDES:]

    plaintext_bits = ""
    previous_ciphertext = iv_bin

    for i in range(0, len(ciphertext), BLOCK_SIZE_SDES):
        cipher_block = ciphertext[i : i + BLOCK_SIZE_SDES]
        
        # Decrypt the block
        decrypted_block = SDES_block(key10, cipher_block, 'de')
        
        # CBC: XOR the decrypted result with previous ciphertext block (or IV)
        plain_block = logical_xor(decrypted_block, previous_ciphertext)
        
        plaintext_bits += plain_block
        previous_ciphertext = cipher_block # Update for the next block

    # Unpad and convert back to string
    unpadded_bits = unpad_sdes(plaintext_bits)
    plaintext = ''.join(chr(int(unpadded_bits[i:i+8], 2)) 
                        for i in range(0, len(unpadded_bits), 8))
    return plaintext

def ofb_mode_sdes(data, key10, iv_bin, mode):
    """Output Feedback (OFB) Mode (symmetric en/de) using S-DES."""
    # Convert data to a binary string
    data_bits = ''.join(decimalToBinary(ord(char), 8) for char in data)
    
    # We only pad the plaintext for encryption, the ciphertext length is fixed.
    padded_data = pad_sdes(data_bits) if mode == 'en' else data_bits

    result_bits = ""
    output_block = iv_bin # IV is the initial output register

    for i in range(0, len(padded_data), BLOCK_SIZE_SDES):
        block = padded_data[i : i + BLOCK_SIZE_SDES]
        
        # 1. Generate the stream key by encrypting the previous output block
        stream_key = SDES_block(key10, output_block, 'en')
        
        # 2. XOR the plaintext/ciphertext block with the stream key
        result_block = logical_xor(block, stream_key)
        
        # 3. The new output block is the newly generated stream key
        output_block = stream_key
        
        result_bits += result_block

    if mode == 'en':
        return iv_bin + result_bits # Prepend the IV
    else:
        # Unpad and convert back to string
        unpadded_bits = unpad_sdes(result_bits)
        plaintext = ''.join(chr(int(unpadded_bits[i:i+8], 2)) 
                            for i in range(0, len(unpadded_bits), 8))
        return plaintext

def ctr_mode_sdes(data, key10, nonce_bin, mode):
    """Counter (CTR) Mode (symmetric en/de) using S-DES."""
    # Convert data to a binary string
    data_bits = ''.join(decimalToBinary(ord(char), 8) for char in data)
    
    # We only pad the plaintext for encryption
    padded_data = pad_sdes(data_bits) if mode == 'en' else data_bits

    result_bits = ""
    counter = int(nonce_bin, 2) # Initialize the counter from the nonce/IV

    for i in range(0, len(padded_data), BLOCK_SIZE_SDES):
        block = padded_data[i : i + BLOCK_SIZE_SDES]
        
        # 1. Encrypt the current counter value to generate the stream key
        counter_bin = decimalToBinary(counter, BLOCK_SIZE_SDES)
        stream_key = SDES_block(key10, counter_bin, 'en')
        
        # 2. XOR the plaintext/ciphertext block with the stream key
        result_block = logical_xor(block, stream_key)
        
        # 3. Increment the counter
        counter += 1
        
        result_bits += result_block

    if mode == 'en':
        # Prepend the initial nonce/IV
        return nonce_bin + result_bits 
    else:
        # Unpad and convert back to string
        unpadded_bits = unpad_sdes(result_bits)
        plaintext = ''.join(chr(int(unpadded_bits[i:i+8], 2)) 
                            for i in range(0, len(unpadded_bits), 8))
        return plaintext

# ====================================================================
# AES MODE IMPLEMENTATIONS (using PyCryptodome)
# ====================================================================

def aes_encrypt(data, key_bytes, mode_type, iv_bytes=None, nonce_bytes=None):
    """A generic function to handle AES encryption for CBC, OFB, and CTR."""
    data_bytes = data.encode('utf-8')

    if mode_type == 'CBC':
        cipher = AES.new(key_bytes, AES.MODE_CBC, iv_bytes)
        # CBC requires padding
        ciphertext = cipher.encrypt(pad(data_bytes, BLOCK_SIZE_AES))
        return iv_bytes + ciphertext
    
    elif mode_type == 'OFB':
        cipher = AES.new(key_bytes, AES.MODE_OFB, iv_bytes)
        # OFB is stream-like, no padding needed for PyCryptodome
        ciphertext = cipher.encrypt(data_bytes)
        return iv_bytes + ciphertext
    
    elif mode_type == 'CTR':
        # CTR uses a nonce, not a traditional IV.
        ctr = Counter.new(128, initial_value=int.from_bytes(nonce_bytes, 'big'))
        cipher = AES.new(key_bytes, AES.MODE_CTR, counter=ctr)
        # CTR is stream-like, no padding needed
        ciphertext = cipher.encrypt(data_bytes)
        return nonce_bytes + ciphertext
    
    raise ValueError("Invalid AES mode type")

def aes_decrypt(ciphertext_with_nonce, key_bytes, mode_type):
    """A generic function to handle AES decryption for CBC, OFB, and CTR."""
    
    # Separate IV/Nonce from ciphertext
    iv_or_nonce_bytes = ciphertext_with_nonce[:BLOCK_SIZE_AES]
    ciphertext = ciphertext_with_nonce[BLOCK_SIZE_AES:]

    if mode_type == 'CBC':
        cipher = AES.new(key_bytes, AES.MODE_CBC, iv_or_nonce_bytes)
        decrypted_padded = cipher.decrypt(ciphertext)
        # Must unpad after decryption
        return unpad(decrypted_padded, BLOCK_SIZE_AES).decode('utf-8')
    
    elif mode_type == 'OFB':
        cipher = AES.new(key_bytes, AES.MODE_OFB, iv_or_nonce_bytes)
        decrypted = cipher.decrypt(ciphertext)
        return decrypted.decode('utf-8') 
    
    elif mode_type == 'CTR':
        ctr = Counter.new(128, initial_value=int.from_bytes(iv_or_nonce_bytes, 'big'))
        cipher = AES.new(key_bytes, AES.MODE_CTR, counter=ctr)
        decrypted = cipher.decrypt(ciphertext)
        return decrypted.decode('utf-8') 
    
    raise ValueError("Invalid AES mode type")

# ====================================================================
# Main Execution Block for Testing
# ====================================================================

def main():
    print("=========================================")
    print(" CRYPTOGRAPHY LAB: MODES OF OPERATION ")
    print("=========================================")
    
    test_plaintext = "Hello, Cryptography Lab!"
    print(f"\nOriginal Text: '{test_plaintext}'")
    
    # --- S-DES PARAMETERS ---
    sdes_key = "1010000001" # 10-bit S-DES key
    sdes_iv = "01010101"     # 8-bit S-DES IV/Nonce
    
    print("\n--- S-DES (8-bit block) Tests ---")

    # CBC Test
    cipher_cbc = cbc_encrypt_sdes(test_plaintext, sdes_key, sdes_iv)
    plain_cbc = cbc_decrypt_sdes(cipher_cbc, sdes_key)
    print(f"CBC Decrypted (S-DES): '{plain_cbc}'")
    assert test_plaintext == plain_cbc, "S-DES CBC Failed!"

    # OFB Test
    cipher_ofb = ofb_mode_sdes(test_plaintext, sdes_key, sdes_iv, 'en')
    # Note: OFB decryption function expects only the actual ciphertext, plus the IV
    plain_ofb = ofb_mode_sdes(cipher_ofb[BLOCK_SIZE_SDES:], sdes_key, sdes_iv, 'de')
    print(f"OFB Decrypted (S-DES): '{plain_ofb}'")
    assert test_plaintext == plain_ofb, "S-DES OFB Failed!"

    # CTR Test
    cipher_ctr = ctr_mode_sdes(test_plaintext, sdes_key, sdes_iv, 'en')
    # Note: CTR decryption function expects only the actual ciphertext, plus the Nonce
    plain_ctr = ctr_mode_sdes(cipher_ctr[BLOCK_SIZE_SDES:], sdes_key, sdes_iv, 'de')
    print(f"CTR Decrypted (S-DES): '{plain_ctr}'")
    assert test_plaintext == plain_ctr, "S-DES CTR Failed!"


    # --- AES PARAMETERS ---
    # AES keys must be 16, 24, or 32 bytes (128, 192, or 256 bits).
    aes_key = os.urandom(16) # 128-bit key
    aes_iv = os.urandom(16)  # 128-bit IV
    aes_nonce = os.urandom(16) # 128-bit Nonce

    print("\n--- AES (128-bit block) Tests ---")

    # CBC Test (AES)
    cipher_aes_cbc = aes_encrypt(test_plaintext, aes_key, 'CBC', iv_bytes=aes_iv)
    plain_aes_cbc = aes_decrypt(cipher_aes_cbc, aes_key, 'CBC')
    print(f"CBC Decrypted (AES): '{plain_aes_cbc}'")
    assert test_plaintext == plain_aes_cbc, "AES CBC Failed!"

    # OFB Test (AES)
    cipher_aes_ofb = aes_encrypt(test_plaintext, aes_key, 'OFB', iv_bytes=aes_iv)
    plain_aes_ofb = aes_decrypt(cipher_aes_ofb, aes_key, 'OFB')
    print(f"OFB Decrypted (AES): '{plain_aes_ofb}'")
    assert test_plaintext == plain_aes_ofb, "AES OFB Failed!"

    # CTR Test (AES)
    cipher_aes_ctr = aes_encrypt(test_plaintext, aes_key, 'CTR', nonce_bytes=aes_nonce)
    plain_aes_ctr = aes_decrypt(cipher_aes_ctr, aes_key, 'CTR')
    print(f"CTR Decrypted (AES): '{plain_aes_ctr}'")
    assert test_plaintext == plain_aes_ctr, "AES CTR Failed!"

    print("\nAll encryption mode tests passed successfully!")

if __name__ == "__main__":
    main()
