#http://139.162.5.230:10325/id/02991510-8414-11f0-928c-9a429061a3a0 finalEndPoint
import re
import requests

base = "http://139.162.5.230:10325"
current = "/"

pattern = re.compile(r"/id/[0-9a-fA-F\-]+")

while True:
    url = base + current
    print("Visiting:", url)
    r = requests.get(url)
    m = pattern.search(r.text)
    if not m:
        print("No new /id/ found — stopping.")
        break
    current = m.group(0)
    print("Found next endpoint:", current)