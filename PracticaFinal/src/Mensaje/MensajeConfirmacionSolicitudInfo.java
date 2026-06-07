package Mensaje;

import java.util.List;

import Agentes.Usuario;

public class MensajeConfirmacionSolicitudInfo extends Mensaje{

    private List<String> listaNombres;
    private List<String> listaPropietarios;
    private List<Usuario> listaUsuarios;

    public MensajeConfirmacionSolicitudInfo(List<String> listaNombres, List<String> listaPropietarios,
        List<Usuario> listaUsuarios){
        this.listaNombres = listaNombres;
        this.listaPropietarios = listaPropietarios;
        this.listaUsuarios = listaUsuarios;
    }

    public List<String> getNombreLibros(){
        return listaNombres;
    }

    public List<String> getPropietarios(){
        return listaPropietarios;
    }

    public List<Usuario> getUsuarios() {
        return listaUsuarios;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CONFIRMACION_SOLICITUD_INFO;
    }
    
}
