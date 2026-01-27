# -------------------------
# S-DES tables and functions
# -------------------------

# Permutation tables
p10=[3, 5, 2, 7, 4, 10, 1, 9, 8, 6]         # P10 permutation
p8=[6, 3, 7, 4, 8, 5, 10, 9]                # P8 permutation
ip8=[2, 6, 3, 1, 4, 8, 5, 7]                # Initial permutation (IP)
ep8=[4,1,2,3,2,3,4,1]                       # Expansion permutation (E/P)
pi4=[2,4,3,1]                               # P4 permutation
ip_1=[4,1,3,5,7,2,8,6]                      # Inverse IP (IP^-1)

# S-boxes
s0=[['01','00','11','10'],
    ['11','10','01','00'],
    ['00','10','01','11'],
    ['11','01','11','10']]

s1=[['00','01','10','11'],
    ['10','00','01','11'],
    ['11','00','01','00'],
    ['10','01','00','11']]

# -------------------------
# Helper Functions
# -------------------------
def binToHex(binary_input):
    decimal_value = int(binary_input, 2)
    hex_value = hex(decimal_value)
    return hex_value
# General permutation function: rearranges bits according to table
def ip(key,st):
    s=''
    for i in key:
        s += st[i-1]
    return s

# Bitwise XOR between two binary strings
def logical_xor(str1, str2):
    xr1=int(str1,2)^int(str2,2)
    return bin(xr1).replace('0b','').zfill(len(str1))

# Row/column decoder for S-box indexing
def RC(str1):
    if str1=='01': return 1
    if str1=='10': return 2
    if str1=='11': return 3
    return 0

# Left-shift (LS) by b for both halves of 10-bit key
def dev5(key10,b):
    key5L=key10[0:5]
    key5R=key10[5:]
    # Perform circular left shift
    key5L=key5L[b:]+key5L[0:b]
    key5R=key5R[b:]+key5R[0:b]
    key10=key5L+key5R
    # Apply P8 permutation to generate subkey
    k1=ip(p8,key10)
    return k1, key10

# Generate two subkeys (K1, K2) from 10-bit key
def key(key10):
    key10=ip(p10,key10)      # Apply P10
    k1,key10=dev5(key10,1)   # First LS-1 → K1
    k2,key10=dev5(key10,2)   # Second LS-2 → K2
    return k1,k2

# One round of Feistel structure
def rund(P,k):
    L=P[0:4]             # Left 4 bits
    R=P[4:]              # Right 4 bits
    ep=ip(ep8,R)         # Expand and permute right half
    ep=logical_xor(ep,k) # XOR with subkey

    # S-box lookups
    s0r=RC(ep[0]+ep[3])
    s0c=RC(ep[1]+ep[2])
    s1r=RC(ep[4]+ep[7])
    s1c=RC(ep[5]+ep[6])

    # Get S-box outputs and apply P4
    P4=s0[s0r][s0c]+s1[s1r][s1c]
    P4=ip(pi4,P4)

    # XOR with left half
    P4=logical_xor(P4,L)

    # Concatenate (R becomes new left)
    P=R+P4
    return P

# S-DES main encryption/decryption function
def SDES(key10,P,st):
    k1,k2=key(key10)          # Generate subkeys
    if st=='de': k2,k1=k1,k2  # Swap keys for decryption
    P=ip(ip8,P)               # Initial permutation (IP)
    P=rund(P,k1)              # Round 1
    P=rund(P,k2)              # Round 2
    P=P[4:]+P[0:4]            # Switch halves
    P=ip(ip_1,P)              # Apply inverse IP
    return P

# Convert decimal to binary (padded to b bits)
def decimalToBinary(n,b):
    bnr=bin(n).replace("0b", "")
    return bnr.rjust(b,'0')

# Encrypt string with S-DES
def encrypt_text(text,key10):
    hexArr=[]
    for i in text:
        p = decimalToBinary(ord(i),8)             # Char → 8-bit binary
        encrypted_bin = SDES(key10,p,'en')        # Encrypt → binary string
        encrypted_int = int(encrypted_bin,2)      # Binary → integer
        hexArr.append(format(encrypted_int,"02x"))# Format as 2-digit hex
    return ''.join(hexArr)   # continuous hex string (no spaces)

# Decrypt continuous hex string back to plaintext
def decrypt_text(hex_text,key10):
    plainArr=[]
    # Process every 2 hex chars (1 byte)
    for i in range(0, len(hex_text), 2):
        byte_hex = hex_text[i:i+2]                      # Take 2 hex digits
        encrypted_bin = decimalToBinary(int(byte_hex,16),8) # Hex → 8-bit bin
        decrypted_bin = SDES(key10,encrypted_bin,'de')  # Decrypt
        plainArr.append(chr(int(decrypted_bin,2)))      # Binary → ASCII char
    return ''.join(plainArr)

# -------------------------
# File Handling
# -------------------------

# File paths
plaintext_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/plain.txt"
cipher_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/sdes_cipher.txt"
decrypted_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/sdes_decrypted.txt"
key_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/sdes_key.txt"

# Example fixed 10-bit key (can be randomized)
key10 = '1010000010'
k1,k2=key(key10)
# Read plaintext
with open(plaintext_file,"r") as f:
    plaintext = f.read()

# Encrypt plaintext → ciphertext
ciphertext = encrypt_text(plaintext,key10)
with open(cipher_file,"w",) as f:
    f.write(ciphertext)

# Decrypt ciphertext → original plaintext
with open(cipher_file,"r",) as f:
    encrypted_data = f.read()
decrypted_text = decrypt_text(encrypted_data,key10)
with open(decrypted_file,"w") as f:
    f.write(decrypted_text)

# Save the used key for reference
with open(key_file,"w") as f:
    f.write(binToHex(key10).replace("0x",''))
    f.write(binToHex(k1).replace("0x",''))
    f.write(binToHex(k2).replace("0x",''))

print("En\De done")
