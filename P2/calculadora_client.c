#include "calculadora.h"
#include <stdio.h>
#include <stdlib.h>

void calc_prog_1(char *host) {
    CLIENT *clnt;
    double *result;
    operandos op;
    int opcion;

    matrices mat;
    resultado_matriz *res_mat;

    clnt = clnt_create(host, CALC_PROG, CALC_VER, "udp");
    if (clnt == NULL) {
        clnt_pcreateerror(host);
        exit(1);
    }

    while (1) {
        printf("\nCalculadora RPC:\n");
        printf("1. Suma\n");
        printf("2. Resta\n");
        printf("3. Multiplicacion\n");
        printf("4. Division\n");
        printf("5. Multiplicación de Matrices\n");
        printf("6. Salir\n");
        printf("Seleccione una opción: ");
        scanf("%d", &opcion);

        if (opcion == 6) {
            break;
        }

        if (opcion >= 1 && opcion <= 4) {
            printf("Ingrese dos números: ");
            scanf("%lf %lf", &op.a, &op.b);

            switch (opcion) {
                case 1:
                    result = suma_1(op, clnt);
                    if (result != NULL) {
                        printf("Resultado de la suma: %.2f\n", *result);
                    }
                    break;
                case 2:
                    result = resta_1(op, clnt);
                    if (result != NULL) {
                        printf("Resultado de la resta: %.2f\n", *result);
                    }
                    break;
                case 3:
                    result = multiplicacion_1(op, clnt);
                    if (result != NULL) {
                        printf("Resultado de la multiplicación: %.2f\n", *result);
                    }
                    break;
                case 4:
                    if (op.b == 0) {
                        printf("No se puede dividir por cero\n");
                        continue;
                    }
                    result = division_1(op, clnt);
                    if (result != NULL) {
                        printf("Resultado de la división: %.2f\n", *result);
                    }
                    break;
            }
            if (result == NULL) {
                clnt_perror(clnt, "Error en la llamada RPC");
            }
        }

        else if (opcion == 5) { // Multiplicación de matrices
            printf("Ingrese las dimensiones de la matriz A (filas columnas): ");
            scanf("%d %d", &mat.A.filas, &mat.A.columnas);

            mat.A.valores.valores_len = mat.A.filas * mat.A.columnas;
            mat.A.valores.valores_val = (double *)malloc(sizeof(double) * mat.A.valores.valores_len);

            printf("Ingrese los valores de la matriz A:\n");
            for (int i = 0; i < mat.A.valores.valores_len; i++) {
                scanf("%lf", &mat.A.valores.valores_val[i]);
            }

            printf("Ingrese las dimensiones de la matriz B (filas columnas): ");
            scanf("%d %d", &mat.B.filas, &mat.B.columnas);

            if (mat.A.columnas != mat.B.filas) {
                printf("Error: El número de columnas de A debe ser igual al número de filas de B\n");
                free(mat.A.valores.valores_val);
                continue;
            }

            mat.B.valores.valores_len = mat.B.filas * mat.B.columnas;
            mat.B.valores.valores_val = (double *)malloc(sizeof(double) * mat.B.valores.valores_len);

            printf("Ingrese los valores de la matriz B:\n");
            for (int i = 0; i < mat.B.valores.valores_len; i++) {
                scanf("%lf", &mat.B.valores.valores_val[i]);
            }

            // Llamada RPC para multiplicar matrices
            res_mat = multiplicar_matrices_1(mat, clnt);

            if (res_mat == NULL) {
                clnt_perror(clnt, "Error en la llamada RPC de multiplicación de matrices");
            } else {
                printf("\nMatriz Resultado (%d x %d):\n", res_mat->filas, res_mat->columnas);
                for (int i = 0; i < res_mat->filas; i++) {
                    for (int j = 0; j < res_mat->columnas; j++) {
                        printf("%.2f ", res_mat->valores.valores_val[i * res_mat->columnas + j]);
                    }
                    printf("\n");
                }
            }

            // Liberar memoria
            free(mat.A.valores.valores_val);
            free(mat.B.valores.valores_val);
            free(res_mat->valores.valores_val);
        }

        else {
            printf("Opción inválida\n");
        }
    }

    clnt_destroy(clnt);
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("usage: %s server_host\n", argv[0]);
        exit(1);
    }
    calc_prog_1(argv[1]);
    exit(0);
}