package Mensaje;

public class MensajeConfirmacionCerrado extends Mensaje{

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CONFIRMACION_CERRADO;
    }

}
