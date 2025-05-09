import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Scanner;
import java.util.ArrayList;

public class Cliente {

    public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void pausar() {
        System.out.println("\nPulse ENTER para continuar...");
        try {
            System.in.read(); // para capturar el salto de línea
        } catch (Exception e) {}
        limpiarPantalla();
    }

    public static void imprimirMenuPrincipal() {
        System.out.println("=======================================");
        System.out.println("    -=+=-SISTEMA DE DONACIONES-=+=-    ");
        System.out.println("=======================================");
        System.out.println("1) Registrarse");
        System.out.println("2) Iniciar sesión");
        System.out.println("3) Salir");
        System.out.println("=======================================");
    }

    public static void imprimirMenuOperaciones() {
        System.out.println("=======================================");
        System.out.println("       -=+=-MENÚ DE OPCIONES-=+=-      ");
        System.out.println("=======================================");
        System.out.println("1) Realizar una donación");
        System.out.println("2) Consultar el total donado al servidor");
        System.out.println("3) Consultar el total donado por el cliente");
        System.out.println("4) Consultar total donado en ambos servidores");
        System.out.println("5) Consultar la lista de donantes");
        System.out.println("6) Salir");
        System.out.println("=======================================");
    }

    public static void imprimirListadoDonantes(ArrayList<Entidad> Donantes){
        if(Donantes.isEmpty()) {
            System.out.println("No hay donantes registrados.");
        } else {
            System.out.println("Listado de donantes: ");
            for(int i = 0; i < Donantes.size(); i++) {
                System.out.println("Donante " + (i+1) + ": " + Donantes.get(i).getNombre() + " ha donado: " + Donantes.get(i).getTotal());
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        String nombreServidor = "replica";
        String cliente = "";
        float cantidad = 0.0f;
        int seleccion = 0;
        String nombreSeleccion = "";
        boolean sesionIniciada = false;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            Donacion_I servidor = (Donacion_I) mireg.lookup(nombreServidor);

            Scanner sc = new Scanner(System.in);
            int opcion = 0;

            limpiarPantalla();
            System.out.println("Bienvenido al sistema de donaciones");

            do {
                imprimirMenuPrincipal();
                System.out.print("Opción: ");
                opcion = sc.nextInt();
                System.out.println();

                if(opcion < 1 || opcion > 3) {
                    System.out.println("La opción seleccionada no es válida");
                    pausar();
                    limpiarPantalla();
                }

            } while (opcion < 1 || opcion > 3);

            limpiarPantalla();

            switch (opcion) {
                case 1:
                    System.out.println("Seleccione el servidor al que desea registrarse:");
                    System.out.println("1) Servidor Principal");
                    System.out.println("2) Servidor Replica");
                    seleccion = sc.nextInt();
                    if (seleccion == 1) {
                        nombreSeleccion = "servidor";
                    } else if (seleccion == 2) {
                        nombreSeleccion = "replica";
                    } else {
                        System.out.println("Opción no válida. Saliendo...");
                        return;
                    }

                    System.out.print("Introduzca el nombre: ");
                    cliente = sc.nextLine();
                    cliente = sc.nextLine();
                    if(servidor.registrado(cliente)) {
                        System.out.println("El nombre introducido ya está registrado.");
                    } else if (servidor.registrar(cliente, nombreSeleccion)) {
                        System.out.println(cliente + " se ha registrado correctamente.");
                        sesionIniciada = true;
                    } else {
                        System.out.println("No se ha podido registrar el cliente.");
                    }
                    pausar();
                    break;

                case 2:
                    System.out.print("Introduzca el nombre: ");
                    cliente = sc.nextLine();
                    cliente = sc.nextLine();

                    if (servidor.iniciarSesion(cliente)) {
                        System.out.println("Sesión iniciada correctamente.");
                        sesionIniciada = true;
                    } else {
                        System.out.println("No se ha podido identificar al cliente.");
                    }
                    pausar();
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    return;
            }

            limpiarPantalla();

            while (sesionIniciada) {
                System.out.println("Bienvenido, " + cliente);
                imprimirMenuOperaciones();

                do {
                    System.out.print("Opción: ");
                    opcion = sc.nextInt();
                    System.out.println();

                    if(opcion < 1 || opcion > 6) {
                        System.out.println("La opción seleccionada no es válida");
                        pausar();
                        limpiarPantalla();
                        imprimirMenuOperaciones();
                    }
                } while (opcion < 1 || opcion > 6);

                limpiarPantalla();

                switch (opcion) {
                    case 1:
                        System.out.print("Introduzca la cantidad a donar: ");
                        cantidad = sc.nextFloat();
                        if (cantidad > 0) {
                            servidor.donar(cliente, cantidad);
                            System.out.println(cliente + " ha donado " + cantidad + " al servidor.");
                            pausar();
                            break;
                        }
                        else {
                            System.out.println("La cantidad a donar debe ser mayor que 0.");
                            pausar();
                            break;
                        }

                    case 2:
                        System.out.println("Total donado al servidor: " + servidor.getSubtotalDonado(cliente));
                        pausar();
                        break;

                    case 3:
                        System.out.println("Total donado por " + cliente + ": " + servidor.getDonacionCliente(cliente));
                        pausar();
                        break;

                    case 4:
                        System.out.println("Total donado entre ambos servidores: " + servidor.getTotalDonado());
                        pausar();
                        break;

                    case 5:
                        ArrayList<Entidad> listado = servidor.getListaDonantes();
                        imprimirListadoDonantes(listado);
                        pausar();
                        break;

                    case 6:
                        System.out.println("Cerrando sesión...");
                        pausar();
                        return;
                }

                limpiarPantalla();
            }

        } catch(NotBoundException | RemoteException e) {
            System.err.println("Excepción del sistema: " + e);
        }
    } 
}
