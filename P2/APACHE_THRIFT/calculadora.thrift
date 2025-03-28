typedef i32 int
typedef double f64  

service Calculadora {
   double suma(1:double num1, 2:double num2),
   double resta(1:double num1, 2:double num2),
   double division(1:double num1, 2:double num2),
   double multiplicacion(1:double num1, 2:double num2),
   double potencia(1:double num1, 2:double num2),
   double raiz(1:double num1, 2:double num2),

   int factorial(1:int num1),  
   int mcd(1:int num1, 2:int num2, 3:int num3, 4:int num4),  
   int mcm(1:int num1, 2:int num2, 3:int num3, 4:int num4)
}

