package Agentes;

import java.util.List;

import Objetos.AlmacenNuevosLibros;
import Objetos.AlmacenServidor;
import Objetos.Libro;
import Objetos.SafeSocket;

public class ConsumidorBibliotecario extends Thread {

    private AlmacenNuevosLibros almacenNuevosLibros;
    private AlmacenServidor almacenServidor;

    public ConsumidorBibliotecario(AlmacenNuevosLibros almacenNuevosLibros, AlmacenServidor almacenServidor) {
        this.almacenNuevosLibros = almacenNuevosLibros;
        this.almacenServidor = almacenServidor;
    }

    public void run() {
        while(true) {
            Libro libro = almacenNuevosLibros.extraer(); //Si no hay libros se quedara esperando
            String titulo_libro = libro.getTitulo();
            List<SafeSocket> listaSocketsEsperando = almacenServidor.registrarLibroYDevolverlistaSocketsEsperando(libro);
            for(SafeSocket s : listaSocketsEsperando){
                AvisarClienteEsperando aviso = new AvisarClienteEsperando(titulo_libro, s);
                aviso.start();
            }
        }
    }

}
