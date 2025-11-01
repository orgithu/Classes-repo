def addRoundKey(a, b):
    afterRoundKey = []
    for i in range(16):
        afterRoundKey.append(a[i] ^ b[i])
    return afterRoundKey
def mPrint(st):
    m = []
    m.append([st[0], st[4], st[8], st[12]])
    m.append([st[1], st[5], st[9], st[13]])
    m.append([st[2], st[6], st[10], st[14]])
    m.append([st[3], st[7], st[11], st[15]])
    for i in m:
        for j in i:
            print(hex(j).replace('0x', '').upper(), end='\t')
        print()
key = "Thats my Kung Fu"
key = key.ljust(16, '\x00')  #Pad
key16=[]
plaintext16=[]
plaintext = input("enter plaintext: ")
while True:
    try:
        choice = input("1. addRoundKey\n2. textToHex number\n3. display in  4x4 matrix\nq. quit\nChoose: ")
        if choice.lower() == 'q':
            break
        # reset buffers
        key16.clear()
        plaintext16.clear()
        if choice == "1":
            for i in key:
                key16.append(ord(i))
            #pad
            p = plaintext
            if len(p) < 16:
                p = p.ljust(16, '\x00')
            else:
                p = p[:16]
            for i in p:
                plaintext16.append(ord(i))
            print("ptext: ", plaintext16)
            print("key16: ", key16)
            afterRoundPlaintext = addRoundKey(key16, plaintext16)
            print("plaintext after addRoundKey:")
            mPrint(afterRoundPlaintext)
        elif choice == "2":
            count = 1
            print("Key in hex: ")
            for i in key:
                print(count, i, ord(i), hex(ord(i))[2:], end='\n')
                count += 1
            count = 1
            print("\nPlaintext in hex: ")
            for i in plaintext:
                print(count, i, ord(i), hex(ord(i))[2:], end='\n')
                count += 1
            print()
        elif choice == "3":
            #plaintext must be 16 bytes.
            p = plaintext
            if len(p) < 16:
                p = p.ljust(16, '\x00')
            else:
                p = p[:16]
            for i in key:
                key16.append(ord(i))
            for i in p:
                plaintext16.append(ord(i))
            print("Key in 4x4 matrix: ")
            mPrint(key16)
            print("plaintext in 4x4 matrix: ")
            mPrint(plaintext16)
        else:
            raise Exception
    except KeyboardInterrupt:
        break
    except:
        print("ALDAA")