def nn(a):
    nl=['ajs','bkt','clu','dmv','enw','fox','gpy','hqz','ir']
    a = a.lower();ind=0
    for i in range(9):
        if a.isalpha():
            if a in nl[i]:
                ind=i;
    return ind+1
#nernees alp's busad character,hooson zaig arilgaad list bolgoj bna
myname = " Munkh - Orgil "
print(myname)
raw_x = list(myname)
x = []
for i in range(len(myname)):
    if raw_x[i] != ' ' and raw_x[i].isalpha():
        x.append(raw_x[i])
print(x)
#nerendeh hargalzah usgend toog onooj bna
raw_result = []
for i in range(len(x)):
    raw_result.append(nn(x[i]))
result = sum(raw_result)
print(raw_result)
print("Result is: ", result)
#bugdiig nemeed 2 orontoi too bol: int->string->strList->intList->sum=int loop to start...
while result > 9:
    str_r = list(str(result))
    print(str_r)
    result = sum(list(map(int,str_r)))
    print(result)
print("Numerology is: ", result)