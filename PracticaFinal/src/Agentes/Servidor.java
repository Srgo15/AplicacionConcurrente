package Agentes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;

import Objetos.AlmacenNuevosLibros;
import Objetos.AlmacenServidor;
import Objetos.Libro;

public class Servidor {

    private static final int MAX_LIBROS = 5;

    public static void cargarLibros(AlmacenServidor almacenServidor, String archivo) {
        // Los libros que carguemos tendran el Usuario a null

        String separador = ";";
        String linea;

        try {

            BufferedReader br = new BufferedReader(new FileReader(archivo));

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(separador);

                if (datos.length == 2) {
                    String titulo = datos[0].trim();
                    String autor = datos[1].trim();

                    Libro libro = new Libro(titulo, autor, null);

                    almacenServidor.addLibro(libro);
                }
            }

            br.close();
            System.out.println("Los libros se cargaron correctamente");

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en la lectura de los libros");
        }

    }

    public static void main(String[] args) {

        String dirIP = "localhost";
        int puerto = 9999;
        String archivoLibros = "";
        AlmacenServidor almacenServidor = new AlmacenServidor();

        if(args.length  == 1) {
            try {
                puerto = Integer.parseInt(args[1]);
            }
            catch (Exception e) {
                archivoLibros = args[0];
                cargarLibros(almacenServidor, archivoLibros);
            }
        }
        else if (args.length == 2) {

            archivoLibros = args[0];
            cargarLibros(almacenServidor, archivoLibros);

            try {
                puerto = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.println("Error al parsear los argumentos");
            }
        }
        
        try { // Abrimos un socket UDP (DatagramSocket) temporal
            try (java.net.DatagramSocket socket = new java.net.DatagramSocket()) {
            // Hacemos un amago de conexión a una IP externa (Google DNS)
            socket.connect(java.net.InetAddress.getByName("8.8.8.8"), 10002);
        
            //El SO nos dice qué IP local ha elegido para esa ruta
            dirIP = socket.getLocalAddress().getHostAddress();
            System.out.println("El servidor se ha inicializado en la direccion IP real: " + dirIP + " con el puerto " + puerto);
            }
        } catch (Exception e) {
            System.out.println("No se pudo detectar la IP de forma automática.");
            e.printStackTrace();
        }

        /* OPCION 2: SIRVE SI NO SE PUEDE INTENTAR CONECTAR A INTERNET
        try {
            InetAddress direccionLocal = InetAddress.getLocalHost();
            dirIP = direccionLocal.getHostAddress(); //Esto nos devuelve la IP de la maquina virtual
            System.out.println("El servidor se ha inicializado en la direccion IP: " + dirIP + " con el puerto " + puerto);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        */

        AlmacenNuevosLibros almacenNuevosLibros = new AlmacenNuevosLibros(MAX_LIBROS);
        ServerSocket ss = null;

        try {
            ss = new ServerSocket(puerto);
            System.out.println("El servidor ha sido iniciado");
            System.out.flush();

            //Ponemos a trabajar a nuestro bibliotecario
            ConsumidorBibliotecario consumidorBibliotecario = new ConsumidorBibliotecario(almacenNuevosLibros, almacenServidor);
            consumidorBibliotecario.start();
            while (true) {

                Socket socket = ss.accept();
                System.out.println("Nuevo cliente conectado");

                // Traspasar al oyente cliente
                OyenteCliente oyenteCliente = new OyenteCliente(socket, almacenServidor, almacenNuevosLibros);
                oyenteCliente.start();

            }

        } catch (Exception e) {
            System.err.println("Ha ocurrido un error al iniciar el servidor");
        }

    }

}

/*CODIGO ALTERNATIVO SI QUISIERAMOS QUE SALGA LA IP CORRECTA
try {
    // Abrimos un socket UDP (DatagramSocket) temporal
    try (java.net.DatagramSocket socket = new java.net.DatagramSocket()) {
        // Hacemos un amago de conexión a una IP externa (Google DNS)
        socket.connect(java.net.InetAddress.getByName("8.8.8.8"), 10002);
        
        //El SO nos dice qué IP local ha elegido para esa ruta
        String dirIP = socket.getLocalAddress().getHostAddress();
        
        System.out.println("El servidor se ha inicializado en la direccion IP real: " + dirIP + " con el puerto " + puerto);
    }
} catch (Exception e) {
    System.out.println("No se pudo detectar la IP de forma automática.");
    e.printStackTrace();
}

*/