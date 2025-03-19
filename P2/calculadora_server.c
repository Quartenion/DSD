#include "calculadora.h"
#include <stdlib.h> // Para malloc
#include <string.h> // Para memcpy

double *
suma_1_svc(operandos op,  struct svc_req *rqstp)
{
    static double  result;
    result = op.a + op.b;
    return &result;
}

double *
resta_1_svc(operandos op,  struct svc_req *rqstp)
{
    static double  result;
    result = op.a - op.b;
    return &result;
}

double *
multiplicacion_1_svc(operandos op,  struct svc_req *rqstp)
{
    static double  result;
    result = op.a * op.b;
    return &result;
}

double *
division_1_svc(operandos op,  struct svc_req *rqstp)
{
    static double  result;
    result = op.a / op.b;
    return &result;
}

resultado_matriz *
multiplicar_matrices_1_svc(matrices mat, struct svc_req *rqstp)
{
    static resultado_matriz result;
    
    // Obtener las dimensiones de las matrices A y B
    int filasA = mat.A.filas;
    int columnasA = mat.A.columnas;
    int filasB = mat.B.filas;
    int columnasB = mat.B.columnas;

    // Validar si se pueden multiplicar las matrices
    if (columnasA != filasB) {
        result.filas = 0;
        result.columnas = 0;
        return &result;
    }

    // Configurar el tamaño de la matriz resultante
    result.filas = filasA;
    result.columnas = columnasB;

    // Multiplicación de matrices
    for (int i = 0; i < filasA; i++) {
        for (int j = 0; j < columnasB; j++) {
            result.valores.valores_val[i * columnasB + j] = 0;
            for (int k = 0; k < columnasA; k++) {
                result.valores.valores_val[i * columnasB + j] += 
                    mat.A.valores.valores_val[i * columnasA + k] * mat.B.valores.valores_val[k * columnasB + j];
            }
        }
    }
    return &result;
}


