package Objetos;

import java.util.List;

import Agentes.Usuario;
import Concurrencia.Locks;
import Concurrencia.Cerrojos.LockTicket;

public class InterfazCliente { //SE USARA PARA ASEGURARNOS DE QUE NO SE MEZCLAN MENSAJES EN LA INTERFAZ DEL CLIENTE

    private Locks lock;

    public InterfazCliente(){
        this.lock = new LockTicket(100);
    }

    public void mostrarMensaje(String mensaje) {
        lock.lock(0);
        System.out.println(mensaje);
        System.out.flush();
        lock.unlock(0);
    }

    public void mostrarInfo(List<String> listaNombreLibros, List<String> listaPropietarios, List<Usuario> listaUsuarios){
        lock.lock(0);
        // --- TABLA DE LIBROS Y PROPIETARIOS ---
        System.out.println("=======================================================");
        System.out.println("|         TABLA DE LIBROS Y PROPIETARIOS              |");
        System.out.println("=======================================================");
        System.out.printf("| %-34s | %-14s |%n", "Nombre del Libro", "Propietario");
        System.out.println("-------------------------------------------------------");

        for (int i = 0; i < listaNombreLibros.size(); i++) {
            System.out.printf("| %-34s | %-14s |%n", listaNombreLibros.get(i), listaPropietarios.get(i));
        }
        System.out.println("======================================================\n");

        // --- TABLA DE USUARIOS ---
        System.out.println("=================================================");
        System.out.println("|               TABLA DE USUARIOS               |");
        System.out.println("=================================================");
        System.out.printf("| %-10s | %-32s |%n", "ID", "Nombre de Usuario");
        System.out.println("-------------------------------------------------");
        
        for (Usuario usuario : listaUsuarios) {
            if(usuario != null){
                System.out.printf("| %-10s | %-32s |%n", usuario.getId(), usuario.getNombre());
            }
        }
        System.out.println("=================================================\n");
        System.out.flush();
        lock.unlock(0);
    }

    public void mostrarMenu(){
        lock.lock(0);
        System.out.println("Seleccione una de las siguientes opciones: (Introduzca el numero adecuado)");
        System.out.println("1- Consultar libros disponibles en el sistema");
        System.out.println("2- Descargar algun libro del sistema");
        System.out.println("3- Consultar mis libros");
        System.out.println("4- Salir");
        System.out.flush();
        lock.unlock(0);
    }
}
