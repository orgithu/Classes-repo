import re, base64
txt = open("C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\F.NSM230-25-26A\\ctfChallenges\\brokencartRevenge\\ssh.pem","r",encoding="utf-8").read()
body = "\n".join(line.strip() for line in txt.splitlines()
                 if line.strip() and not line.startswith("-----"))
# remove lines that are entirely slashes or >90% slashes and long
lines = []
for L in body.splitlines():
    s = L.strip()
    if all(ch=='/' for ch in s): continue
    if len(s)>50 and s.count('/')/len(s) > 0.9: continue
    lines.append(s)
clean = "".join(lines)
# reflow
reflow = "\n".join(re.findall('.{1,70}', clean))
out = "-----BEGIN OPENSSH PRIVATE KEY-----\n" + reflow + "\n-----END OPENSSH PRIVATE KEY-----\n"
open("C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\F.NSM230-25-26A\\ctfChallenges\\brokencartRevenge\\tempssh.pem","w").write(out)
print("Wrote tempssh.pem — now test with: ssh-keygen -y -f tempssh.pem")
