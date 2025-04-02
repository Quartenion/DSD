import math
from calculadora import Calculadora

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

class ServidorTrigonometricoHandler:
    def seno(self, angulo):
        print(f"Calculando seno({angulo})")
        resultado = math.sin(math.radians(angulo))
        # Redondear el resultado para evitar pequeños errores de precisión
        return round(resultado, 10)  # Redondea a 10 decimales
    
    def coseno(self, angulo):
        print(f"Calculando coseno({angulo})")
        resultado = math.cos(math.radians(angulo))
        return round(resultado, 10)
    
    def tangente(self, angulo):
        print(f"Calculando tangente({angulo})")
        resultado = math.tan(math.radians(angulo))
        return round(resultado, 10)

if __name__ == "__main__":
    handler = ServidorTrigonometricoHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9091)  # Puerto diferente al servidor principal
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    
    print("Iniciando servidor trigonométrico...")
    server.serve()
    print("Servidor trigonométrico finalizado")
