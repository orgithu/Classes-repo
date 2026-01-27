"""
P(n) = (p-1)(q-1)
p,q prime numbers
n = p*q
e, gcd(P(n),e) = 1, 1<e<P(n)
e*d mod P(n) = 1
egcd table is used.
e*e^-1
write egcd and import to use 
d = pow(e,-1,P(n)) --> e^-1 mod P(n)
p and q must be big as possible. in practice, 7-11 digit prime numbers.
298x for example
"""
import string
import time
st=list(string.ascii_lowercase+string.ascii_uppercase+"0123456789 ,;.?\n")
def egcd(a, b):
    if a == 0:
        return b, 0, 1
    g, x1, y1 = egcd(b % a, a)
    x = y1 - (b // a) * x1
    y = x1
    return g, x, y

def inv(a, m):
    g, x, _ = egcd(a, m)
    if g != 1:
        raise ValueError("no inverse")
    return x % m

def charToDecimal(char):
    return st.index(char)

def convertToChar(P): #len=4
    if len(str(P)) == 4:
        try:
            l = int(str(P)[2:])
            r = int(str(P)[:2])
            result = st[r]+st[l]
            return str(result)
        except:
            pass
    elif (len(str(P)) > 4):
        raise ValueError("len is more than 4")
    else:
        a = '0' + str(P)
        return convertToChar(a)

def mypow(a, b, n):
    f = 1
    a %= n
    b_bin = bin(b)[2:]
    for i in b_bin:
        f = (f * f) % n
        if i == '1':
            f = (f * a) % n
    return f

def rsa(x, k):
    return mypow(x, k[0], k[1])

def keys(p=int, q=int):
    e = 11
    n = p * q
    pn = (p - 1) * (q - 1)
    d = inv(e, pn)
    return [e, n], [d, n]

def encrypt(pt=str,k=list):
    if len(pt) % 2 != 0:
        pt = pt + ' '
    i = 0
    strC = []
    while i < len(pt):
        l = charToDecimal(pt[i])
        r = charToDecimal(pt[i+1])
        P = int(str(l)+str(r))
        C = rsa(P,k)
        strC.append(str(hex(C).replace("0x",'')))
        i += 2
    return listToStr(strC)

def decrypt(cipher,k):
    ct = strToList(cipher)
    strP = []
    for c in ct:
        P = rsa(int(c), k)
        char = convertToChar(P)
        if char != None:
            strP.append(char)
    print(strP)
    result = ''.join(strP)
    if result.startswith(' '):
        result = result[1:]
    return result

def listToStr(l=list):
    strList = ''
    temp = 0
    for i in l:
        if temp == len(l) - 1:
            strList += str(i)
        else:
            strList += str(i) + '/'
        temp+=1
    return strList

def strToList(st=str):
    ls = []
    temp = []
    temp = st.split('/')
    for i in temp:
        ls.append(int(i,16))
    return ls

start = time.time_ns()
p = 1313131313131313131313131 #25 digit
q = 1304313049130631309313099
PU,PR = keys(p, q)
#plaintext = "a0"
"""ciphertext = encrypt(plaintext,PU)
dePt = decrypt(ciphertext,PR)
print("ciphertext:",ciphertext)
print("plaintext",''.join(dePt))"""



with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week12/plain.txt","r") as f:
    plaintext = f.read()
ciphertext = encrypt(plaintext,PU)

with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week12/oldcipher.txt","w") as f:
    f.write(ciphertext)

with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week12/oldcipher.txt","r") as f:
    ciphertext1 = f.read()
decrypted = decrypt(ciphertext1,PR)

with open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/week12/olddecrypted.txt","w") as f:
    f.write(decrypted)
end = time.time_ns()
dur = end - start
print("Done\nduration:", dur/10**9,"second")