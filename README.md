# DSD · Desarrollo de Sistemas Distribuidos

Prácticas de **Desarrollo de Sistemas Distribuidos** (Grado en Ingeniería Informática, Universidad de Granada). Cada práctica resuelve un problema cliente-servidor con una tecnología distinta de comunicación entre procesos.

| Carpeta | Tecnología | Qué hace |
|---|---|---|
| `P2/RPC/` | Sun RPC (C) | Calculadora remota: el cliente envía operaciones y el servidor las resuelve. |
| `P2/APACHE_THRIFT/` | Apache Thrift (Python) | Calculadora remota más completa (operaciones básicas, factorial, MCD/MCM, vectores, expresiones). El servidor delega parte del trabajo en un segundo servidor. |
| `P3/` | Java RMI | `EJERCICIO/` es un **sistema de donaciones replicado**: dos servidores (principal y réplica) reparten a los clientes registrados y llevan el total donado. `Ejemplo1`…`Ejemplo3` son los ejemplos guiados. |
| `P4/SistemaDomotico/` | Node.js, Socket.IO, MongoDB | **Sistema domótico**: una página simula sensores, otra es el panel del usuario y un agente activa actuadores (aire, calefacción, persianas, riego, ventanas) cuando las medidas superan los umbrales. Guarda el historial en MongoDB. |
| `P4/ejercicios/` | Node.js | Ejercicios previos: calculadora web, conexiones con Socket.IO y prueba de MongoDB. |
| `codigo-ejemplos/` | RPC y Thrift | Ejemplos de clase (mensajes remotos, listado de directorios, calculadora). |

Cada práctica tiene su enunciado o memoria en PDF dentro de su carpeta.

## Cómo ejecutarlo

Todo está pensado para Linux.

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

El cliente es interactivo: pide los datos por teclado para registrarse, iniciar sesión y donar.

Para los ejemplos guiados basta con `sh ejecutar.sh` dentro de `P3/Ejemplo1`.

### P4 · Sistema domótico

Necesita Node.js y un MongoDB local en `mongodb://localhost:27017`.

```bash
cd P4/SistemaDomotico
npm install
node servidor.js
```

Después abre en el navegador:
- <http://localhost:8080/sensores> para enviar medidas de los sensores.
- <http://localhost:8080/usuario> para ver el estado, las alarmas y controlar los actuadores.
