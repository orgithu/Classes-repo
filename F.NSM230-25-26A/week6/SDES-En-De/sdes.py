# -------------------------
# S-DES tables and functions
# -------------------------
p10=[3, 5, 2, 7, 4, 10, 1, 9, 8, 6]
p8=[6, 3, 7, 4, 8, 5, 10, 9]
ip8=[2, 6, 3, 1, 4, 8, 5, 7]
ep8=[4,1,2,3,2,3,4,1]
pi4=[2,4,3,1]
ip_1=[4,1,3,5,7,2,8,6]
s0=[['01','00','11','10'],
    ['11','10','01','00'],
    ['00','10','01','11'],
    ['11','01','11','10']]
s1=[['00','01','10','11'],
    ['10','00','01','11'],
    ['11','00','01','00'],
    ['10','01','00','11']]

def ip(key,st):
    s=''
    for i in key:
        s += st[i-1]
    return s

def logical_xor(str1, str2):
    xr1=int(str1,2)^int(str2,2)
    return bin(xr1).replace('0b','').zfill(len(str1))

def RC(str1):
    if str1=='01': return 1
    if str1=='10': return 2
    if str1=='11': return 3
    return 0

def dev5(key10,b):
    key5L=key10[0:5]
    key5R=key10[5:]
    key5L=key5L[b:]+key5L[0:b]
    key5R=key5R[b:]+key5R[0:b]
    key10=key5L+key5R
    k1=ip(p8,key10)
    return k1, key10

def key(key10):
    key10=ip(p10,key10)
    k1,key10=dev5(key10,1)
    k2,key10=dev5(key10,2)
    return k1,k2

def rund(P,k):
    L=P[0:4]
    R=P[4:]
    ep=ip(ep8,R)
    ep=logical_xor(ep,k)
    s0r=RC(ep[0]+ep[3])
    s0c=RC(ep[1]+ep[2])
    s1r=RC(ep[4]+ep[7])
    s1c=RC(ep[5]+ep[6])
    P4=s0[s0r][s0c]+s1[s1r][s1c]
    P4=ip(pi4,P4)
    P4=logical_xor(P4,L)
    P=R+P4
    return P

def SDES(key10,P,st):
    k1,k2=key(key10)
    if st=='de': k2,k1=k1,k2
    P=ip(ip8,P)
    P=rund(P,k1)
    P=rund(P,k2)
    P=P[4:]+P[0:4]
    P=ip(ip_1,P)
    return P

def decimalToBinary(n,b):
    bnr=bin(n).replace("0b", "")
    return bnr.rjust(b,'0')

def encrypt_text(text,key10):
    binArr=[]
    for i in text:
        p=decimalToBinary(ord(i),8)
        binArr.append(chr(int(SDES(key10,p,'en'),2)))
    return ''.join(binArr)

def decrypt_text(text,key10):
    binArr=[]
    for i in text:
        p=decimalToBinary(ord(i),8)
        binArr.append(chr(int(SDES(key10,p,'de'),2)))
    return ''.join(binArr)

# -------------------------
# File Handling
# -------------------------
plaintext_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/plain.txt"
cipher_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/sdes_cipher.txt"
decrypted_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/sdes_decrypted.txt"
key_file = "C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week6/SDES-En-De/sdes_key.txt"

# Example: fixed 10-bit key (you can generate randomly if you want)
key10 = '1010000010'

# Read plaintext
with open(plaintext_file,"r",encoding="utf-8") as f:
    plaintext = f.read()

# Encrypt
ciphertext = encrypt_text(plaintext,key10)
with open(cipher_file,"w",encoding="utf-8") as f:
    f.write(ciphertext)

# Decrypt
with open(cipher_file,"r",encoding="utf-8") as f:
    encrypted_data = f.read()
decrypted_text = decrypt_text(encrypted_data,key10)
with open(decrypted_file,"w",encoding="utf-8") as f:
    f.write(decrypted_text)

# Save the key
with open(key_file,"w",encoding="utf-8") as f:
    f.write(key10)

print("Encryption and decryption done. Files saved.")