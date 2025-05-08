import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.DrbgParameters.Reseed;
import java.util.HashMap;
import java.util.Map;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;

public class DonacionesReplica extends UnicastRemoteObject implements InterfazServidorCliente, InterfazServidorReplica {
    private String servidor;
    private String replica;
    private float subtotalDonado;

    private Map<String, Cliente> clientesRegistrados;

    // Constructor 
    public DonacionesReplica(String servidor, String replica) throws RemoteException {
        this.servidor = servidor;
        this.replica = replica;
        this.subtotalDonado = 0.0f;
        clientesRegistrados = new HashMap<>();
    }

    // Comprueba si un cliente ya esta registrado o en la replica 
    @Override
    public boolean registrado(String nombreCliente) throws RemoteException {
        InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
        return this.existeCliente(nombreCliente) || servidorReplica.existeCliente(nombreCliente);
    }

    @Override
    public boolean registrar(String nombreCliente) throws RemoteException {
        // Verificar si ya está registrado en este servidor
        if (existeCliente(nombreCliente)) return false;

        // Verificar si está registrado en la réplica
        InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
        if (servidorReplica.existeCliente(nombreCliente)) return false;

        // Registrar cliente en el servidor con menos clientes
        if (getNumClientesRegistrados() < servidorReplica.getNumClientesRegistrados()) {
            confirmarRegistroCliente(nombreCliente, this.servidor);
        } else {
            servidorReplica.confirmarRegistroCliente(nombreCliente, this.replica);
        }
        return true;
    }

    @Override
    public Boolean inicioSesion(String nombreCliente) throws RemoteException {
        String servidorCliente = identificarCliente(nombreCliente);
        if (servidorCliente.equals(this.servidor)) {
            return existeCliente(nombreCliente);
        } else if (servidorCliente.equals(this.replica)) {
            InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
            return servidorReplica.existeCliente(nombreCliente);
        }
        return false;
    }

    @Override
    public String identificarCliente(String nombreCliente) throws RemoteException {
        if (existeCliente(nombreCliente)) return this.servidor;

        InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
        if (servidorReplica.existeCliente(nombreCliente)) return this.replica;

        return "";
    }

    // Comprueba si existe el cliente en clientesRegistrados
    // Devuelve true si existe, false si no existe
    @Override
    public boolean existeCliente(String nombreCliente) throws RemoteException {
        return clientesRegistrados.containsKey(nombreCliente);
    }

    // Devuelve el numero de clientes registrados
    @Override
    public int getNumClientesRegistrados() throws RemoteException {
        return clientesRegistrados.size();
    }

    // Comprueba que servidor esta el cliente registrado para buscar el subtotal del servidor
    @Override
    public float getSubtotalDonado(String nombreCliente) throws RemoteException {
        String servidorCliente = identificarCliente(nombreCliente);
        if (servidorCliente.equals(this.servidor)) {
            return getSubtotalDonado();
        } else if (servidorCliente.equals(this.replica)) {
            InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
            return servidorReplica.getSubtotalDonado();
        }
        return 0.0f;
    }

    // Devuelve el subtotaldonado
    @Override
    public float getSubtotalDonado()throws RemoteException {
        return this.subtotalDonado;
    }
    

    // Introduce a un cliente en clientesRegistrados
    @Override
    public void confirmarRegistroCliente(String nombreCliente, String nombreServidor) throws RemoteException {
        clientesRegistrados.put(nombreCliente, new Cliente(nombreCliente));
        System.out.println("Cliente " + nombreCliente + " registrado en el servidor " + nombreServidor);
    }

    // Devuelve una referencia a la replica del servidor, es una referencia a null si no se encuentra la replica
    @Override
    public InterfazServidorReplica obtenerReplica(String host, String nombreReplica) throws RemoteException {
        InterfazServidorReplica servidorReplica = null;

        try {
            Registry registroRMI = LocateRegistry.getRegistry(host, 1099);
            servidorReplica = (InterfazServidorReplica) registroRMI.lookup(nombreReplica);
        } catch (NotBoundException | RemoteException e) {
            System.out.println("No se encuentra el servidor replica con el nombre " + nombreReplica);
        }

        return servidorReplica;
    }

    // Devuelve el nombre del servidor
    @Override
    public String obtenerNombreServidor() throws RemoteException {
        return this.servidor;
    }

    // Incrementa el subtotal donado al servidor en una cantidad
    @Override
    public void incrementarSubtotalDonado(float donacion) throws RemoteException {
        this.subtotalDonado += donacion;
    }

    // Comprueba si el nombre y password de un cliente son correctos entre servidores

    // Obtiene la cantidad total donada por un cliente
    @Override
    public float getDonacionCliente(String nombreCliente) throws RemoteException {
        String nombreServidor = identificarCliente(nombreCliente);

        if(nombreServidor.equals(this.replica)){
            InterfazServidorReplica servidorReplica = this.obtenerReplica("localhost", this.replica);
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
    // Donar en el servidor correspondiente cuando ya se ha comprobado en que servidor esta el cliente
    @Override
    public void donar(String nombreCliente, float donacion) throws RemoteException {
        String servidorCliente = identificarCliente(nombreCliente);
        if (servidorCliente.equals(this.servidor)) {
            Cliente cliente = clientesRegistrados.get(nombreCliente);
            cliente.donar(donacion);
            incrementarSubtotalDonado(donacion);
            System.out.println("El cliente " + nombreCliente + " ha donado " + donacion);
        } else if (servidorCliente.equals(this.replica)) {
            InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
            servidorReplica.donar(nombreCliente, donacion);
        }
    }


    @Override
    public float getTotalDonado() throws RemoteException {
        InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
        return this.subtotalDonado + servidorReplica.getSubtotalDonado();
    }

    private void agregarDonantesDeServidor(ArrayList<Cliente> lista, InterfazServidorReplica servidor) throws RemoteException {
        for (Cliente cliente : servidor.getClientesRegistrados().values()) {
            if (cliente.getNumDonaciones() > 0) {
                lista.add(cliente);
            }
        }
    }

    @Override
    public ArrayList<Cliente> getListaDonantes() throws RemoteException {
        ArrayList<Cliente> listaDonantes = new ArrayList<>();
        agregarDonantesDeServidor(listaDonantes, this);
        InterfazServidorReplica servidorReplica = obtenerReplica("localhost", this.replica);
        agregarDonantesDeServidor(listaDonantes, servidorReplica);
        return listaDonantes;
    }
}