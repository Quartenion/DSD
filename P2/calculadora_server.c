#include "calculadora.h"
#include "matriz_calculadora.h"
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
    matrices_mc mat_mc;
    resultado_matriz_mc *res_mc;
    CLIENT *clnt;

    // Inicializar estructuras
    mat_mc.A.filas = mat.A.filas;
    mat_mc.A.columnas = mat.A.columnas;
    mat_mc.A.valores.valores_len = mat.A.valores.valores_len;
    mat_mc.A.valores.valores_val = (double *)malloc(sizeof(double) * mat.A.filas * mat.A.columnas);

    mat_mc.B.filas = mat.B.filas;
    mat_mc.B.columnas = mat.B.columnas;
    mat_mc.B.valores.valores_len = mat.B.valores.valores_len;
    mat_mc.B.valores.valores_val = (double *)malloc(sizeof(double) * mat.B.filas * mat.B.columnas);

    if (!mat_mc.A.valores.valores_val || !mat_mc.B.valores.valores_val) {
        fprintf(stderr, "Error: No se pudo asignar memoria.\n");
        return NULL;
    }

    // Copiar datos de matrices
    memcpy(mat_mc.A.valores.valores_val, mat.A.valores.valores_val, sizeof(double) * mat.A.filas * mat.A.columnas);
    memcpy(mat_mc.B.valores.valores_val, mat.B.valores.valores_val, sizeof(double) * mat.B.filas * mat.B.columnas);

    // 📌 OPCIÓN 1: LLAMAR A OTRO SERVIDOR RPC (Servidor 2)
    clnt = clnt_create("localhost", MATRIZ_CALC_PROG, MATRIZ_CALC_VER, "tcp");
    if (clnt == NULL) {
        clnt_pcreateerror("Error al conectar con Servidor 2");
        return NULL;
    }

    res_mc = multiplicar_matrices_mc_1(mat_mc, clnt);  // ✅ Aquí llamamos la versión remota
    if (res_mc == NULL) {
        clnt_perror(clnt, "Error en llamada RPC a Servidor 2");
        clnt_destroy(clnt);
        return NULL;
    }
    
    clnt_destroy(clnt); // Destruir cliente después de la llamada

    // 📌 OPCIÓN 2: LLAMAR DIRECTAMENTE LA FUNCIÓN LOCAL
    // res_mc = multiplicar_matrices_mc_1_svc(mat_mc, rqstp);

    // Copiar el resultado a la estructura de retorno
    result.filas = res_mc->filas;
    result.columnas = res_mc->columnas;
    result.valores.valores_len = res_mc->valores.valores_len;
    result.valores.valores_val = (double *)malloc(sizeof(double) * result.filas * result.columnas);

    if (!result.valores.valores_val) {
        fprintf(stderr, "Error: No se pudo asignar memoria para el resultado.\n");
        return NULL;
    }

    memcpy(result.valores.valores_val, res_mc->valores.valores_val, sizeof(double) * result.filas * result.columnas);

    // Liberar memoria
    free(mat_mc.A.valores.valores_val);
    free(mat_mc.B.valores.valores_val);

    return &result;
}

