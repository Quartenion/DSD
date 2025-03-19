/* calculadora.x : Protocolo de cálculo remoto con operaciones mejoradas */

/* Definición de estructuras */
struct matriz {
    int filas;
    int columnas;
    double valores<100>;
};

struct resultado_matriz {
    int filas;
    int columnas;
    double valores<100>;
};

struct operandos {
    double a;
    double b;
};

struct matrices {
    matriz A;
    matriz B;
};

struct figura {
    double base;
    double altura;
    double radio;
    double base_menor;
};

program CALC_PROG {
    version CALC_VER {
        /* Operaciones aritméticas básicas */
        double suma(operandos) = 1;
        double resta(operandos) = 2;
        double multiplicacion(operandos) = 3;
        double division(operandos) = 4;
        double modulo(operandos) = 5;        
        double potencia(operandos) = 6;
        double raiz(double) = 7;
        
        /* Operaciones con matrices */
        resultado_matriz multiplicar_matrices(matrices) = 8;
        resultado_matriz suma_matrices(matrices) = 9;
        resultado_matriz resta_matrices(matrices) = 10;
        resultado_matriz transpuesta_matriz(matriz) = 11;
        
        /* Cálculo de áreas de figuras geométricas */
        double area_triangulo(figura) = 12;
        double area_circulo(double) = 13;
        double area_trapecio(figura) = 14;
    } = 1;
} = 0x20000001;
