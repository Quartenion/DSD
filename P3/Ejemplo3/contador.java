// Implementa la Interfaz remota
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;

public class contador extends UnicastRemoteObject implements icontador {
    private int suma;

    // Constructor
    public contador() throws RemoteException {
    }

    // Método para obtener la suma
    public int sumar() throws RemoteException {
        return suma;
    }

    // Método para establecer un valor a la suma
    public void sumar(int valor) throws RemoteException {
        suma = valor;
    }

    // Método para incrementar la suma
    public int incrementar() throws RemoteException {
        suma++;
        return suma;
    }
}
