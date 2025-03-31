package org.archery.simulation.model;

class LinearCongruentialGenerator {
    private long x; // Estado interno del generador
    private final long a; // Multiplicador
    private final long c; // Incremento
    private final long m; // Módulo

    // Constructor por defecto que inicializa el generador con una semilla basada en el tiempo actual
    // y con los parámetros típicos del método de congruencia lineal.
    // (1L << 31): 2147483648 es 2^31, que es el módulo utilizado en muchos generadores de números pseudoaleatorios.
    public LinearCongruentialGenerator() {
        this(System.nanoTime(), 1103515245, 12345, (1L << 31));
    }

    // Constructor que permite especificar la semilla y los parámetros del generador.
    public LinearCongruentialGenerator(long seed, long a, long c, long m) {
        this.x = seed;
        this.a = a;
        this.c = c;
        this.m = m;
    }

    // Genera un número entero pseudoaleatorio en el rango [min, max].
    public int nextInt(int min, int max) {
        return min + (int) (nextLong() % (max - min + 1));
    }

    // Genera un número decimal pseudoaleatorio en el rango [0, 1].
    public double nextDouble() {
        return (double) nextLong() / m;
    }

    // Genera un número decimal pseudoaleatorio en un rango específico [min, max].
    public double nextDouble(double min, double max) {
        return min + (max - min) * ((double) nextLong() / m);
    }

    // Genera el siguiente número en la secuencia utilizando la ecuación de congruencia lineal.
    private long nextLong() {
        x = (a * x + c) % m;
        return x;
    }
}