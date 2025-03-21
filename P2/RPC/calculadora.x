/* calculadora.x : Protocolo de cálculo remoto con operaciones mejoradas */

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

struct expresion {
    string operacion<100>;
};

program CALC_PROG {
    version CALC_VER {
        double suma(operandos) = 1;
        double resta(operandos) = 2;
        double multiplicacion(operandos) = 3;
        double division(operandos) = 4;
        double modulo(operandos) = 5;        
        double potencia(operandos) = 6;
        double raiz(operandos) = 7;
        double operacion_compuesta(expresion) = 8;
        resultado_matriz multiplicar_matrices(matrices) = 9;
        resultado_matriz suma_matrices(matrices) = 10;
        resultado_matriz resta_matrices(matrices) = 11;
    } = 1;
} = 0x20000001; 
