### 1. `for` Loop

O loop `for` é usado quando você sabe quantas vezes deseja repetir um bloco de código. Ele é controlado por uma variável de contagem.

```jsx
for (int i = 0; i < 10; i++)
{
    Console.WriteLine(i);
}
```

### 2. `foreach` Loop

O loop `foreach` é usado para iterar sobre os elementos de uma coleção (como arrays ou listas).

```jsx
int[] numbers = { 1, 2, 3, 4, 5 };
foreach (int number in numbers)
{
    Console.WriteLine(number);
}
```

### 3. `while` Loop

O loop `while` continua a executar um bloco de código enquanto uma condição especificada for verdadeira.

```jsx
int i = 0;
while (i < 10)
{
    Console.WriteLine(i);
    i++;
}
```

### 4. `do-while` Loop

O loop `do-while` é semelhante ao `while`, mas garante que o bloco de código seja executado pelo menos uma vez.

```jsx
int i = 0;
do
{
    Console.WriteLine(i);
    i++;
} while (i < 10);
```

### 5. `Parallel.For` Loop

O loop `Parallel.For` é usado para executar iterações em paralelo, melhorando a performance em cenários com grandes volumes de dados.

```jsx
Parallel.For(0, 10, i =>
{
    Console.WriteLine(i);
});
```

### 6. `Parallel.ForEach` Loop

O loop `Parallel.ForEach` é usado para iterar sobre coleções em paralelo.

```jsx
int[] numbers = { 1, 2, 3, 4, 5 };
Parallel.ForEach(numbers, number =>
{
    Console.WriteLine(number);
});
```