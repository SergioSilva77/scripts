function isPrime(num) {
    for(let i = 2, s = Math.sqrt(num); i <= s; i++) {
        if(num % i === 0) return false;
    }
    return num > 1;
}

function getPrimes(start, end){
    var primes = []
    for (let i = start; i<=end;i++){
        if (isPrime(i)){
            primes.push(i)
        }
    }
    return primes
}
console.log(getPrimes(1, 1000))