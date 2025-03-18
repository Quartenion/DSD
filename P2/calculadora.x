/* calculadora.x : Protocolo de cálculo remoto */

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

program CALC_PROG {
    version CALC_VER {
        double suma(operandos) = 1;
        double resta(operandos) = 2;
        double multiplicacion(operandos) = 3;
        double division(operandos) = 4;
        resultado_matriz multiplicar_matrices(matrices) = 5;
    } = 1;
} = 0x20000001;
