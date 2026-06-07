package Agentes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Mensaje.KindMensaje;
import Mensaje.Mensaje;
import Mensaje.MensajeConexion;
import Mensaje.MensajeConfirmacionCerradoP2P;
import Mensaje.MensajeConfirmacionConexion;
import Mensaje.MensajeDarLibro;
import Mensaje.MensajeError;
import Mensaje.MensajePreparadoCS;
import Mensaje.MensajeSolicitudLibro;
import Objetos.AlmacenCliente;
import Objetos.InterfazCliente;
import Objetos.Libro;
import Objetos.SafeSocket;

public class Emisor extends Thread{
    private Libro libro;
    private int id_destino;
    private AlmacenCliente almacenCliente;
    private String nombre_destino;
    private InterfazCliente interfazCliente;

    public Emisor(Libro libro, int id, AlmacenCliente almacenCliente, String nombre_destino, InterfazCliente interfazCliente){
        this.libro = libro;
        this.id_destino = id;
        this.almacenCliente = almacenCliente;
        this.nombre_destino = nombre_destino;
        this.interfazCliente = interfazCliente;
    }

    public void run(){
        try {
            ServerSocket serverP2P = new ServerSocket(0); // Usamos el 0 para que nos de el siguiente puerto disponible
            int puerto_asignado = serverP2P.getLocalPort();
            String ip = almacenCliente.getUsuario().getIp();

            MensajePreparadoCS mnsj_prep_cs = new MensajePreparadoCS(puerto_asignado, ip, almacenCliente.getUsuario().getId(), libro.getTitulo(), id_destino);
            almacenCliente.getSafeSocket().writeSafe(mnsj_prep_cs);
            
            Socket s = serverP2P.accept(); //Usamos un socket normal ya que solo escribira este hilo
            SafeSocket socket = new SafeSocket(s);
            almacenCliente.addSocket(libro.getTitulo(), socket);
            while(true){
                Mensaje mensaje_recibido = (Mensaje) socket.readSafe();

                if(mensaje_recibido == null){
                    continue;
                }

                if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONEXION) {
                    MensajeConexion msj = (MensajeConexion) mensaje_recibido;
                    if(msj.getUsuario().getId() == id_destino) {
                        MensajeConfirmacionConexion mnsjConfConexion = new MensajeConfirmacionConexion(id_destino);
                        socket.writeSafe(mnsjConfConexion);
                    }
                    else {
                        MensajeError mnsjError = new MensajeError("El libro habia sido solicitado por un usario distinto: " + id_destino, id_destino);
                        socket.writeSafe(mnsjError);
                    }
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_SOLICITUD_LIBRO){
                    MensajeSolicitudLibro msj = (MensajeSolicitudLibro) mensaje_recibido;
                    if(msj.getTitulo().equals(libro.getTitulo())){
                        Libro l = almacenCliente.buscarLibro(libro.getTitulo()); //Comprobamos que seguimos teniendo el libro
                        if(l != null) {
                            almacenCliente.prestarLibro(l.getTitulo());
                            interfazCliente.mostrarMensaje("Se ha prestado el libro " + l.getTitulo() + " al usuario " + nombre_destino);
                            MensajeDarLibro mnsjDarLibro = new MensajeDarLibro(l);
                            socket.writeSafe(mnsjDarLibro);
                        }
                        else {
                            MensajeError mnsjError = new MensajeError("El libro ya no lo tiene este usuario" , id_destino);
                            socket.writeSafe(mnsjError);
                        }
                    }
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CERRAR_P2P){
                    MensajeConfirmacionCerradoP2P msj = new MensajeConfirmacionCerradoP2P();
                    socket.writeSafe(msj);
                    almacenCliente.deleteSocket(libro.getTitulo());
                    socket.cerrarSocket();
                    break;
                }
            }
            serverP2P.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
}
