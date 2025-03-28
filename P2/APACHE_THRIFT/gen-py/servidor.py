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
        print("sumando " + str(n1) + " con " + str(n2))
        return n1 + n2

    def resta(self, n1, n2):
        print("restando " + str(n1) + " con " + str(n2))
        return n1 - n2
    
    def multiplicacion(self, n1, n2):
        print("multiplicando " + str(n1) + " con " + str(n2))
        return n1 * n2
    
    def division(self, n1, n2):
        if n2 == 0:
            raise ZeroDivisionError("No se puede dividir por cero")
        return n1 / n2  
    
    def potencia(self, n1, n2):
        print("elevando " + str(n1) + " con " + str(n2))
        return pow(n1,n2);

    def raiz(self, n1, n2):
        print("radicando " + str(n1) + " con " + str(n2))
        return pow(n1,1/n2)
    
    def factorial(self, n1):
        if n1 < 0:
            raise ValueError("El número debe ser no negativo")
        if n1 == 0:
            return 1
        else:
            return n1 * self.factorial(n1 - 1)
        
    def mcd(self, n1, n2, n3, n4):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a
        return gcd(gcd(n1, n2), gcd(n3, n4))
    
    def mcm(self, n1, n2, n3, n4):
        def lcm(a, b):
            return abs(a * b) // gcd(a, b)
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a
        return lcm(lcm(n1, n2), lcm(n3, n4))

if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("iniciando servidor...")
    server.serve()
    print("fin")
