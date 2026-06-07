package Agentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Mensaje.MensajeCerrarConexion;
import Mensaje.MensajeSolicitudInfo;
import Mensaje.MensajeSolicitudLibro;
import Objetos.AlmacenCliente;
import Objetos.InterfazCliente;
import Objetos.Libro;
import Objetos.SafeSocket;

public class MenuTeclado extends Thread{

    private AlmacenCliente almacenCliente;
    private SafeSocket socket;
    private Scanner scanner;
    private InterfazCliente interfazCliente;

    public MenuTeclado(AlmacenCliente almacenCliente, SafeSocket socket, Scanner scanner, InterfazCliente interfazCliente){
        this.almacenCliente = almacenCliente;
        this.socket = socket;
        this.scanner = scanner;
        this.interfazCliente = interfazCliente;
    }

    public void run(){
        boolean salir = false;

        // Lanzar hilo para compartir informacion con otros usuarios
        int opcion_seleccionada;

        while(!salir) {
            interfazCliente.mostrarMenu();

            while(true) {
                try {
                    opcion_seleccionada = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (Exception e) {
                    interfazCliente.mostrarMensaje("Error al intentar parsear la opcion. Intentelo de nuevo");
                }
            }
            
            switch(opcion_seleccionada) {

                case 1: //MANDAMOS EL MENSAJE DE SOLICITAR LA INFORMACION
                    MensajeSolicitudInfo msjPedirInfo = new MensajeSolicitudInfo();
                    socket.writeSafe(msjPedirInfo);
                    interfazCliente.mostrarMensaje("Solicitando la informacion del sistema al servidor...");
                    break;
                case 2:
                    interfazCliente.mostrarMensaje("Introduzca el titulo del libro que desea descargar");
                    String nombreLibro = scanner.nextLine();
                    Libro miLibro  = almacenCliente.buscarLibro(nombreLibro);
                    if(miLibro == null){ //No tienes el libro
                        MensajeSolicitudLibro msjPedirLibro = new MensajeSolicitudLibro(nombreLibro, almacenCliente.getUsuario().getId());
                        socket.writeSafe(msjPedirLibro);
                        interfazCliente.mostrarMensaje("Enviando su peticion al servidor...");
                    }
                    else {
                        interfazCliente.mostrarMensaje("El libro " + nombreLibro + " ya estaba en su posesion");
                    }
                    break;
                case 3:
                    almacenCliente.mostrarLibros();
                    break;
                case 4:
                    salir = true;
                    List<Libro> listaLibros = new ArrayList<>(almacenCliente.getLibros().values());
                    MensajeCerrarConexion msjCerrarConex = new MensajeCerrarConexion(almacenCliente.getUsuario().getId(), listaLibros);
                    socket.writeSafe(msjCerrarConex);
                    interfazCliente.mostrarMensaje("El usuario " + almacenCliente.getUsuario().getId() + " comunica al servidor la salida del sistema...");
                    break;
                default:
                    interfazCliente.mostrarMensaje("Error: Opcion no valida. Intentelo de nuevo");
                    break;
                
            }

            try {
                sleep(1000); //SIMULAMOS QUE ESPERAMOa LA INFORMACION
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
