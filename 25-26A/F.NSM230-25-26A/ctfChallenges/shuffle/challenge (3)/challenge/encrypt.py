import random
import string

KEY = list(string.ascii_uppercase)
random.shuffle(KEY)

with open("MESSAGE", 'r') as f:
  MSG = f.read()

ENCRYPTED = []
for i in range(len(MSG)):
  current = MSG[i]

  for _ in range(i):
    current = KEY[ord(current) - ord('A')]

  ENCRYPTED.append(current)

with open("ENCRYPTED", "w") as f:
  f.write("".join(ENCRYPTED))