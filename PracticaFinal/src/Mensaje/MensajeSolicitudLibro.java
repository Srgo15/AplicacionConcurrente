package Mensaje;

public class MensajeSolicitudLibro extends Mensaje{

    private String titulo;
    private int id_usuario;

    public MensajeSolicitudLibro(String titulo, int id){
        this.titulo = titulo;
        this.id_usuario = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getID(){
        return id_usuario;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_SOLICITUD_LIBRO;
    }

}
