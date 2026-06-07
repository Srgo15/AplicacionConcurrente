package Objetos;

import java.util.HashMap;
import java.util.Map;

import Concurrencia.ControladorLE;
import Concurrencia.Monitores.GestorLE_Locks;
import Mensaje.MensajeCerrarP2P;

public class InfoUsuarioLibrosYConexiones {

    private Map<String, SafeSocket> tSockets;
    private Map<String, Libro> tLibros;
    private ControladorLE controlador;

    public InfoUsuarioLibrosYConexiones(){
        this.tSockets = new HashMap<>();
        this.tLibros = new HashMap<>();
        this.controlador = new GestorLE_Locks(); // Usamos Monitores
    }

    public Map<String, SafeSocket> getSockets(){
        try {
            this.controlador.request_read();
            try {
                return new HashMap<>(this.tSockets);
            }
            finally {
                this.controlador.release_read();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void addSocket(String titulo_libro, SafeSocket socket) {
        try {
            this.controlador.request_write();
            try {
                if(!this.tSockets.containsKey(titulo_libro)) {
                    this.tSockets.put(titulo_libro, socket);
                }
            }
            finally {
                this.controlador.release_write();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Libro> getLibros() {
        try {
            controlador.request_read();
            try {
                return new HashMap<>(this.tLibros);
            }
            finally {
                controlador.release_read();
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void addLibro(Libro libro) {
        try{
            this.controlador.request_write();
            try {
                String titulo = libro.getTitulo();
                this.tLibros.put(titulo, libro);
            }
            finally {
                this.controlador.release_write();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Libro buscarLibro(String titulo) {
        try {
            controlador.request_read();
            try {
                if(tLibros.containsKey(titulo)){
                    return tLibros.get(titulo);
                }
                return null;
            }
            finally {
                controlador.release_read();
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void mostrarLibros(){
        try {
            controlador.request_read();
            try {
                for(Libro l : tLibros.values()){
                    System.out.println(l.toString());
                }
            }
            finally {
                controlador.release_read();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void prestarLibro(String titulo){
        try{
            this.controlador.request_write();
            try {
                tLibros.remove(titulo);
            }
            finally {
                this.controlador.release_write();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteSocket(String titulo_libro) {
        try{
            this.controlador.request_write();
            try {
                tSockets.remove(titulo_libro);
            }
            finally {
                this.controlador.release_write();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean prestandoLibro(String titulo_libro) {
        try {
            controlador.request_read();
            try {
                if(tSockets.containsKey(titulo_libro)){
                    return true;
                }
                else {
                    return false;
                }
            }
            finally {
                controlador.release_read();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void cerrarConexion(){
        try{
            this.controlador.request_write();
            try {
                tLibros = new HashMap<>(); //Vaciamos nuestros libros que han sido devueltos a la biblioteca
                for(String s: tSockets.keySet()){
                    SafeSocket sock = tSockets.get(s);
                    MensajeCerrarP2P mensajeCerrarP2P = new MensajeCerrarP2P();
                    sock.writeSafe(mensajeCerrarP2P);
                }
                tSockets.clear();
            }
            finally {
                this.controlador.release_write();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
