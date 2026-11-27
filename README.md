# DSD · Desarrollo de Sistemas Distribuidos

Prácticas de la asignatura Desarrollo de Sistemas Distribuidos del Grado en Ingeniería Informática de la Universidad de Granada.

La idea de la asignatura es resolver el mismo tipo de problema, un cliente que le pide algo a un servidor, con una tecnología distinta en cada práctica. Por eso hay dos calculadoras remotas, una con Sun RPC en C y otra con Apache Thrift en Python, después un sistema de donaciones con Java RMI y, por último, un sistema domótico en tiempo real con Node.js y Socket.IO.

## Las prácticas

**P2 · Calculadora con Sun RPC (C).** El cliente manda la operación y los números al servidor, que la resuelve y le devuelve el resultado. La interfaz se define en `calculadora.x` y `rpcgen` genera a partir de ella el código que se encarga de la comunicación.

**P2 · Calculadora con Apache Thrift (Python).** Es la misma idea, pero más completa: además de las operaciones básicas tiene factorial, MCD y MCM, operaciones con vectores y evaluación de expresiones. El servidor principal no lo hace todo él solo, sino que le pasa parte del trabajo a un segundo servidor, así que también hace de cliente.

**P3 · Sistema de donaciones con Java RMI.** Hay dos servidores, el principal y una réplica, que se reparten a los clientes registrados. Un cliente se registra en uno de los dos, inicia sesión y dona, y los servidores se comunican entre ellos para saber dónde está registrado cada cliente y cuánto se ha donado en total. Las carpetas `Ejemplo1` a `Ejemplo3` son los ejemplos guiados que se hicieron antes del ejercicio.

**P4 · Sistema domótico (Node.js, Socket.IO y MongoDB).** Una página simula los sensores de una casa (temperatura, luminosidad, humedad y lluvia) y envía las medidas al servidor, que las reparte en tiempo real a todos los clientes conectados. Un agente revisa cada medida y, si se sale de los umbrales, lanza una alarma y activa los actuadores que hagan falta. Por ejemplo, si entra demasiada luz cierra la persiana, si hace frío enciende la calefacción y cierra la ventana si está abierta, y si llueve cierra la ventana y apaga el riego. Desde la página del usuario se ve el estado de todo y se pueden controlar los actuadores a mano, y el historial de eventos se guarda en MongoDB.

Cada práctica tiene su enunciado o su memoria en PDF dentro de su carpeta.

## Cómo ejecutarlo

**Requisitos:** Linux. Para cada práctica, además, lo que se indica en su apartado.

### P2 · Calculadora con Sun RPC

```bash
sudo apt install build-essential libtirpc-dev rpcbind
cd P2/RPC
make -f Makefile.calculadora
./calculadora_server &
./calculadora_client localhost
```

### P2 · Calculadora con Apache Thrift

```bash
pip install thrift
cd P2/APACHE_THRIFT/gen-py
python3 segundo_servidor.py &   # puerto 9091
python3 servidor.py &           # puerto 9090
python3 cliente.py
```

### P3 · Donaciones con Java RMI

```bash
cd P3/EJERCICIO
javac *.java
java -Djava.security.policy=server.policy Servidor &          # registro RMI en el puerto 1099
java -Djava.security.policy=server.policy ServidorReplica &   # registro RMI en el puerto 1098
java -Djava.security.policy=server.policy Cliente
```

El cliente es interactivo, así que te va pidiendo por teclado lo que quieres hacer. Para los ejemplos guiados basta con ejecutar `sh ejecutar.sh` dentro de `P3/Ejemplo1`.

### P4 · Sistema domótico

Necesita Node.js y un MongoDB local en `mongodb://localhost:27017`.

```bash
cd P4/SistemaDomotico
npm install
node servidor.js
```

Después abre en el navegador:
- http://localhost:8080/sensores para enviar las medidas de los sensores
- http://localhost:8080/usuario para ver el estado y las alarmas, y controlar los actuadores

## Estructura

```
P2/RPC/               <- calculadora con Sun RPC
P2/APACHE_THRIFT/     <- calculadora con Apache Thrift
P3/EJERCICIO/         <- sistema de donaciones con Java RMI
P3/Ejemplo1..3/       <- ejemplos guiados de RMI
P4/SistemaDomotico/   <- sistema domótico
P4/ejercicios/        <- ejercicios previos con Node.js, Socket.IO y MongoDB
codigo-ejemplos/      <- ejemplos de clase de RPC y Thrift
```
