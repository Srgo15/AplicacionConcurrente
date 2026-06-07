package Mensaje;


public class MensajeConfirmacionConexion extends Mensaje {

    private int id_asignado;

    public MensajeConfirmacionConexion(int id_asignado) {
        this.id_asignado = id_asignado;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CONFIRMACION_CONEXION;
    }

    public int getIdAsignado() {
        return id_asignado;
    }

}
