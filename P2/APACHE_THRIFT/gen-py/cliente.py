from calculadora import Calculadora

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

import os

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

while True:
    print("-=+=-MENÚ DE OPCIONES-=+=-")
    print("1. OPERACIONES BÁSICAS")
    print("2. OPERACIONES INTERMEDIAS")
    print("3. OPERACIONES AVANZADAS")
    print("4. SALIR")
    print("Seleccione una opción:")

    try:
        opcion = int(input())
        if 1 <= opcion <= 4:
            os.system('clear')
            pass
        else:
            print("POR FAVOR, INTRODUZCA UN NÚMERO ENTRE 1 Y 4")

    except ValueError:
        print("INTRODUZCA UN NÚMERO")
        

    if opcion == 1:
        print("-=+=-MENÚ DE OPERACIONES BÁSICAS-=+=-")
        print("1. SUMA")
        print("2. RESTA")
        print("3. MULTIPLICACIÓN")
        print("4. DIVISIÓN")
        print("5. POTENCIA")
        print("6. RAÍZ ENÉSIMA")
        print("7. ATRÁS")
        print("Seleccione una opción:")
        opcion = int(input())

        if opcion == 7:
            os.system('clear')
            continue
        
        while True:
            print("Ingrese el primer número:")
            try:
                num1 = int(input())
                break
            except ValueError:
                print("POR FAVOR, INTRODUZCA UN NÚMERO VÁLIDO")
        
        while True:
            print("Ingrese el segundo número:")
            try:
                num2 = int(input())
                break
            except ValueError:
                print("POR FAVOR, INTRODUZCA UN NÚMERO VÁLIDO")

        if opcion == 1:
            resultado = client.suma(num1, num2)
            print(str(num1) + " + " + str(num2) + " = " + str(resultado))
        elif opcion == 2:
            resultado = client.resta(num1, num2)
            print(str(num1) + " - " + str(num2) + " = " + str(resultado))
        elif opcion == 3:
            resultado = client.multiplicacion(num1, num2)
            print(str(num1) + " * " + str(num2) + " = " + str(resultado))
        elif opcion == 4:
            resultado = client.division(num1, num2)
            print(str(num1) + " / " + str(num2) + " = " + str(resultado))
        elif opcion == 5:
            resultado = client.potencia(num1, num2)
            print(str(num1) + " ^ " + str(num2) + " = " + str(resultado))
        elif opcion == 6:
            resultado = client.raiz(num1, num2)
            print(str(num1) + " ^ (1/" + str(num2) + ") = " + str(resultado))
        elif opcion == 7:
            exit(1);


    elif opcion == 2:
        print("-=+=-MENÚ DE OPERACIONES INTERMEDIAS-=+=-")
        print("1. FACTORIAL")
        print("2. MCD ENTRE 4 NÚMEROS")
        print("3. MCM ENTRE 4 NÚMEROS")
        print("4. SALIR")

        print("Opción no válida")

    elif opcion == 3:
        print("-=+=-MENU DE OPERACIONES AVANZADAS-=+=-")
        print("1. SUMA DE MATRICES")
        print("2. RESTA DE MATRICES")
        print("3. MULTIPLICACIÓN DE MATRICES")
    
    elif opcion == 4:
        break

transport.close()
