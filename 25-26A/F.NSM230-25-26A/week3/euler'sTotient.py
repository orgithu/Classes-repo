# Python 3 program to calculate Euler's Totient Function using Euler's product formula
 
def euler_totient_product(n) :
 
    result = n   # Initialize result as n
      
  # Iterate through all prime factors of n
    p = 2
    while p * p<= n :
 
        # Check if p is a prime factor.
        if n % p == 0 :
 
            # If yes, then update n and result
            while n % p == 0 :
                n = n // p
            result = result * (1.0 - (1.0 / float(p)))
        p = p + 1
         
   # If n is prime
    if n > 1 :
        result -= result // n
    return int(result)
     
     
# Driver program  
for n in range(1, 11) :
    print("Euler's Totient for", n, ":", euler_totient_product(n))