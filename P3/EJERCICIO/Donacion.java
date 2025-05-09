import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Donacion extends UnicastRemoteObject implements Donacion_I {

    private String servidorPrincipal;
    private String nombreReplica;
    private float totalLocalDonado;
    private Map<String, Entidad> baseClientes;

    public Donacion(String servidorPrincipal, String nombreReplica) throws RemoteException {
        this.servidorPrincipal = servidorPrincipal;
        this.nombreReplica = nombreReplica;
        this.totalLocalDonado = 0.0f;
        this.baseClientes = new HashMap<>();
    }

    // =============================
    // -=+=-Gestión de Réplicas-=+=-
    // =============================

    private Donacion_I getServidorRemoto(String host, String nombreServidor) throws RemoteException {
        try {
            Registry registro = LocateRegistry.getRegistry(host, 1099);
            return (Donacion_I) registro.lookup(nombreServidor);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("No se pudo obtener la referencia del servidor remoto: " + nombreServidor);
            return null;
        }
    }

    @Override
    public Donacion_I getReplica(String host, String nombreReplica) throws RemoteException {
        return getServidorRemoto(host, nombreReplica);
    }


    // =============================
    // -=+=-Gestión de Clientes-=+=-
    // =============================

    @Override
    public boolean existe(String cliente) throws RemoteException {
        return baseClientes.containsKey(cliente);
    }

    @Override
    public boolean registrado(String cliente) throws RemoteException {
        Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
        return this.existe(cliente) || (replica != null && replica.existe(cliente));
    }

    @Override
    public boolean registrar(String cliente, String destinoServidor) throws RemoteException {
        if (existe(cliente)) return false;

        Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
        if (replica != null && replica.existe(cliente)) return false;

        if (destinoServidor.equals(servidorPrincipal)) {
            confirmarRegistro(cliente, servidorPrincipal);
        } else if (destinoServidor.equals(nombreReplica)) {
            if (replica != null) {
                replica.confirmarRegistro(cliente, nombreReplica);
            }
        } else {
            System.out.println("Servidor destino inválido.");
            return false;
        }

        return true;
    }

    @Override
    public void confirmarRegistro(String cliente, String nombreServidor) throws RemoteException {
        baseClientes.put(cliente, new Entidad(cliente));
        System.out.println("Cliente '" + cliente + "' registrado en: " + nombreServidor);
    }

    @Override
    public Boolean iniciarSesion(String cliente) throws RemoteException {
        String origen = ubicarServidorCliente(cliente);
        if (origen.equals(servidorPrincipal)) {
            return existe(cliente);
        } else if (origen.equals(nombreReplica)) {
            Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
            return replica != null && replica.existe(cliente);
        }
        return false;
    }

    @Override
    public String identificarCliente(String cliente) throws RemoteException {
        return ubicarServidorCliente(cliente);
    }

    private String ubicarServidorCliente(String cliente) throws RemoteException {
        if (existe(cliente)) return servidorPrincipal;

        Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
        if (replica != null && replica.existe(cliente)) return nombreReplica;

        return "";
    }

    @Override
    public int getNumClientesRegistrados() throws RemoteException {
        return baseClientes.size();
    }

    @Override
    public Map<String, Entidad> getClientesRegistrados() {
        return baseClientes;
    }


    // ===============================
    // -=+=-Gestión de Donaciones-=+=-
    // ===============================

    @Override
    public void donar(String cliente, float monto) throws RemoteException {
        String ubicacion = ubicarServidorCliente(cliente);
        if (ubicacion.equals(servidorPrincipal)) {
            Entidad c = baseClientes.get(cliente);
            if (c != null) {
                c.donar(monto);
                sumarDonacionLocal(monto);
                System.out.println("Cliente '" + cliente + "' donó: " + monto);
            }
        } else if (ubicacion.equals(nombreReplica)) {
            Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
            if (replica != null) {
                replica.donar(cliente, monto);
            }
        }
    }

    @Override
    public void incrementarSubtotalDonado(float monto) throws RemoteException {
        sumarDonacionLocal(monto);
    }

    private void sumarDonacionLocal(float monto) {
        totalLocalDonado += monto;
    }

    @Override
    public float getSubtotalDonado() throws RemoteException {
        return totalLocalDonado;
    }

    @Override
    public float getDonacionCliente(String cliente) throws RemoteException {
        String origen = ubicarServidorCliente(cliente);

        if (origen.equals(nombreReplica)) {
            Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
            if (replica != null) {
                Entidad c = replica.getClientesRegistrados().get(cliente);
                return c != null ? c.getTotal() : 0;
            }
        } else if (origen.equals(servidorPrincipal)) {
            Entidad c = baseClientes.get(cliente);
            return c != null ? c.getTotal() : 0;
        }

        return 0;
    }

    @Override
    public float getSubtotalDonado(String cliente) throws RemoteException {
        String origen = ubicarServidorCliente(cliente);
        if (origen.equals(servidorPrincipal)) {
            return getSubtotalDonado();
        } else if (origen.equals(nombreReplica)) {
            Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
            return (replica != null) ? replica.getSubtotalDonado() : 0.0f;
        }
        return 0.0f;
    }

    @Override
    public float getTotalDonado() throws RemoteException {
        Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
        float totalReplica = (replica != null) ? replica.getSubtotalDonado() : 0.0f;
        return totalLocalDonado + totalReplica;
    }

    @Override
    public ArrayList<Entidad> getListaDonantes() throws RemoteException {
        ArrayList<Entidad> donantes = new ArrayList<>();

        for (Entidad c : baseClientes.values()) {
            if (c.getNumDonaciones() > 0) {
                donantes.add(c);
            }
        }

        Donacion_I replica = getServidorRemoto("localhost", nombreReplica);
        if (replica != null) {
            for (Entidad c : replica.getClientesRegistrados().values()) {
                if (c.getNumDonaciones() > 0) {
                    donantes.add(c);
                }
            }
        }

        return donantes;
    }
}
