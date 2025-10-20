def extract_chars(text_path, key_path):
    # Read all lines of the text file
    with open(text_path, 'r', encoding='utf-8') as f:
        text_lines = f.readlines()

    result = []

    # Read the key pairs
    with open(key_path, 'r', encoding='utf-8') as f:
        for line_num, pair in enumerate(f, start=1):
            pair = pair.strip()
            if not pair:
                continue
            try:
                line_idx, char_idx = map(int, pair.split(','))
            except ValueError:
                print(f"Skipping malformed line {line_num}: {pair!r}")
                continue

            # Adjust to 0-based indices
            line_index = line_idx - 1
            char_index = char_idx - 1

            # Check bounds
            if 0 <= line_index < len(text_lines):
                text_line = text_lines[line_index].rstrip('\n')
                if 0 <= char_index < len(text_line):
                    result.append(text_line[char_index])
                else:
                    print(f"Warning: line {line_idx} shorter than {char_idx + 1} chars")
            else:
                print(f"Warning: no line {line_idx} in text file")

    # Combine extracted characters into a string
    secret = ''.join(result)
    print("Extracted text:", secret)
    return secret


if __name__ == "__main__":
    extract_chars('C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\F.NSM230-25-26A\\ctfChallenges\\Унш унш бас дахин унш\\readme.txt', 'C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\F.NSM230-25-26A\\ctfChallenges\\Унш унш бас дахин унш\\key.txt')
