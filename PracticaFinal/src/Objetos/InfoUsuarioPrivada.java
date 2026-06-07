package Objetos;

import Agentes.Usuario;
import Concurrencia.ControladorLE;
import Concurrencia.Monitores.GestorLE_Locks;

public class InfoUsuarioPrivada {

    private Usuario usuario;
    private SafeSocket safeSocket;
    private ControladorLE controlador;

    public InfoUsuarioPrivada(Usuario usuario, SafeSocket socket){
        this.usuario = usuario;
        this.safeSocket = socket;
        this.controlador = new GestorLE_Locks();
    }

    public void setIDUsuario(int id){
        try{
            this.controlador.request_write();
            try {
                usuario.setID(id);
            }
            finally {
                this.controlador.release_write();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Usuario getUsuario(){
        try {
            this.controlador.request_read();
            try {
                return usuario;
            }
            finally {
                this.controlador.release_read();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SafeSocket getSafeSocket() {
        try {
            this.controlador.request_read();
            try {
                return safeSocket;
            }
            finally {
                this.controlador.release_read();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
