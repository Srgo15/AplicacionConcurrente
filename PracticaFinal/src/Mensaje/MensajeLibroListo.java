package Mensaje;

public class MensajeLibroListo extends Mensaje {

    private String titulo;

    public MensajeLibroListo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_LIBRO_LISTO;
    }

}
