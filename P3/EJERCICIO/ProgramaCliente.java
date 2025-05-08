import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Scanner;
import java.util.ArrayList;

public class ProgramaCliente {
    public static void imprimirMenuPrincipal() {
        System.out.println("1--> Registrarse");
        System.out.println("2--> Iniciar sesión");
        System.out.println("3--> Salir");
    }

    public static void imprimirMenuOperaciones() {
        System.out.println("1--> Realizar una donación");
        System.out.println("2--> Consultar el total donado al servidor(Replica)");
        System.out.println("3--> Consultar el total donado por el cliente");
        System.out.println("4--> Consultar total donado en ambos servidores");
        System.out.println("5--> Consultar la lista de donantes");
        System.out.println("6--> Salir");
    }

    public static void imprimirHistorialDonaciones(String nombreCliente, ArrayList<Float> historial) {
        
        if(historial.isEmpty()) {
            System.out.println("El cliente " + nombreCliente + " no ha realizado ninguna donacion");
        }
        else {
            System.out.println("Historial de donaciones del cliente " + nombreCliente + ":");
            
            for(int i = 0; i < historial.size(); i++) {
                System.out.println("Donacion " + (i+1) + ": " + historial.get(i));
            }
        }
    }

    public static void imprimirListadoDonantes(ArrayList<Cliente> Donantes){
        if(Donantes.isEmpty()) {
            System.out.println("No hay donantes");
        }
        else {
            System.out.println("Historial de donantes: ");
            
            for(int i = 0; i < Donantes.size(); i++) {
                System.out.println("Donante " + (i+1) + ": " + Donantes.get(i).obtenerNombre() + " ha donado: " + Donantes.get(i).obtenerDonacionTotal());
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        String nombreServidor = "servidorReplica";
        String cliente = "";
        float cantidad = 0.0f;
        boolean sesionIniciada = false;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            InterfazServidorCliente servidor = (InterfazServidorCliente) mireg.lookup(nombreServidor);

            System.out.println("Bienvenido al servidor de donaciones");
            System.out.println("Seleccione una opcion: ");

            Scanner sc = new Scanner(System.in);
            int opcion = 0;
            
            do {
                imprimirMenuPrincipal();
                System.out.print("Opción: ");
                opcion = sc.nextInt();
                System.out.println();

                if(opcion < 1 || opcion > 3)
                    System.out.println("La opción seleccionada no es válida");
                
            } while (opcion < 1 || opcion > 3);

            switch (opcion) {
                case 1:
                    System.out.print("Introduzca el nombre: ");
                    cliente = sc.nextLine();
                    cliente = sc.nextLine();

                    if(servidor.registrado(cliente)) {
                        System.out.println("El nombre introducido ya está registrado, pruebe con otro");
                        System.out.println ("Intentelo de nuevo, saliendo...");
                    } else {
                        if (servidor.realizarRegistro(cliente)) {
                            System.out.println(cliente + " se ha registrado");
                            sesionIniciada = true;
                        }
                        else
                            System.out.println("No se ha podido registrar el cliente");
                    }
                    break;

                case 2:
                    System.out.print("Introduzca el nombre: ");
                    cliente = sc.nextLine();
                    cliente = sc.nextLine();

                    if (servidor.inicioSesion(cliente)) {
                        System.out.println("El cliente se ha identificado");
                        sesionIniciada = true;
                    }
                    else
                        System.out.println("No se ha podido identificar el cliente");

                    break;

                case 3:
                        System.out.println("Saliendo...");
                    break;
            }

            System.out.println();

            if (sesionIniciada) {
                while(true){
                System.out.println("\n Menú de operaciones: ");
                do {
                    imprimirMenuOperaciones();
                    System.out.print("Opción: ");
                    opcion = sc.nextInt();
                    System.out.println();
    
                    if(opcion < 1 || opcion > 6)
                        System.out.println("La opción seleccionada no es válida");
                    
                } while (opcion < 1 || opcion > 6);

                switch (opcion) {
                    case 1:
                        System.out.print("Introduzca la cantidad a donar: ");
                        cantidad = sc.nextFloat();
                        servidor.realizarDonacion(cliente, cantidad);
                        System.out.println(cliente + " ha donado " + cantidad + " al servidor");
                        break;
                    
                    case 2:
                        System.out.println("Se ha donado en total al servidor " + servidor.obtenerSubtotalDonado(cliente));
                        break;
                    
                    case 3:
                        System.out.println(cliente + " ha donado en total al servidor " + servidor.obtenerDonacionCliente(cliente));
                        break;
                    
                    case 4:
                    System.out.println("Se ha donado en total al servidor " + servidor.obtenerTotalDonadoServidores());
                        break;
                    case 5:
                        ArrayList<Cliente> listado = servidor.obtenerListeDonantes();
                        imprimirListadoDonantes(listado);
                        break;
                    case 6:
                        break;

                }
                if(opcion == 6){
                    break;
                }
                }
                
            }

        } catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
    } 
}