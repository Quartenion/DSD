import os
from calculadora import Calculadora
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

def leer_entero(mensaje):
    while True:
        try:
            return int(input(mensaje))
        except ValueError:
            print("POR FAVOR, INTRODUZCA UN NÚMERO VÁLIDO")

def imprimir_matriz(matriz):
    for fila in matriz:
        print("  ".join(f"{elem:.2f}" for elem in fila))
    print() 

while True:
    os.system('clear')
    print("-=+=-MENÚ DE OPCIONES-=+=-")
    print("1. OPERACIONES BÁSICAS")
    print("2. OPERACIONES INTERMEDIAS")
    print("3. OPERACIONES AVANZADAS")
    print("4. OPERACIONES TRIGONOMÉTRICAS")
    print("5. SALIR")
    opcion = leer_entero("Seleccione una opción: ")
    if opcion not in range(1, 6):
        os.system('clear')
        print("POR FAVOR, INTRODUZCA UN NÚMERO ENTRE 1 Y 4")
        input("\nPresione ENTER para continuar...")
        os.system('clear')
        continue
    os.system('clear')

    if opcion == 1:
        while True:
            print("-=+=-MENÚ DE OPERACIONES BÁSICAS-=+=-")
            print("1. SUMA")
            print("2. RESTA")
            print("3. MULTIPLICACIÓN")
            print("4. DIVISIÓN")
            print("5. POTENCIA")
            print("6. RAÍZ ENÉSIMA")
            print("7. ATRÁS")
            
            opcion_basica = leer_entero("Seleccione una opción: ")
            
            if opcion_basica == 7:
                os.system('clear')
                break

            if opcion_basica not in range(1, 7):
                os.system('clear')
                print("POR FAVOR, INTRODUZCA UN NÚMERO ENTRE 1 Y 7")
                input("\nPresione ENTER para continuar...")
                os.system('clear')
                continue
            
            num1 = leer_entero("Ingrese el primer número: ")
            num2 = leer_entero("Ingrese el segundo número: ")

            if opcion_basica == 1:
                resultado = client.suma(num1, num2)
                print(f"{num1} + {num2} = {resultado}")
            elif opcion_basica == 2:
                resultado = client.resta(num1, num2)
                print(f"{num1} - {num2} = {resultado}")
            elif opcion_basica == 3:
                resultado = client.multiplicacion(num1, num2)
                print(f"{num1} * {num2} = {resultado}")
            elif opcion_basica == 4:
                if num2 == 0:
                    print("Error: No se puede dividir entre 0")
                else:
                    resultado = client.division(num1, num2)
                    print(f"{num1} / {num2} = {resultado}")
            elif opcion_basica == 5:
                resultado = client.potencia(num1, num2)
                print(f"{num1} ^ {num2} = {resultado}")
            elif opcion_basica == 6:
                resultado = client.raiz(num1, num2)
                print(f"{num1} ^ (1/{num2}) = {resultado}")

            input("\nPresione ENTER para continuar...")  
            os.system('clear')

    elif opcion == 2:
        while True:
            print("-=+=-MENÚ DE OPERACIONES INTERMEDIAS-=+=-")
            print("1. FACTORIAL")
            print("2. MCD ENTRE 4 NÚMEROS")
            print("3. MCM ENTRE 4 NÚMEROS")
            print("4. ATRÁS")

            opcion_intermedia = leer_entero("Seleccione una opción: ")

            if opcion_intermedia == 4:
                break

            if opcion_intermedia not in range(1, 4):
                print("POR FAVOR, INTRODUZCA UN NÚMERO ENTRE 1 Y 4")
                input("\nPresione ENTER para continuar...")
                os.system('clear')
                continue

            if opcion_intermedia == 1:
                num = leer_entero("Ingrese un número: ")
                if num > 12:
                    print("Error: El factorial solo se puede calcular para números menores o iguales a 12")
                else:
                    print(f"Factorial de {num} = {client.factorial(num)}")
            elif opcion_intermedia == 2:
                nums = [leer_entero(f"Número {i+1}: ") for i in range(4)]
                print(f"MCD de {nums} = {client.mcd(*nums)}")
            elif opcion_intermedia == 3:
                nums = [leer_entero(f"Número {i+1}: ") for i in range(4)]
                print(f"MCM de {nums} = {client.mcm(*nums)}")

            input("\nPresione ENTER para continuar...")
            os.system('clear')

    elif opcion == 3:
        while True:
            print("-=+=-MENÚ DE OPERACIONES AVANZADAS-=+=-")
            print("1. SUMA DE MATRICES")
            print("2. RESTA DE MATRICES")
            print("3. MULTIPLICACIÓN DE MATRICES")
            print("4. SUMA DE VECTORES")
            print("5. RESTA DE VECTORES")
            print("6. PRODUCTO ESCALAR")
            print("7. PRODUCTO VECTORIAL")
            print("8. EXPRESIÓN ARITMÉTICA COMPUESTA")
            print("9. ATRÁS")
            
            opcion_avanzada = leer_entero("Seleccione una opción: ")

            if opcion_avanzada == 9:
                break

            if opcion_avanzada not in range(1, 9):
                print("POR FAVOR, INTRODUZCA UN NÚMERO ENTRE 1 Y 9")
                input("\nPresione ENTER para continuar...")
                os.system('clear')
                continue

            if opcion_avanzada in [1, 2, 3]:  # Operaciones con matrices
                # Solicitar dimensiones de la primera matriz
                filas1 = leer_entero("Ingrese el número de filas de la primera matriz: ")
                columnas1 = leer_entero("Ingrese el número de columnas de la primera matriz: ")

                # Solicitar dimensiones de la segunda matriz
                filas2 = leer_entero("Ingrese el número de filas de la segunda matriz: ")
                columnas2 = leer_entero("Ingrese el número de columnas de la segunda matriz: ")

                # Verificar si las matrices son multiplicables
                if columnas1 != filas2:
                    print("No se puede multiplicar las matrices: el número de columnas de la primera matriz debe ser igual al número de filas de la segunda matriz.")
                    input("\nPresione ENTER para continuar...")
                    os.system('clear')
                    continue

                def leer_matriz(filas, columnas):
                    matriz = []
                    print(f"Ingrese la matriz de tamaño {filas}x{columnas}:")
                    for i in range(filas):
                        fila = [leer_entero(f"Elemento ({i+1},{j+1}): ") for j in range(columnas)]
                        matriz.append(fila)
                    return matriz

                # Leer las dos matrices
                matriz1 = leer_matriz(filas1, columnas1)
                matriz2 = leer_matriz(filas2, columnas2)

                if opcion_avanzada == 1:  # Suma de matrices
                    resultado = client.suma_matrices(matriz1, matriz2)
                    print("Resultado de la suma:")
                    imprimir_matriz(resultado)
                elif opcion_avanzada == 2:  # Resta de matrices
                    resultado = client.resta_matrices(matriz1, matriz2)
                    print("Resultado de la resta:")
                    imprimir_matriz(resultado)
                elif opcion_avanzada == 3:  # Multiplicación de matrices
                    resultado = client.multiplica_matrices(matriz1, matriz2)
                    print("Resultado de la multiplicación:")
                    imprimir_matriz(resultado)

            elif opcion_avanzada in [4, 5, 6, 7]:  # Operaciones con vectores
                # Solicitar tamaño de los vectores
                tamano = leer_entero("Ingrese el tamaño de los vectores: ")

                # Solicitar los vectores
                def leer_vector():
                    vector = []
                    print(f"Ingrese el vector de tamaño {tamano}:")
                    for i in range(tamano):
                        vector.append(leer_entero(f"Elemento {i+1}: "))
                    return vector

                # Leer los dos vectores
                vector1 = leer_vector()
                vector2 = leer_vector()

                # Comprobación del tamaño de los vectores
                if len(vector1) != len(vector2):
                    print("Los vectores deben tener el mismo tamaño para poder operar.")
                    input("\nPresione ENTER para continuar...")
                    os.system('clear')
                    continue

                if opcion_avanzada == 4:  # Suma de vectores
                    resultado = client.suma_vectores(vector1, vector2)
                    print(f"Resultado de la suma de vectores: {resultado}")
                elif opcion_avanzada == 5:  # Resta de vectores
                    resultado = client.resta_vectores(vector1, vector2)
                    print(f"Resultado de la resta de vectores: {resultado}")
                elif opcion_avanzada == 6:  # Producto escalar
                    resultado = client.producto_escalar(vector1, vector2)
                    print(f"Resultado del producto escalar: {resultado}")
                elif opcion_avanzada == 7:  # Producto vectorial
                    try:
                        resultado = client.producto_vectorial(vector1, vector2)
                        print(f"Resultado del producto vectorial: {resultado}")
                    except ValueError as e:
                        print(e)

            elif opcion_avanzada == 8:  # Expresión aritmética
                expresion = input("Ingrese la expresión aritmética: ")

                try:
                    
                    resultado = client.evaluar_expresion(expresion)
                    print(f"Resultado de la expresión: {resultado}")
                except Exception as e:
                    print("Error en la expresión: Verifique la sintaxis y los operadores.")

            input("\nPresione ENTER para continuar...")
            os.system('clear')


    elif opcion == 4:
        while True:
            print("-=+=-MENÚ DE OPERACIONES TRIGONOMÉTRICAS-=+=-")
            print("1. SENO")
            print("2. COSENO")
            print("3. TANGENTE")
            print("4. ATRÁS")

            opcion_trigonometrica = leer_entero("Seleccione una opción: ")

            if opcion_trigonometrica == 4:
                break

            if opcion_trigonometrica not in range(1, 4):
                print("POR FAVOR, INTRODUZCA UN NÚMERO ENTRE 1 Y 4")
                input("\nPresione ENTER para continuar...")
                os.system('clear')
                continue

            angulo = leer_entero("Ingrese el ángulo en grados: ")

            if opcion_trigonometrica == 1:
                resultado = client.seno(angulo)
                print(f"Seno({angulo}) = {resultado}")
            elif opcion_trigonometrica == 2:
                resultado = client.coseno(angulo)
                print(f"Coseno({angulo}) = {resultado}")
            elif opcion_trigonometrica == 3:
                resultado = client.tangente(angulo)
                print(f"Tangente({angulo}) = {resultado}")

            input("\nPresione ENTER para continuar...")
            os.system('clear')

    elif opcion == 5:
        print("Saliendo...")
        break

transport.close()
