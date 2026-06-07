package Objetos;

import java.util.List;

import Agentes.Usuario;

public class InfoSolicitud {

    private List<String> titulosLibros;
    private List<String> propietariosLibros;
    private List<Usuario> listaUsuarios;

    public InfoSolicitud(List<String> titulosLibros, List<String> propietariosLibros, List<Usuario> listaUsuarios){
        this.titulosLibros = titulosLibros;
        this.propietariosLibros = propietariosLibros;
        this.listaUsuarios = listaUsuarios;
    }
    
    public List<String> getTitulosLibros(){
        return titulosLibros;
    }

    public List<String> getPropietarios(){
        return propietariosLibros;
    }

    public List<Usuario> getUsuarios(){
        return listaUsuarios;
    }

}
