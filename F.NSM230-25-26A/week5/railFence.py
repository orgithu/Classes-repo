def encryptRailFence(text, key):
    # Return the text unchanged if key is 1 or less (no encryption needed)
    if key <= 1:
        return text
    # Initialize a list for each rail
    rails = []
    for i in range(key):
        rails.append('')
    row = 0
    dir_down = False
    # Traverse the text and place each character in the correct rail
    for ch in text:
        rails[row] = rails[row] + ch
        # Change direction at the top or bottom rail
        if row == 0 or row == key - 1:
            dir_down = not dir_down
        # Move up or down the rails
        if dir_down:
            row += 1
        else:
            row -= 1
    # Concatenate all rails to get the encrypted result
    result = ""
    for r in rails:
        result += r
    return result

def decryptRailFence(cipher, key):
    if key <= 1:
        return cipher
    # Count how many characters go into each rail
    counts = []
    for i in range(key):
        counts.append(0)
    row = 0
    dir_down = False
    for i in range(len(cipher)):
        counts[row] = counts[row] + 1
        if row == 0 or row == key - 1:
            dir_down = not dir_down
        if dir_down:
            row += 1
        else:
            row -= 1
    # Split the cipher text into rails based on counts
    rails = []
    idx = 0
    for c in counts:
        part = cipher[idx:idx + c]
        rails.append(part)
        idx = idx + c
    # Prepare pointers for each rail
    ptrs = []
    for i in range(key):
        ptrs.append(0)
    result_chars = []
    row = 0
    dir_down = False
    # Reconstruct the original text by traversing rails in zigzag order
    for i in range(len(cipher)):
        if ptrs[row] < len(rails[row]):
            result_chars.append(rails[row][ptrs[row]])
            ptrs[row] = ptrs[row] + 1
        if row == 0 or row == key - 1:
            dir_down = not dir_down
        if dir_down:
            row += 1
        else:
            row -= 1
    return "".join(result_chars)

with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/plain.txt", "r") as f:
    plaintext = f.read()
key = 3
ciphertext = encryptRailFence(plaintext, key)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/railFence_cipher.txt", "w") as f:
    f.write(ciphertext)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/railFence_cipher.txt", "r") as f:
    encrypted_data = f.read()
decrypted_text = decryptRailFence(encrypted_data, key)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/railFence_decrypted.txt", "w") as f:
    f.write(decrypted_text)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/week5/railFence_key.txt", "w") as f:
    f.write(str(key))
