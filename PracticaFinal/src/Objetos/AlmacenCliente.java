package Objetos;

import java.util.Map;

import Agentes.Usuario;

public class AlmacenCliente {

    private InfoUsuarioPrivada infoUsuarioPrivada;
    private InfoUsuarioLibrosYConexiones infoUsuarioLibrosYConexiones;

    public AlmacenCliente(Usuario user, SafeSocket safeSocket){
        this.infoUsuarioPrivada = new InfoUsuarioPrivada(user, safeSocket);
        this.infoUsuarioLibrosYConexiones = new InfoUsuarioLibrosYConexiones();
    }

    public void setIDUsuario(int id) {
        infoUsuarioPrivada.setIDUsuario(id);
    }

    public Usuario getUsuario(){
        return infoUsuarioPrivada.getUsuario();
    }

    public SafeSocket getSafeSocket() {
        return infoUsuarioPrivada.getSafeSocket();
    }

    public Map<String, SafeSocket> getSockets(){
        return infoUsuarioLibrosYConexiones.getSockets();
    }

    public Map<String, Libro> getLibros() {
        return infoUsuarioLibrosYConexiones.getLibros();
    }

    public void addSocket(String titulo_libro, SafeSocket socket) {
        infoUsuarioLibrosYConexiones.addSocket(titulo_libro, socket);
    }

    public void addLibro(Libro libro) {
        infoUsuarioLibrosYConexiones.addLibro(libro);
    }

    public Libro buscarLibro(String titulo) {
        return infoUsuarioLibrosYConexiones.buscarLibro(titulo);
    }

    public void mostrarLibros(){
        infoUsuarioLibrosYConexiones.mostrarLibros();
    }

    public void prestarLibro(String titulo){
        infoUsuarioLibrosYConexiones.prestarLibro(titulo);
    }

    public void deleteSocket(String titulo_libro) {
        infoUsuarioLibrosYConexiones.deleteSocket(titulo_libro);
    }

    public boolean prestandoLibro(String titulo_libro) {
        return infoUsuarioLibrosYConexiones.prestandoLibro(titulo_libro);
    }

    public void cerrarConexion(){
        infoUsuarioLibrosYConexiones.cerrarConexion();
    }

}
