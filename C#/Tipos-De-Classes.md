# Tipos de classes


### 1. `sealed class`

A palavra-chave `sealed` impede que outras classes herdem desta classe. É útil quando você deseja evitar a criação de subclasses de uma determinada classe.

```jsx
public sealed class MyClass
{
    public int MyProperty { get; set; }

    public void MyMethod()
    {
        Console.WriteLine("This is a sealed class.");
    }
}

// O código abaixo resultará em erro de compilação:
// public class DerivedClass : MyClass
// {
// }
```

### 2. `internal class`

A palavra-chave `internal` torna a classe acessível apenas dentro do mesmo assembly. É útil para encapsular implementações que não devem ser expostas fora do assembly.

```jsx
internal class MyInternalClass
{
    public int MyProperty { get; set; }

    public void MyMethod()
    {
        Console.WriteLine("This is an internal class.");
    }
}

// Esta classe pode ser acessada em qualquer lugar dentro do mesmo assembly, mas não fora dele.
public class AnotherClass
{
    public void Test()
    {
        MyInternalClass myInternal = new MyInternalClass();
        myInternal.MyMethod();
    }
}
```

### 3. `abstract class`

A palavra-chave `abstract` define uma classe que não pode ser instanciada diretamente. Classes abstratas são usadas como base para outras classes.

```jsx
public abstract class Animal
{
    public abstract void MakeSound();
}

public class Dog : Animal
{
    public override void MakeSound()
    {
        Console.WriteLine("Woof!");
    }
}

// Animal myAnimal = new Animal(); // Isso causará um erro de compilação.
Animal myDog = new Dog();
myDog.MakeSound();
```

### 4. `partial class`

A palavra-chave `partial` permite que a definição de uma classe seja dividida em vários arquivos. É útil para organizar grandes classes em vários arquivos.

```csharp
// Em File1.cs
public partial class PartialClass
{
    public void Method1()
    {
        Console.WriteLine("Method1");
    }
}

// Em File2.cs
public partial class PartialClass
{
    public void Method2()
    {
        Console.WriteLine("Method2");
    }
}

// Usando a PartialClass
PartialClass pc = new PartialClass();
pc.Method1();
pc.Method2();
```

### 5. `static class`

A palavra-chave `static` define uma classe que não pode ser instanciada e só contém membros estáticos. É útil para utilitários ou funções auxiliares que não precisam de estado de instância.

```csharp
public static class Utilities
{
    public static void PrintMessage(string message)
    {
        Console.WriteLine(message);
    }
}

// Utilizando a classe estática
Utilities.PrintMessage("Hello, World!");
```

### 6. `readonly`

A palavra-chave `readonly` pode ser aplicada a campos para indicar que eles só podem ser atribuídos durante a inicialização ou em um construtor. É útil para criar campos imutáveis.

```csharp
public class ReadOnlyExample
{
    public readonly int ReadOnlyField;

    public ReadOnlyExample(int value)
    {
        ReadOnlyField = value;
    }

    public void DisplayValue()
    {
        Console.WriteLine(ReadOnlyField);
    }
}

// Utilizando a classe ReadOnlyExample
ReadOnlyExample example = new ReadOnlyExample(10);
example.DisplayValue();
```