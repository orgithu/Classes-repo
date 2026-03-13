import os
import socket
import struct
import hashlib

hostid = os.popen("hostid").read().strip()
hostname = socket.gethostname()

ioukey = int(hostid, 16)
for x in hostname:
    ioukey = ioukey + ord(x)

iouPad1 = b'\x4B\x58\x21\x81\x56\x7B\x0D\xF3\x21\x43\x9B\x7E\xAC\x1D\xE6\x8A'
iouPad2 = b'\x80' + 39 * b'\x00'
md5sum = hashlib.md5(iouPad1 + iouPad2 + struct.pack('!i', ioukey) + iouPad1).hexdigest()

license_content = f"[license]\n{hostname} = {md5sum[:16]};\n"

try:
    with open("iourc.txt", "w") as f:
        f.write(license_content)
    print(f"Done")
except Exception as e:
    print(f"Error writing to file: {e}")
