import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.DrbgParameters.Reseed;
import java.util.HashMap;
import java.util.Map;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;

public class Donacion extends UnicastRemoteObject implements Donacion_I {
    private String servidor;
    private String replica;
    private float subtotalDonado;

    private Map<String, Cliente> clientesRegistrados;

    // Constructor 
    public Donacion(String servidor, String replica) throws RemoteException {
        this.servidor = servidor;
        this.replica = replica;
        this.subtotalDonado = 0.0f;
        clientesRegistrados = new HashMap<>();
    }

    // Comprueba si un cliente ya esta registrado o en la replica 
    @Override
    public boolean registrado(String nombreCliente) throws RemoteException {
        Donacion_I servidorReplica = getReplica("localhost", this.replica);
        return this.existe(nombreCliente) || servidorReplica.existe(nombreCliente);
    }

    @Override
    public boolean registrar(String nombreCliente, String servidorDestino) throws RemoteException {
        // Verificar si el cliente ya está registrado en el servidor actual
        if (existe(nombreCliente)) return false;

        // Verificar si el cliente ya está registrado en la réplica
        Donacion_I servidorReplica = getReplica("localhost", this.replica);
        if (servidorReplica.existe(nombreCliente)) return false;

        // Verificar en qué servidor se quiere registrar el cliente
        if (servidorDestino.equals(this.servidor)) {
            // Registrar en el servidor actual
            confirmarRegistroCliente(nombreCliente, this.servidor);
        } else if (servidorDestino.equals(this.replica)) {
            // Registrar en la réplica
            servidorReplica.confirmarRegistroCliente(nombreCliente, this.replica);
        } else {
            System.out.println("Servidor de destino no válido.");
            return false;
        }

        return true;
    }


    @Override
    public Boolean iniciarSesion(String nombreCliente) throws RemoteException {
        String servidorCliente = identificarCliente(nombreCliente);
        if (servidorCliente.equals(this.servidor)) {
            return existe(nombreCliente);
        } else if (servidorCliente.equals(this.replica)) {
            Donacion_I servidorReplica = getReplica("localhost", this.replica);
            return servidorReplica.existe(nombreCliente);
        }
        return false;
    }

    @Override
    public String identificarCliente(String nombreCliente) throws RemoteException {
        if (existe(nombreCliente)) return this.servidor;

        Donacion_I servidorReplica = getReplica("localhost", this.replica);
        if (servidorReplica.existe(nombreCliente)) return this.replica;

        return "";
    }

    @Override
    public boolean existe(String nombreCliente) throws RemoteException {
        return clientesRegistrados.containsKey(nombreCliente);
    }

    @Override
    public int getNumClientesRegistrados() throws RemoteException {
        return clientesRegistrados.size();
    }

    @Override
    public float getSubtotalDonado(String nombreCliente) throws RemoteException {
        String servidorCliente = identificarCliente(nombreCliente);
        if (servidorCliente.equals(this.servidor)) {
            return getSubtotalDonado();
        } else if (servidorCliente.equals(this.replica)) {
            Donacion_I servidorReplica = getReplica("localhost", this.replica);
            return servidorReplica.getSubtotalDonado();
        }
        return 0.0f;
    }

    @Override
    public float getSubtotalDonado()throws RemoteException {
        return this.subtotalDonado;
    }
    
    @Override
    public void confirmarRegistroCliente(String nombreCliente, String nombreServidor) throws RemoteException {
        clientesRegistrados.put(nombreCliente, new Cliente(nombreCliente));
        System.out.println("Cliente " + nombreCliente + " registrado en el servidor " + nombreServidor);
    }

    @Override
    public Donacion_I getReplica(String host, String nombreReplica) throws RemoteException {
        Donacion_I servidorReplica = null;

        try {
            Registry registroRMI = LocateRegistry.getRegistry(host, 1099);
            servidorReplica = (Donacion_I) registroRMI.lookup(nombreReplica);
        } catch (NotBoundException | RemoteException e) {
            System.out.println("No se encuentra el servidor replica con el nombre " + nombreReplica);
        }

        return servidorReplica;
    }

    // Incrementa el subtotal donado al servidor en una cantidad
    @Override
    public void incrementarSubtotalDonado(float donacion) throws RemoteException {
        this.subtotalDonado += donacion;
    }

    @Override
    public float getDonacionCliente(String nombreCliente) throws RemoteException {
        String nombreServidor = identificarCliente(nombreCliente);

        if(nombreServidor.equals(this.replica)){
            Donacion_I servidorReplica = this.getReplica("localhost", this.replica);
            Cliente cliente = servidorReplica.getClientesRegistrados().get(nombreCliente);
            return cliente.getTotal();   
        }else if(nombreServidor.equals(this.servidor)){
            Cliente cliente = this.clientesRegistrados.get(nombreCliente);
            return cliente.getTotal(); 
        }
        return 0;
    }

    public Map<String, Cliente> getClientesRegistrados() {
        return clientesRegistrados;
    }
    
    @Override
    public void donar(String nombreCliente, float donacion) throws RemoteException {
        String servidorCliente = identificarCliente(nombreCliente);
        if (servidorCliente.equals(this.servidor)) {
            Cliente cliente = clientesRegistrados.get(nombreCliente);
            cliente.donar(donacion);
            incrementarSubtotalDonado(donacion);
            System.out.println("El cliente " + nombreCliente + " ha donado " + donacion);
        } else if (servidorCliente.equals(this.replica)) {
            Donacion_I servidorReplica = getReplica("localhost", this.replica);
            servidorReplica.donar(nombreCliente, donacion);
        }
    }


    @Override
    public float getTotalDonado() throws RemoteException {
        Donacion_I servidorReplica = getReplica("localhost", this.replica);
        return this.subtotalDonado + servidorReplica.getSubtotalDonado();
    }

    @Override
    public ArrayList<Cliente> getListaDonantes() throws RemoteException{
        Donacion_I servidorReplica = this.getReplica("localhost", this.replica);
        Map<String,Cliente> clientesReplica = servidorReplica.getClientesRegistrados();

        Map<String,Cliente> clientesServidor = this.getClientesRegistrados();

        ArrayList<Cliente> listaClientes = new ArrayList<>();

          // Agregar los clientes de la réplica al ArrayList
        for (Cliente cliente : clientesReplica.values()) {
            if(cliente.getNumDonaciones() > 0){
                listaClientes.add(cliente);
            }
        }

        // Agregar los clientes del servidor actual al ArrayList
        for (Cliente cliente : clientesServidor.values()) {
            if(cliente.getNumDonaciones() > 0){
                listaClientes.add(cliente);
            }
        }

        return listaClientes;
    }
}