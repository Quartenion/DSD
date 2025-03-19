/* matriz_calculadora.x - Protocolo para Servidor 2 */

struct matriz_mc {
    int filas;
    int columnas;
    double valores<100>;
};

struct resultado_matriz_mc {
    int filas;
    int columnas;
    double valores<100>;
};

struct matrices_mc {
    matriz_mc A;
    matriz_mc B;
};

program MATRIZ_CALC_PROG {
    version MATRIZ_CALC_VER {
        resultado_matriz_mc multiplicar_matrices_mc(matrices_mc) = 1;
    } = 1;
} = 0x20000002;
