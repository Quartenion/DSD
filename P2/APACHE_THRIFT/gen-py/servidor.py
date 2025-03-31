import glob
import sys

from calculadora import Calculadora

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import logging

logging.basicConfig(level=logging.DEBUG)


class CalculadoraHandler:
    def __init__(self):
        self.log = {}

    def suma(self, n1, n2):
        print(f"Sumando {n1} + {n2}")
        return n1 + n2

    def resta(self, n1, n2):
        print(f"Restando {n1} - {n2}")
        return n1 - n2
    
    def multiplicacion(self, n1, n2):
        print(f"Multiplicando {n1} * {n2}")
        return n1 * n2
    
    def division(self, n1, n2):
        print(f"Dividiendo {n1} / {n2}")
        if n2 == 0:
            raise ZeroDivisionError("No se puede dividir por cero")
        return n1 / n2  
    
    def potencia(self, n1, n2):
        print(f"Elevando {n1} ^ {n2}")
        return pow(n1, n2)

    def raiz(self, n1, n2):
        print(f"Calculando raíz {n2}-ésima de {n1}")
        return pow(n1, 1/n2)
    
    def factorial(self, n1):
        print(f"Calculando factorial de {n1}")
        if n1 < 0:
            raise ValueError("El número debe ser no negativo")
        if n1 == 0:
            return 1
        else:
            return n1 * self.factorial(n1 - 1)
        
    def mcd(self, n1, n2, n3, n4):
        print(f"Calculando MCD de {n1}, {n2}, {n3}, {n4}")
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a
        return gcd(gcd(n1, n2), gcd(n3, n4))
    
    def mcm(self, n1, n2, n3, n4):
        print(f"Calculando MCM de {n1}, {n2}, {n3}, {n4}")
        def lcm(a, b):
            return abs(a * b) // gcd(a, b)
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a
        return lcm(lcm(n1, n2), lcm(n3, n4))
    
    def suma_matrices(self, m1, m2):
        print(f"Sumando matrices {m1} + {m2}")
        return [[m1[i][j] + m2[i][j] for j in range(len(m1[0]))] for i in range(len(m1))]
    
    def resta_matrices(self, m1, m2):
        print(f"Restando matrices {m1} - {m2}")
        return [[m1[i][j] - m2[i][j] for j in range(len(m1[0]))] for i in range(len(m1))]
    
    def multiplica_matrices(self, m1, m2):
        print(f"Multiplicando matrices {m1} * {m2}")
        if len(m1[0]) != len(m2):
            raise ValueError("Las matrices no se pueden multiplicar")
        return [[sum(m1[i][k] * m2[k][j] for k in range(len(m2))) for j in range(len(m2[0]))] for i in range(len(m1))]
    
    def suma_vectores(self, v1, v2):
        print(f"Sumando vectores {v1} + {v2}")
        return [v1[i] + v2[i] for i in range(len(v1))]
    
    def resta_vectores(self, v1, v2):
        print(f"Restando vectores {v1} - {v2}")
        return [v1[i] - v2[i] for i in range(len(v1))]
    
    def producto_escalar(self, v1, v2):
        print(f"Producto escalar de {v1} * {v2}")
        return sum(v1[i] * v2[i] for i in range(len(v1)))
    
    def producto_vectorial(self, v1, v2):
        print(f"Producto vectorial de {v1} x {v2}")
        if len(v1) != 3 or len(v2) != 3:
            raise ValueError("El producto vectorial solo es válido para vectores de dimensión 3")
        return [
            v1[1] * v2[2] - v1[2] * v2[1],
            v1[2] * v2[0] - v1[0] * v2[2],
            v1[0] * v2[1] - v1[1] * v2[0]
        ]
    
    def evaluar_expresion(self, expresion):
        print(f"Evaluando expresión: {expresion}")
        try:
            resultado = eval(expresion)
            return resultado
        except Exception as e:
            raise ValueError(f"Error al evaluar la expresión: {e}")

if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("Iniciando servidor...")
    server.serve()
    print("Servidor finalizado")
