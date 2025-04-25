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
   int mcm(1:int num1, 2:int num2, 3:int num3, 4:int num4),

   list<double> suma_vectores(1:list<double> vector1, 2:list<double> vector2),
   list<double> resta_vectores(1:list<double> vector1, 2:list<double> vector2),
   double producto_escalar(1:list<double> vector1, 2:list<double> vector2),
   list<double> producto_vectorial(1:list<double> vector1, 2:list<double> vector2),
   double evaluar_expresion(1:string expresion),
   
   list<list<double>> suma_matrices(1:list<list<double>> matriz1, 2:list<list<double>> matriz2),
   list<list<double>> resta_matrices(1:list<list<double>> matriz1, 2:list<list<double>> matriz2),
   list<list<double>> multiplica_matrices(1:list<list<double>> matriz1, 2:list<list<double>> matriz2),

   double seno(1:double num),
   double coseno(1:double num),
   double tangente(1:double num)
}
