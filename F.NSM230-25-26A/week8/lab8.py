def addRoundKey(a, b):
    afterRoundKey = []
    for i in range(16):
        afterRoundKey.append(hex(a[i]^b[i])[2:])
    return afterRoundKey
key = "Thats my Kung Fu"
key16=[]
plaintext16=[]
try:
    plaintext = input("enter plaintext:")
    choice = input("1. addRoundKey\n2. textToHex number\n3. display in  4x4 matrix\n")
    if choice == "1":
        for i in key:
            key16.append(ord(i))
        for i in plaintext:
            plaintext16.append(ord(i))
        afterRoundPlaintext = addRoundKey(key16, plaintext16)
        print("plaintext after addRoundKey: ", afterRoundPlaintext)
    elif choice == "2":
        print("Key in hex: ")
        for i in key:
            print(i, ord(i), hex(ord(i))[2:], end=' ')
        print("\nPlaintext in hex: ")
        for i in plaintext:
            print(i, ord(i), hex(ord(i))[2:], end=' ')
        print()
    elif choice == "3":
        print("Key in 4x4 matrix: ")
        for i in plaintext:
            print(hex(ord(i))[2:], end=' ')
        for i in range(4):
            for j in range(4):
                print(hex(ord(key[j*4+i]))[2:], end=' ')
            print()
    else:
        raise Exception
except:
    print("ALDAA")