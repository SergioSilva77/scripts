package com.mulithreadcalculadora;

public class Calculadora {

    private int soma;

    public synchronized int somaArray(int[] array) throws InterruptedException {
        soma = 0;
        for (int i = 0; i< array.length ; i++) {
            soma += array[i];
            System.out.println("Executando a soma "+Thread.currentThread().getName() + " somando o valor "+ array[i] + " Com total de "+ soma);
            Thread.sleep(100);
        }
        return soma;
    }

}
