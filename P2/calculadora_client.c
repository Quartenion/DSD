#include "calculadora.h"

#include <stdio.h>

#include <stdlib.h>

#include <string.h>

#include <ctype.h>

#define MAX_EXPRESION 256

void leer_matriz(matriz * m, char nombre) {
  printf("Ingrese las dimensiones de la matriz %c (filas columnas): ", nombre);
  scanf("%d %d", & m -> filas, & m -> columnas);

  m -> valores.valores_len = m -> filas * m -> columnas;
  m -> valores.valores_val = (double * ) malloc(sizeof(double) * m -> valores.valores_len);

  printf("Ingrese los valores de la matriz %c: ", nombre);
  for (int i = 0; i < m -> valores.valores_len; i++) {
    scanf("%lf", & m -> valores.valores_val[i]);
  }
}

void mostrar_instrucciones() {
    printf("\n=== Instrucciones de Uso ===\n");
    printf("1. Suma: 1 1 - devuelve 2.\n");
    printf("2. Resta: 2 1 - devuelve 1.\n");
    printf("3. Multiplicación: 2 2 - devuelve 4.\n");
    printf("4. División: 2 2 - devuelve 1.\n");
    printf("5. Módulo: 4 2 - devuelve 0.\n");
    printf("6. Potencia: 2 3 - devuelve 8.\n");
    printf("7. Raíz: 9 2 - devuelve 3; 27 3 - devuelve 3.\n");
    printf("8. Operación Compuesta: 1 + 2 / 3 * 4 - devuelve 4.\n");
    printf("9. Multiplicación de Matrices: Ingrese dos matrices con dimensiones compatibles.\n");
    printf("10. Suma de Matrices: Ingrese dos matrices del mismo tamaño y se sumarán.\n");
    printf("11. Resta de Matrices: Ingrese dos matrices del mismo tamaño y se restarán.\n");
    printf("505. Mostrar esta ayuda.\n");
    printf("0. Salir del programa.\n");
}

void calc_prog_1(char * host) {
  CLIENT * clnt;
  double * result;
  operandos op;
  int opcion;
  expresion exp;
  resultado_matriz * res_mat;
  matrices mat;

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
    printf("5. Módulo\n");
    printf("6. Potencia\n");
    printf("7. Raíz\n");
    printf("8. Operacion Compuesta\n");
    printf("9. Multiplicación de Matrices\n");
    printf("10. Suma de Matrices\n");
    printf("11. Resta de Matrices\n");
    printf("0. Salir\n");
	printf("505. Instrucciones de Uso\n");
    printf("Seleccione una opción: ");
    scanf("%d", & opcion);

    if (opcion == 0) {
      break;
    }
	else if (opcion == 505) {
		mostrar_instrucciones();
		continue;
	}

    if (opcion >= 1 && opcion <= 7) {
      printf("Ingrese dos números: ");
      scanf("%lf %lf", & op.a, & op.b);

      switch (opcion) {
      case 1:
        result = suma_1(op, clnt);
        break;
      case 2:
        result = resta_1(op, clnt);
        break;
      case 3:
        result = multiplicacion_1(op, clnt);
        break;
      case 4:
        result = (op.b != 0) ? division_1(op, clnt) : NULL;
        break;
      case 5:
        result = (op.b != 0) ? modulo_1(op, clnt) : NULL;
        break;
      case 6:
        result = potencia_1(op, clnt);
        break;
      case 7:
        result = raiz_1(op, clnt);
        break;
      }

      if (result != NULL) {
        printf("Resultado: %.2f\n", * result);
      } else {
        printf("La división por cero no está permitida.\n");
      }
    } else if (opcion == 8) {
      printf("Ingrese la expresión a evaluar: ");
      exp.operacion = (char * ) malloc(MAX_EXPRESION * sizeof(char));
      scanf(" %[^\n]", exp.operacion);

      result = operacion_compuesta_1(exp, clnt);

      if (result != NULL) {
        printf("Resultado de la operación compuesta: %.2f\n", * result);
      } else {
        printf("Error en la evaluación de la expresión.\n");
      }
    } else if (opcion >= 9 && opcion <= 11) {
      leer_matriz( & mat.A, 'A');
      leer_matriz( & mat.B, 'B');

      if ((opcion == 9 && mat.A.columnas != mat.B.filas) ||
        (opcion >= 10 && (mat.A.filas != mat.B.filas || mat.A.columnas != mat.B.columnas))) {
        printf("Error: Dimensiones incorrectas para la operación.\n");
        free(mat.A.valores.valores_val);
        free(mat.B.valores.valores_val);
        continue;
      }

      res_mat = (opcion == 9) ? multiplicar_matrices_1(mat, clnt) :
        (opcion == 10) ? suma_matrices_1(mat, clnt) :
        resta_matrices_1(mat, clnt);

      if (res_mat == NULL) {
        clnt_perror(clnt, "Error en la llamada RPC de matrices");
      } else {
        printf("\nMatriz Resultado (%d x %d):\n", res_mat -> filas, res_mat -> columnas);
        for (int i = 0; i < res_mat -> filas; i++) {
          for (int j = 0; j < res_mat -> columnas; j++) {
            printf("%.2f ", res_mat -> valores.valores_val[i * res_mat -> columnas + j]);
          }
          printf("\n");
        }
        free(res_mat -> valores.valores_val);
      }
      free(mat.A.valores.valores_val);
      free(mat.B.valores.valores_val);
    } else {
      printf("Opción inválida\n");
    }
  }
  clnt_destroy(clnt);
}

int main(int argc, char * argv[]) {
  if (argc < 2) {
    printf("usage: %s server_host\n", argv[0]);
    exit(1);
  }
  calc_prog_1(argv[1]);
  exit(0);
}