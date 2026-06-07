package Objetos;

import java.util.List;
import java.util.Map;

import Agentes.Usuario;

public class AlmacenServidor {

    private InfoSistema infoSistema;

    private InfoEsperando infoEsperando;

    public AlmacenServidor() {
        this.infoSistema = new InfoSistema();
        this.infoEsperando = new InfoEsperando();
    }

    
    public void addLibro(Libro libro) {
        infoSistema.addLibro(libro);
    }

    public int registrarUser(Usuario usuario, SafeSocket safeSocket){
        return infoSistema.registrarUser(usuario, safeSocket);
    }

    public List<Usuario> getUsuarios() {
        return infoSistema.getUsuarios();
    }

    public Map<String, Libro> getLibros() {
        return infoSistema.getLibros();
    }

    public InfoSolicitud recopilarInfo(){
        return infoSistema.recopilarInfo();
    }

    public Map<String, Integer> getInfoLibros() {
        return infoSistema.getInfoLibros();
    }

    public Map<Integer, SafeSocket> getSockets() {
        return infoSistema.getSockets();
    }

    public ResultadoSolicitud prestamoLibro(String titulo, int id_usuario) {
        return infoSistema.prestamoLibro(titulo, id_usuario);
    }

    public SafeSocket buscarCanal(int id) {
        return infoSistema.buscarCanal(id);
    }

    public void recuperaLibros(List<Libro> listaLibros, int id_usuario) {
        infoSistema.recuperaLibros(listaLibros, id_usuario);
    }

    public void cambiarPropietario(String titulo_libro, int id_propietario){
        infoSistema.cambiarPropietario(titulo_libro, id_propietario);
    }

    public boolean addLibroEsperando(String titulo, int id_esperando) {
        return infoEsperando.addLibroEsperando(titulo, id_esperando);
    }

    public List<SafeSocket> registrarLibroYDevolverlistaSocketsEsperando(Libro libro) {
        infoSistema.registrarLibroYDevolverlistaSocketsEsperando1(libro);
        List<Integer> list = infoEsperando.registrarLibroYDevolverlistaSocketsEsperando(libro);
        return infoSistema.registrarLibroYDevolverlistaSocketsEsperando2(list);
    }

    public String nombreUsuario(int id) {
        return infoSistema.nombreUsuario(id);
    }

}
