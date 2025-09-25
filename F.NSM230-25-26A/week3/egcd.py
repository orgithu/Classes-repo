# Python program to demonstrate working of extended 
# Euclidean Algorithm 
    
# function for extended Euclidean Algorithm 
def gcdExtended(a, b): 
    # Base Case 
    if a == 0 : 
        return b,0,1
            
    gcd,x1,y1 = gcdExtended(b%a, a) 
    
    # Update x and y using results of recursive 
    # call 
    x = y1 - (b//a) * x1 
    y = x1 
    
    return gcd,x,y

# Driver code 
a, b = 1759,550
g, x, y = gcdExtended(a, b) 
print("gcd(", a , "," , b, ") = ", g, "x: ",x%b, "y: ",y)
k,b = 3,26
g,x,y = gcdExtended(k,b)
print("gcd(", k , "," , b, ") = ", g, "x: ",x%b, "y: ",y)