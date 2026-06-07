package Mensaje;

import Agentes.Usuario;

public class MensajeConexion extends Mensaje{

    private Usuario user;

    public MensajeConexion(Usuario user) {
        this.user = user;
    }

    public Usuario getUsuario(){
        return user;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CONEXION;
    }

}
