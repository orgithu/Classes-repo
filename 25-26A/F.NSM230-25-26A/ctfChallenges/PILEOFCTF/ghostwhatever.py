data = open("/home/orgdg/Documents/Classes-repo/F.NSM230-25-26A/ctfChallenges/PILEOFCTF/secret.bin", "rb").read()
key = [0x13, 0x37, 0x56, 0x78]
print(data)
decoded = bytes([ data[i] ^ key[i % 4] for i in range(len(data)) ])
print(decoded)
