package Mensaje;

import java.util.List;

import Objetos.Libro;

public class MensajeCerrarConexion extends Mensaje {

    private int id_usuario;
    private List<Libro> listaLibros;

    public MensajeCerrarConexion(int id_usuario, List<Libro> listaLibros) {
        this.id_usuario = id_usuario;
        this.listaLibros = listaLibros;
    }

    public int getIDUsuario() {
        return id_usuario;
    }

    public List<Libro> getListaLibros() {
        return listaLibros;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_CERRAR_CONEXION;
    }

}
