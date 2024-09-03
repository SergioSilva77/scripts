package com.mulithreadcalculadora;

public class MinhaThreadSoma implements  Runnable{

    private String nome;
    private int[] nums;
    private static Calculadora calc = new Calculadora();

    public MinhaThreadSoma(String nome, int[] nums){
        this.nome = nome;
        this.nums = nums;
        new Thread(this, nome).start();
    }

    @Override
    public void run() {
        try {
            System.out.println(this.nome + " iniciada");
            int soma  = 0;
            soma = calc.somaArray(this.nums);
            System.out.println("Resultado da soma para thread "+this.nome + " é "+ soma);
            System.out.println(this.nome + " terminada");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
