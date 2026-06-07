package Mensaje;

import Objetos.Libro;

public class MensajeDarLibro extends Mensaje{

    private Libro libro;

    public MensajeDarLibro(Libro libro){
        this.libro = libro;
    }

    public Libro getLibro(){
        return libro;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_DAR_LIBRO;
    }

}
