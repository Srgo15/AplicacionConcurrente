package Objetos;

import Concurrencia.ControladorPC;
import Concurrencia.Semaforos.GestorPCNario;

public class InfoAlmacenNuevosLibros {

    private int N;
	private Libro[] buf;
	private ControladorPC controlador;
	private int ini, fin;

    public InfoAlmacenNuevosLibros(int N) {
        this.N = N;
		buf = new Libro[N];
		this.controlador = new GestorPCNario(N); //Utilizamos nuestro modelo de Productor/Consumidor con Semaforos
		ini = 0;
		fin = 0;
    }

    public void almacenar(Libro producto){
        try {
			controlador.preProducir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        buf[fin] = producto;
        fin = (fin + 1) % N;
        try {
			controlador.postProducir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public Libro extraer() {
        Libro myProduct;
        try {
			controlador.preConsumir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        myProduct = buf[ini];
		ini =  (ini +1) %N;
		try {
			controlador.postConsumir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return myProduct;
    }

}
