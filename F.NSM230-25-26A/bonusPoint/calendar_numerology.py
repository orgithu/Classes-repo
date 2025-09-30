#y=input("year: ")
#m=input("month: ")
#d=input("day: ")
y='2006'
m='07'
d='10'
raw_result=list(y+m+d)
print("Raw result:", raw_result)
result = sum(list(map(int,raw_result)))
print("Result:",result)
while result > 9:
    str_r = list(str(result))
    print(str_r)
    result = sum(list(map(int,str_r)))
    print(result)
print("Amidraliin too:",result)