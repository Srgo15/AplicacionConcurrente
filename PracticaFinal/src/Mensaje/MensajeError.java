package Mensaje;

public class MensajeError extends Mensaje {

    private String mensaje_error;
    private int id_destino; // opcional

    public MensajeError(String mensaje_error, int id_destino) {
        this.mensaje_error = mensaje_error;
        this.id_destino = id_destino;
    }

    public String getMensaje() {
        return mensaje_error;
    }

    public int getID_Destino(){
        return id_destino;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_ERROR;
    }

}
