#!/bin/bash

# Configuración de variables
SERVER_HOST=$1
N_THREADS=$2

# Verificar si se proporcionaron los parámetros necesarios
if [ -z "$SERVER_HOST" ] || [ -z "$N_THREADS" ]; then
    echo "Uso: $0 <host_servidor> <numero_hebras>"
    exit 1
fi

# Compilación de los archivos Java
echo "Compilando archivos Java..."
javac -d . Cliente_Ejemplo_Multi_Threaded.java Ejemplo_I.java Ejemplo.java

# Crear el archivo de políticas
echo "Creando archivo de políticas..."
echo "grant codeBase \"file:./\" { permission java.security.AllPermission; };" > server.policy

# Ejecutar el servidor RMI
echo "Iniciando el servidor RMI..."
gnome-terminal -- bash -c "java -Djava.security.policy=server.policy Ejemplo"

# Esperar un momento para asegurar que el servidor RMI esté listo
sleep 2

# Ejecutar el cliente RMI con múltiples hilos
echo "Iniciando el cliente RMI..."
java -Djava.security.policy=server.policy Cliente_Ejemplo_Multi_Threaded $SERVER_HOST $N_THREADS
