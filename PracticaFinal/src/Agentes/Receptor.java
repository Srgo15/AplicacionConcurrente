package Agentes;

import java.net.Socket;

import Mensaje.KindMensaje;
import Mensaje.Mensaje;
import Mensaje.MensajeCerrarP2P;
import Mensaje.MensajeConexion;
import Mensaje.MensajeDarLibro;
import Mensaje.MensajeError;
import Mensaje.MensajeNuevoPropietario;
import Mensaje.MensajeSolicitudLibro;
import Objetos.AlmacenCliente;
import Objetos.InterfazCliente;
import Objetos.Libro;
import Objetos.SafeSocket;

public class Receptor extends Thread{

    private AlmacenCliente almacenCliente;
    private String ip_emisor;
    private int num_puerto;
    private String titulo_libro;
    private String nombre_destino;
    private InterfazCliente interfazCliente;

    public Receptor(AlmacenCliente almacenCliente, String ip_emisor, int num_puerto, String titulo_libro, String nombre_destino, InterfazCliente interfazCliente){
        this.almacenCliente = almacenCliente;
        this.ip_emisor = ip_emisor;
        this.num_puerto = num_puerto;
        this.titulo_libro = titulo_libro;
        this.nombre_destino = nombre_destino;
        this.interfazCliente = interfazCliente;
    }

    public void run(){
        try {
            Socket s = new Socket(ip_emisor, num_puerto);
            SafeSocket socket_p2p = new SafeSocket(s);
            almacenCliente.addSocket(titulo_libro, socket_p2p);
            int id_user = almacenCliente.getUsuario().getId();
            MensajeConexion mensajeConexion = new MensajeConexion(almacenCliente.getUsuario());
            socket_p2p.writeSafe(mensajeConexion);
            while(true){
                Mensaje mensaje_recibido = (Mensaje) socket_p2p.readSafe();

                if(mensaje_recibido == null){
                    continue;
                }

                if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONFIRMACION_CONEXION){
                    MensajeSolicitudLibro mnsj_SolicitudLibro = new MensajeSolicitudLibro(titulo_libro, id_user);
                    socket_p2p.writeSafe(mnsj_SolicitudLibro);
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_DAR_LIBRO){
                    MensajeDarLibro msj = (MensajeDarLibro) mensaje_recibido;
                    Libro libro = msj.getLibro();
                    almacenCliente.addLibro(libro);
                    MensajeNuevoPropietario mnsjNuevoPropietario = new MensajeNuevoPropietario(titulo_libro, id_user);
                    almacenCliente.getSafeSocket().writeSafe(mnsjNuevoPropietario); //Avisamos al servidor de que ha cambiado el propietario

                    interfazCliente.mostrarMensaje("Se ha recibido el libro " + libro.getTitulo() + " del usuario " + nombre_destino);

                    MensajeCerrarP2P mnsjCerrarP2P = new MensajeCerrarP2P();
                    socket_p2p.writeSafe(mnsjCerrarP2P); //Hemos recibido el libro asi que pedimos cerrar la conexion
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONFIRMACION_CERRADO_P2P){
                    almacenCliente.deleteSocket(titulo_libro);
                    socket_p2p.cerrarSocket();
                    break;
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_ERROR){
                    MensajeError mnsj_error = (MensajeError) mensaje_recibido;
                    String motivo_error = mnsj_error.getMensaje();
                    System.out.println(motivo_error);
                    MensajeCerrarP2P mnsjCerrarP2P = new MensajeCerrarP2P(); //Solicitamos el cerrado de la conexion
                    socket_p2p.writeSafe(mnsjCerrarP2P);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
