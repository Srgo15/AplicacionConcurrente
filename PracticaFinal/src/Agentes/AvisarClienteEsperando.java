package Agentes;

import Mensaje.MensajeLibroListo;
import Objetos.SafeSocket;

public class AvisarClienteEsperando extends Thread {

    private String titulo;
    private SafeSocket safeSocket;

    public AvisarClienteEsperando(String titulo, SafeSocket safeSocket) {
        this.titulo = titulo;
        this.safeSocket = safeSocket;
    }

    public void run() {

        try {
            MensajeLibroListo mnsj_libr_list = new MensajeLibroListo(titulo);
            safeSocket.writeSafe(mnsj_libr_list);
        }
        catch(Exception e) {
            System.out.println("El cliente ha abandonado el sistema antes de recibir el libro " + titulo);
        }

    }

}
