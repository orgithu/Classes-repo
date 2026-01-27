#y=input("year: ")
#m=input("month: ")
#d=input("day: ")
y='2006'
m='07'
d='10'
amidralToo = []
raw_result=list(y+m+d)
result = sum(list(map(int,raw_result)))
while result > 9:
    str_r = list(str(result))
    result = sum(list(map(int,str_r)))
print("Tursun on:", y, "\nsar:", m, "\nudur:", d)
print("Amidraliin too:",result)
with open("C:/Users/orgil/OneDrive/Documents/GitHub/Classes-repo/F.NSM230-25-26A/bonusPoint/amidral_too.txt", "r") as f:
    content = f.read()
    amidralToo = content.split("\n")
print("Tailbar: ", amidralToo[result-1])
