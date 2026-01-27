temp = [619,627,629,626,613,626,608,605,615,628,533,633,639,534,627,633] #actual first half flag[i]^550 value
decoded = ''
for i in range(len(temp)):
    decoded += chr(temp[i] ^ 550)
print(decoded, len(temp))

"""import os

flag = os.environ.get('flag', 'MUSTCTF{fake_flag_for_testing}').encode()

def leak(i):
    return flag[i] ^ sum([c for c in b'MUSTCTF'])


while True:
    try:
        i = int(input('i = '))
        assert i < len(flag) // 2  # Sorry. Only first half is leaked
        print('Leak:', leak(i))
    except Exception:
        print('Unexpected error')
        break"""