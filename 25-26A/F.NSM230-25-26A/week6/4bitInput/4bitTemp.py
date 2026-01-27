def decimalToBinary(n,b):
    bnr=bin(n).replace("0b", "")
    bnr=bnr.rjust(b, '0') #this reverses an array.
    return bnr

def binArray(text):
    binArr=[]
    binF=[]
    for i in text:
        p=decimalToBinary(ord(i),8)
        binArr.append(p)
        binF.append(p[0:4])
        binF.append(p[4:])
    return binF

plaintText="Hello Des encryption"
print(binArray(plaintText))
def feistel(plaintText):
    s16=""
    f_ed=[14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7]
    for bitn in binArray(plaintText):
        pt=int(bitn,2)
        en=f_ed[pt]
        s16+=hex(en)[2:]
    return s16
def feisteld(plaintText):
    s16=""
    f_ed=[14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7]
    for i in range(0,len(plaintText),2):
        pt1=int(plaintText[i],16)
        pt2=int(plaintText[i+1],16)
        de1=f_ed.index(pt1)
        de2=f_ed.index(pt2)
        tmdgt=de1*16+de2
        s16+=chr(tmdgt)
    return s16

en=feistel(plaintText)
print(en)
de=feisteld(en)
print(de)