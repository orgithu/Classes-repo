def addRoundKey(a, b):
    afterRoundKey = []
    for i in range(16):
        afterRoundKey.append(hex(a[i]^b[i])[2:])
    return afterRoundKey
key = "Thats my Kung Fu"
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
            afterRoundPlaintext = addRoundKey(key16, plaintext16)
            print("plaintext after addRoundKey: ", afterRoundPlaintext)
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
            # ensure plaintext is 16 bytes for matrix display
            p = plaintext
            if len(p) < 16:
                p = p.ljust(16, '\x00')
            else:
                p = p[:16]
            print("Key in 4x4 matrix: ")
            for i in range(4):
                for j in range(4):
                    print(hex(ord(key[j*4+i]))[2:], end=' ')
                print()
            print("plaintext in 4x4 matrix: ")
            for i in range(4):
                for j in range(4):
                    print(hex(ord(p[j*4+i]))[2:], end=' ')
                print()
        else:
            raise Exception
    except KeyboardInterrupt:
        break
    except:
        print("ALDAA")