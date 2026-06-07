package Objetos;

public class ResultadoSolicitud {
    private int estado; //0->Biblioteca, 1->Otro Usuario
    private Libro libro;
    private SafeSocket safeSocket;

    public ResultadoSolicitud(Libro l){
        this.estado = 0;
        this.libro = l;
    }

    public ResultadoSolicitud(SafeSocket s){
        this.estado = 1;
        this.safeSocket = s;
    }

    public int getEstado(){
        return estado;
    }

    public Libro getLibro(){
        return libro;
    }

    public SafeSocket getSafeSocket(){
        return safeSocket;
    }
}
