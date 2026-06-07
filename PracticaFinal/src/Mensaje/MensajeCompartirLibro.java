package Mensaje;

public class MensajeCompartirLibro extends Mensaje{

    private int id_destino;
    private String titulo;
    private String nombre_destino;

    public MensajeCompartirLibro(int id, String titulo, String nombre_destino){
        this.id_destino = id;
        this.titulo = titulo;
        this.nombre_destino = nombre_destino;
    }

    public int getID(){
        return id_destino;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNombreDestino() {
        return nombre_destino;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_COMPARTIR_LIBRO;
    }

}
