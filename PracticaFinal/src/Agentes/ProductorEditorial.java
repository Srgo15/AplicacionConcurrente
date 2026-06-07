package Agentes;

import Objetos.AlmacenNuevosLibros;
import Objetos.Libro;

public class ProductorEditorial extends Thread{

    private AlmacenNuevosLibros almacenNuevosLibros;
    private String titulo_libro;

    public ProductorEditorial(AlmacenNuevosLibros almacenNuevosLibros, String titulo){
        this.almacenNuevosLibros = almacenNuevosLibros;
        this.titulo_libro = titulo;
    }

    public void run(){
        Libro l = new Libro(titulo_libro, "Desconocido", null);
        try {
            sleep(5000); //Este sleep simula el tiempo que tarda en llegar el libro
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        almacenNuevosLibros.almacenar(l);
    }

}
