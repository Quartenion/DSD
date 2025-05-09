import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface Donacion_I extends Remote { 
    // Comprueba si el cliente está registrado en este servidor
    boolean existe(String nombreCliente) throws RemoteException;

    // Comprueba si el cliente está registrado en este servidor o su réplica
    boolean registrado(String nombreCliente) throws RemoteException;

    // Registra al cliente en el servidor indicado si no está registrado
    boolean registrar(String nombreCliente, String servidorDestino) throws RemoteException;

    // Comprueba si el cliente puede iniciar sesión
    Boolean iniciarSesion(String nombreCliente) throws RemoteException;

    // Devuelve el nombre del servidor donde está registrado el cliente
    String identificarCliente(String nombreCliente) throws RemoteException;

    // Devuelve el número de clientes registrados en este servidor
    int getNumClientesRegistrados() throws RemoteException;

    // Añade un cliente a la lista de registrados
    void confirmarRegistroCliente(String nombreCliente, String nombreServidor) throws RemoteException;

    // Obtiene la referencia a la réplica
    Donacion_I getReplica(String host, String nombreReplica) throws RemoteException;

    // Aumenta el subtotal donado en este servidor
    void incrementarSubtotalDonado(float donacion) throws RemoteException;

    // Devuelve el total donado en este servidor
    float getSubtotalDonado() throws RemoteException;

    // Devuelve lo donado por un cliente (en el servidor correspondiente)
    float getDonacionCliente(String nombreCliente) throws RemoteException;

    // Devuelve el subtotal del servidor donde está el cliente
    float getSubtotalDonado(String nombreCliente) throws RemoteException;

    // Devuelve el total donado entre este servidor y su réplica
    float getTotalDonado() throws RemoteException;

    // Devuelve los clientes que han donado
    ArrayList<Cliente> getListaDonantes() throws RemoteException;

    // Añade una donación a un cliente
    void donar(String nombreCliente, float donacion) throws RemoteException;

    // Devuelve el mapa de clientes registrados en este servidor
    Map<String, Cliente> getClientesRegistrados() throws RemoteException;
}
