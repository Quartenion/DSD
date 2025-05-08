import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

// Interfaz utilizada por los servidores de cara al cliente

public interface InterfazServidorCliente extends Remote { 
    
    // Comprueba si un cliente esta registrado
    public boolean registrado(String nombreCliente) throws RemoteException;

    // Realiza registro de un cliente a partir de su nombre y password
    // Devuelve true si se registra el cliente (no existe), false si no se registra (ya existe)
    public boolean realizarRegistro(String nombreCliente) throws RemoteException;

    // Realiza una donacion de un cliente a partir de su nombre y la cantidad donada
    public void realizarDonacion(String nombreCliente, float donacion) throws RemoteException;

    // Comprueba que el nombre y la contraseña coincide devolviendo true para iniciar sesion
    // devuelve false si no concuerdan para para que no pueda iniciar sesion
    public Boolean inicioSesion(String nombreCliente) throws RemoteException;

    // Obtiene la cantidad total donada en un servidor
    public float obtenerSubtotalDonado(String nombreCliente) throws RemoteException;

    // Obtiene la cantidad total donada a un servidor por un cliente
    public float obtenerDonacionCliente(String nombreCliente) throws RemoteException;
    
    //obtiene el total donado en ambos servidores
    public float obtenerTotalDonadoServidores() throws RemoteException;

    //obtiene la lista de donantes sin orden
    public ArrayList<Cliente> obtenerListeDonantes() throws RemoteException;
}