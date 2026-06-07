package Objetos;

public class AlmacenNuevosLibros {

	private InfoAlmacenNuevosLibros infoAlmacenNuevosLibros;

	public AlmacenNuevosLibros(int N) {
		this.infoAlmacenNuevosLibros = new InfoAlmacenNuevosLibros(N);
	}
	
    public void almacenar(Libro producto) {
		infoAlmacenNuevosLibros.almacenar(producto);
    }

    public Libro extraer() {
		return infoAlmacenNuevosLibros.extraer();
    }

}
