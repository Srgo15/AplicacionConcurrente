package Mensaje;


public class MensajeSolicitudInfo extends Mensaje{

    public MensajeSolicitudInfo(){}

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_SOLICITUD_INFO;
    }

}
