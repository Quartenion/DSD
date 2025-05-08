import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface Donacion_I extends Remote { 
    // Comprueba si un cliente está registrado
    boolean existeCliente(String nombreCliente) throws RemoteException;

    // Verifica si el cliente está registrado en este servidor o en la réplica
    boolean registrado(String nombreCliente) throws RemoteException;

    // Registra un cliente si no está registrado en ninguno de los servidores
    boolean registrar(String nombreCliente) throws RemoteException;

    // Verifica si el cliente tiene sesión iniciada
    Boolean inicioSesion(String nombreCliente) throws RemoteException;

    // Identifica en qué servidor está registrado el cliente
    String identificarCliente(String nombreCliente) throws RemoteException;

    // Devuelve el número de clientes registrados en este servidor
    int getNumClientesRegistrados() throws RemoteException;

    // Confirma el registro de un cliente en un servidor
    void confirmarRegistroCliente(String nombreCliente, String nombreServidor) throws RemoteException;

    // Obtiene la referencia a la réplica del servidor
    Donacion_I obtenerReplica(String host, String nombreReplica) throws RemoteException;

    // Devuelve el nombre del servidor
    String obtenerNombreServidor() throws RemoteException;

    // Incrementa el subtotal donado en este servidor
    void incrementarSubtotalDonado(float donacion) throws RemoteException;

    // Obtiene el subtotal total donado en este servidor
    float getSubtotalDonado() throws RemoteException;

    // Obtiene el total donado a un cliente en este servidor
    float getDonacionCliente(String nombreCliente) throws RemoteException;

    // Obtiene el total donado por un cliente en ambos servidores
    float getSubtotalDonado(String nombreCliente) throws RemoteException;

    // Obtiene el total donado en ambos servidores
    float getTotalDonado() throws RemoteException;

    // Obtiene la lista de clientes que han hecho donaciones
    ArrayList<Cliente> getListaDonantes() throws RemoteException;

    // Realiza una donación por parte de un cliente
    void donar(String nombreCliente, float donacion) throws RemoteException;

    Map<String, Cliente> getClientesRegistrados() throws RemoteException;
}
