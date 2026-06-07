package Agentes;

import java.net.Socket;
import java.util.List;

import Mensaje.KindMensaje;
import Mensaje.Mensaje;
import Mensaje.MensajeCerrarConexion;
import Mensaje.MensajeCompartirLibro;
import Mensaje.MensajeConexion;
import Mensaje.MensajeConfirmacionCerrado;
import Mensaje.MensajeConfirmacionConexion;
import Mensaje.MensajeConfirmacionSolicitudInfo;
import Mensaje.MensajeDarLibro;
import Mensaje.MensajeError;
import Mensaje.MensajeNuevoPropietario;
import Mensaje.MensajePreparadoCS;
import Mensaje.MensajePreparadoSC;
import Mensaje.MensajeSolicitudLibro;
import Objetos.AlmacenNuevosLibros;
import Objetos.AlmacenServidor;
import Objetos.InfoSolicitud;
import Objetos.Libro;
import Objetos.ResultadoSolicitud;
import Objetos.SafeSocket;

public class OyenteCliente extends Thread {

    private SafeSocket safeSocket;
    private AlmacenServidor almacenServidor;
    private AlmacenNuevosLibros almacenNuevosLibros;

    public OyenteCliente(Socket socket_cliente, AlmacenServidor almacenServidor, AlmacenNuevosLibros almacenNuevosLibros) {
        this.safeSocket = new SafeSocket(socket_cliente);
        this.almacenServidor = almacenServidor;
        this.almacenNuevosLibros = almacenNuevosLibros;
    }

    @Override
    public void run() {

        try {

            while (true) {
                Mensaje mensaje_recibido = safeSocket.readSafe();

                if(mensaje_recibido == null){
                    continue;
                }

                if (mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CONEXION) {

                    MensajeConexion mnsj = (MensajeConexion) mensaje_recibido;

                    Usuario user = mnsj.getUsuario();

                    int id_asignado = almacenServidor.registrarUser(user, safeSocket);

                    MensajeConfirmacionConexion msj_conf_con = new MensajeConfirmacionConexion(id_asignado);
                    safeSocket.writeSafe(msj_conf_con);
                } 
                else if (mensaje_recibido.getTipo() == KindMensaje.MENSAJE_SOLICITUD_INFO) {
                    InfoSolicitud info = almacenServidor.recopilarInfo();
                    MensajeConfirmacionSolicitudInfo msj_conf_sol_info = new MensajeConfirmacionSolicitudInfo(
                            info.getTitulosLibros(), info.getPropietarios(), info.getUsuarios());

                    safeSocket.writeSafe(msj_conf_sol_info);
                } 
                else if (mensaje_recibido.getTipo() == KindMensaje.MENSAJE_SOLICITUD_LIBRO) {
                    MensajeSolicitudLibro mnsj = (MensajeSolicitudLibro) mensaje_recibido;
                    String titulo = mnsj.getTitulo();
                    int id_user = mnsj.getID();

                    ResultadoSolicitud res = almacenServidor.prestamoLibro(titulo, id_user);
                    if(res == null) {
                        MensajeError msj_error = new MensajeError("El libro no esta en el sistema. Se ha solicitado a la editorial. Se le avisara cuando este disponible", -1);
                        boolean producir = almacenServidor.addLibroEsperando(titulo, id_user);
                        safeSocket.writeSafe(msj_error);
                        if(producir){
                            ProductorEditorial productorEditorial = new ProductorEditorial(almacenNuevosLibros, titulo);
                            productorEditorial.start();
                        }
                    }
                    else {
                        switch (res.getEstado()) {
                            case 0: //Esta en la biblioteca
                                MensajeDarLibro msj_DarLibro = new MensajeDarLibro(res.getLibro());
                                safeSocket.writeSafe(msj_DarLibro);
                                break;
                            case 1: //Lo tiene otro usuario
                                MensajeCompartirLibro msj_CompartirLibro = new MensajeCompartirLibro(id_user, titulo, almacenServidor.nombreUsuario(id_user));
                                res.getSafeSocket().writeSafe(msj_CompartirLibro);
                                break;
                            default:
                                break;
                        }
                    }
                } 
                else if (mensaje_recibido.getTipo() == KindMensaje.MENSAJE_PREPARADO_CS) {
                    MensajePreparadoCS mnsj = (MensajePreparadoCS) mensaje_recibido;
                    int num_puerto = mnsj.getPuerto();
                    String ip = mnsj.getIP();
                    int id = mnsj.getID();
                    int id_destino = mnsj.getID_Destino();

                    SafeSocket safeSocketDestino = almacenServidor.buscarCanal(id_destino);
                    MensajePreparadoSC msj_PreparadoSC = new MensajePreparadoSC(num_puerto, ip, mnsj.getTitulo(), almacenServidor.nombreUsuario(id));
                    if (safeSocketDestino != null) {
                        safeSocketDestino.writeSafe(msj_PreparadoSC);
                    }
                } 
                else if (mensaje_recibido.getTipo() == KindMensaje.MENSAJE_CERRAR_CONEXION) {
                    MensajeCerrarConexion mnsj = (MensajeCerrarConexion) mensaje_recibido;

                    int id_usuario = mnsj.getIDUsuario();
                    List<Libro> listaLibros = mnsj.getListaLibros();

                    almacenServidor.recuperaLibros(listaLibros, id_usuario);

                    MensajeConfirmacionCerrado msj_cerrado = new MensajeConfirmacionCerrado();
                    safeSocket.writeSafe(msj_cerrado);
                    safeSocket.cerrarSocket();
                    System.out.println("El usuario " + id_usuario + " abandona el sistema");
                    System.out.flush();
                    break;
                }
                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_ERROR) {
                    MensajeError mnsj_err = (MensajeError) mensaje_recibido;

                    String mensaje = mnsj_err.getMensaje();
                    int id_destino = mnsj_err.getID_Destino();

                    // Obtener el socket del cliente destino
                    SafeSocket socketDestino = almacenServidor.buscarCanal(id_destino);

                    // Creamos el mensaje
                    MensajeError mnsj_err_enviar = new MensajeError(mensaje, -1);

                    // Enviamos el mensaje de error al usuario que pidio el libro
                    socketDestino.writeSafe(mnsj_err_enviar);

                }

                else if(mensaje_recibido.getTipo() == KindMensaje.MENSAJE_NUEVO_PROPIETARIO){
                    MensajeNuevoPropietario msj = (MensajeNuevoPropietario) mensaje_recibido;
                    int id_propietario = msj.getPropietario();
                    String titulo_libro = msj.getTitulo();
                    almacenServidor.cambiarPropietario(titulo_libro, id_propietario);
                }

            }
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error");
        }

    }

}
