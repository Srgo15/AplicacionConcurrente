package Agentes;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import Mensaje.MensajeConexion;
import Objetos.AlmacenCliente;
import Objetos.InterfazCliente;
import Objetos.SafeSocket;

public class Cliente {

    public static void iniciar_conexion(String nombre, String ip, String ipServidor, int num_puerto, Scanner scanner) {
        Usuario user = new Usuario(nombre, ip);
        try {
            Socket socket = new Socket(ipServidor, num_puerto);
            SafeSocket safeSocket = new SafeSocket(socket);
            AlmacenCliente almacenCliente = new AlmacenCliente(user, safeSocket);
            InterfazCliente interfazCliente = new InterfazCliente();

            // LANZAMOS EL HILO CORRESPONDIENTE AL OYENTE SERVIDOR
            OyenteServidor hiloOyenteServidor = new OyenteServidor(almacenCliente, safeSocket, interfazCliente);
            hiloOyenteServidor.start();

            // MANDAMOS EL MENSAJE DE CONEXION
            MensajeConexion msj = new MensajeConexion(almacenCliente.getUsuario());
            safeSocket.writeSafe(msj);;

            // LANZAMOS EL HILO CORRESPONDIENTE AL MENU INTERACTIVO CON EL TECLADO
            MenuTeclado menu = new MenuTeclado(almacenCliente, safeSocket, scanner, interfazCliente);
            menu.start();
            menu.join();

            hiloOyenteServidor.join();//ESPERAMOS A QUE NOS LLEGUE EL MENSAJE DE CONFIRMACION Y SE CIERREN LOS SOCKETS
        } catch (Exception e) {
            System.err.println("Error al crear el socket.");
        }
    }

    public static void main(String[] args) { // Recibiremos la IP por los args

        System.out.println("---BIENVENIDO A LA BIBLIOTECA DIGITAL---");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduzca su nombre de usuario:");

        String nombre_usuario = scanner.nextLine();
        String dirIP = "";

        try {
            // Obtenemos la direccion IP
            InetAddress direccionLocal = InetAddress.getLocalHost();
            dirIP = direccionLocal.getHostAddress();
        } catch (Exception e) {
            System.err.println("Error al obtener la direccion IP");
        }

        String dirIPServidor = "localhost";
        String puerto = "9999";
        if (args.length > 1) { // NOS PASAN LA IP DEL SERVIDOR Y EL NUMERO DE PUERTO
            dirIPServidor = args[0];
            puerto = args[1];
        }

        int num_puerto = Integer.parseInt(puerto);
        iniciar_conexion(nombre_usuario, dirIP, dirIPServidor, num_puerto, scanner);

        System.out.println("El cliente " + nombre_usuario + " abandona el sistema");
    }

}
