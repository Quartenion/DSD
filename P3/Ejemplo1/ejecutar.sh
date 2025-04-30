#!/bin/bash
echo "Lanzando el ligador de RMI…"

# Matar el proceso rmiregistry si ya está en ejecución
echo "Verificando si hay un proceso rmiregistry en ejecución..."
pkill -f rmiregistry  # Matar cualquier proceso de rmiregistry que esté ejecutándose

# Espera para asegurarse de que el proceso ha terminado
sleep 2

# Inicia el rmiregistry
echo "Iniciando el rmiregistry..."
rmiregistry &  # Inicia el rmiregistry en segundo plano

# Esperar un poco más para asegurarse de que el rmiregistry esté listo
sleep 5

# Verificar si el rmiregistry está corriendo (opcional)
if ! pgrep -x "rmiregistry" > /dev/null
then
    echo "El rmiregistry no se ha iniciado correctamente. Abortando."
    exit 1
fi

echo "Compilando con javac..."
javac *.java
sleep 2

echo "Lanzando el servidor..."
java -Djava.security.policy=server.policy -cp . -Djava.rmi.server.codebase=file:./ -Djava.rmi.server.hostname=localhost Ejemplo &
sleep 2

echo "Lanzando el primer cliente..."
java -Djava.security.policy=server.policy -cp . Cliente_Ejemplo localhost 0
sleep 2

echo "Lanzando el segundo cliente..."
java -Djava.security.policy=server.policy -cp . Cliente_Ejemplo localhost 3
