package Mensaje;

public class MensajeConfirmacionCerradoP2P extends Mensaje{

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CONFIRMACION_CERRADO_P2P;
    }

}
