import requests

url = "http://139.162.5.230:10176/"  # Replace with the actual URL

s = requests.Session()
response = s.get(url)

if 'X-Request-Timestamp' not in response.headers:
    print("No timestamp header found. Exiting.")
    exit(1)

timestamp_str = response.headers['X-Request-Timestamp']
timestamp = float(timestamp_str)

seconds = int(timestamp)
microseconds = int(round((timestamp - seconds) * 1000000))

hex_sec = format(seconds, '08x')
hex_usec = format(microseconds, '05x')
uniqid = hex_sec + hex_usec
token = uniqid[:11]

response2 = s.get(url, params={'token': token})
print(response2.text)