def fermat_primality_test(n, k=5):
    import random
    if n <= 1:
        return False
    if n <= 3:
        return True
    for _ in range(k):
        a = random.randint(2, n - 2)
        if pow(a, n - 1, n) != 1:
            return False
    return True

# Teste com alguns números
print(fermat_primality_test(97))  # True
print(fermat_primality_test(100)) # False
