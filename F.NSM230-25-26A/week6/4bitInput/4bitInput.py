fourBitMap = {0:14, 1:4, 2:13, 3:1, 4:2, 5:15, 6:11, 7:8, 8:3, 9:10, 10:6, 11:12, 12:5, 13:9, 14:0, 15:7}
try:
    choice = input("1. 4bitTo4bit\n2. plaintextToBinary\n")
    if choice == "1":
        fourBitInput = input("enter four bit, ex: 0000\n")
        str(fourBitInput)
        fourBitDecimal = int(fourBitInput, 2)
        mappedDecimal = fourBitMap[fourBitDecimal]
        mappedBinary = bin(mappedDecimal).replace("0b", "")
        print(mappedBinary)
    elif choice == "2":
        plaintext = input("enter plaintext:\n")
        print("|char|ord| 8bitOfOrd |len| left | right|ld |rd |lm |rm |\n--------------------------------------------------------")
        for char in plaintext:
            charOrd = ord(char)
            charBin = bin(charOrd).replace("0b","")
            charBin = '0' * (8 - len(charBin)) + charBin
            leftBin = charBin[0:4]
            rightBin = charBin[4:]
            leftDecimal = int(leftBin, 2)
            rightDecimal = int(rightBin, 2)
            leftMap = fourBitMap[leftDecimal]
            rightMap = fourBitMap[rightDecimal]
            leftMapBin =  bin(leftMap).replace("0b","").zfill(4)
            rightMapBin = bin(rightMap).replace("0b","").zfill(4)
            mapBin = leftMapBin + rightMapBin
            mappedDecimal = int(mapBin, 2)
            mappedChar = chr(mappedDecimal)
            print("|",char,"|",charOrd,"|",charBin,"|",len(charBin),"|",leftBin,"|",rightBin,"|",leftDecimal,"|",rightDecimal,"|",leftMap,"|",rightMap,"|", leftMapBin, rightMapBin, mapBin, mappedDecimal, mappedChar)
    else:
        raise Exception
except:
    print("ALDAA")