/* calculadora.x : Protocolo de cálculo remoto */
struct operandos {
    double a;
    double b;
};

program CALC_PROG {
    version CALC_VER {
        double suma(operandos) = 1;
        double resta(operandos) = 2;
        double multiplicacion(operandos) = 3;
        double division(operandos) = 4;
    } = 1;
} = 0x20000001;
