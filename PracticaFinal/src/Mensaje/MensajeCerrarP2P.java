package Mensaje;

public class MensajeCerrarP2P extends Mensaje{

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CERRAR_P2P;
    }

}
