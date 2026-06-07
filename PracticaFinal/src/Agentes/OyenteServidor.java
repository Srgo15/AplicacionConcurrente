package Agentes;

import java.util.List;

import Mensaje.KindMensaje;
import Mensaje.Mensaje;
import Mensaje.MensajeCompartirLibro;
import Mensaje.MensajeConfirmacionConexion;
import Mensaje.MensajeConfirmacionSolicitudInfo;
import Mensaje.MensajeDarLibro;
import Mensaje.MensajeError;
import Mensaje.MensajeLibroListo;
import Mensaje.MensajePreparadoSC;
import Objetos.AlmacenCliente;
import Objetos.InterfazCliente;
import Objetos.Libro;
import Objetos.SafeSocket;

public class OyenteServidor extends Thread {

    private AlmacenCliente almacenCliente;
    private SafeSocket safeSocket;
    private InterfazCliente interfazCliente;

    public OyenteServidor(AlmacenCliente almacenCliente, SafeSocket socket, InterfazCliente interfazCliente) {
        this.safeSocket = socket;
        this.almacenCliente = almacenCliente;
        this.interfazCliente = interfazCliente;
    }
    
    public void run() {

        try {

            while(true) {
                Mensaje mensaje_recibido = safeSocket.readSafe();

                if(mensaje_recibido == null){
                    continue;
                }

                if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONFIRMACION_CONEXION) {
                    MensajeConfirmacionConexion mnsj_conf_confir = (MensajeConfirmacionConexion) mensaje_recibido;
                    
                    int id_asignado = mnsj_conf_confir.getIdAsignado();
                    almacenCliente.setIDUsuario(id_asignado);

                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONFIRMACION_SOLICITUD_INFO) {
                    MensajeConfirmacionSolicitudInfo mnsj_conf_sol_info = (MensajeConfirmacionSolicitudInfo) mensaje_recibido;
                    
                    List<String> listaNombreLibros = mnsj_conf_sol_info.getNombreLibros();
                    List<String> listaPropietarios = mnsj_conf_sol_info.getPropietarios();
                    List<Usuario> listaUsuarios = mnsj_conf_sol_info.getUsuarios();
                
                    // MOSTRAR POR PANTALLA LA INFORMACION
                    interfazCliente.mostrarInfo(listaNombreLibros, listaPropietarios, listaUsuarios);

                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_DAR_LIBRO) {
                    MensajeDarLibro mnsj_dar_libro = (MensajeDarLibro) mensaje_recibido;

                    Libro libro = mnsj_dar_libro.getLibro();

                    // Conflicto (usar monitores de LE)
                    almacenCliente.addLibro(libro);
                    // FIN conflicto
                    interfazCliente.mostrarMensaje("Se ha recibido de la biblioteca el libro solicitado: " + libro.getTitulo());

                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_COMPARTIR_LIBRO) {
                    MensajeCompartirLibro mnsj_comp_libr = (MensajeCompartirLibro) mensaje_recibido;

                    int user_destino = mnsj_comp_libr.getID();
                    String titulo = mnsj_comp_libr.getTitulo();
                    String nombre_destino = mnsj_comp_libr.getNombreDestino();

                    Libro libro_solicitado = almacenCliente.buscarLibro(titulo);

                    if(libro_solicitado != null) {
                        if(almacenCliente.prestandoLibro(libro_solicitado.getTitulo())){
                            MensajeError mensajeError = new MensajeError("El libro ya se estaba prestando a otro usuario", user_destino);
                            safeSocket.writeSafe(mensajeError);
                        }
                        else {
                            Emisor emisor = new Emisor(libro_solicitado, user_destino, almacenCliente, nombre_destino, interfazCliente);//Lanzamos el hilo que gestiona la comunicacion
                            emisor.start();
                        }
                    }
                    else {
                        int id_usuario = almacenCliente.getUsuario().getId();
                        MensajeError mnsj_error = new MensajeError("El libro no se encuentra en el sistema, lo tenía el usuario " + id_usuario, user_destino);

                        safeSocket.writeSafe(mnsj_error);
                    }

                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_PREPARADO_SC) {
                    MensajePreparadoSC mnsj_prep_sc = (MensajePreparadoSC) mensaje_recibido;

                    String ip_destino = mnsj_prep_sc.getIP();
                    int puerto_destino = mnsj_prep_sc.getPuerto();
                    String titulo_libro = mnsj_prep_sc.getTitulo();
                    String nombre_destino = mnsj_prep_sc.getNombreDestino();

                    // Establecemos conexion entre los dos clientes
                    Receptor receptor = new Receptor(almacenCliente, ip_destino, puerto_destino, titulo_libro, nombre_destino, interfazCliente);
                    receptor.start();
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONFIRMACION_CERRADO) {
                    almacenCliente.cerrarConexion();
                    safeSocket.cerrarSocket();
                    break;
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_ERROR) {
                    MensajeError mnsj_error = (MensajeError) mensaje_recibido;
                    String motivo_error = mnsj_error.getMensaje();
                    System.out.println(motivo_error);
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_LIBRO_LISTO){
                    MensajeLibroListo msj = (MensajeLibroListo) mensaje_recibido;
                    String titulo = msj.getTitulo();
                    interfazCliente.mostrarMensaje("*****ATENCION*****  El libro " + titulo + " que habias solicitado ya esta disponible");
                }
                
            }


        } catch (Exception e) {
            interfazCliente.mostrarMensaje("Error en el socket");
        }



    }

}
