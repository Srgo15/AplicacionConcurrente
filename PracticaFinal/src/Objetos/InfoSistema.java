package Objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Agentes.Usuario;
import Concurrencia.ControladorLE;
import Concurrencia.Semaforos.GestorLE;

public class InfoSistema {

    private List<Usuario> tUsuarios;
    private Map<String, Libro> tLibros;
    private Map<String, Integer> infoLibros;
    private Map<Integer, SafeSocket> tSockets;
    private ControladorLE controlador;

    public InfoSistema() {
        this.tUsuarios = new ArrayList<>();
        this.tLibros = new HashMap<>();
        this.infoLibros = new HashMap<>();
        this.tSockets = new HashMap<>();
        this.controlador = new GestorLE(); //USAMOS SEMAFOROS

    }

    //METODO DE ESCRITURA
    public void addLibro(Libro libro) {
        try {
            controlador.request_write();
            try {
                tLibros.put(libro.getTitulo(), libro);
                infoLibros.put(libro.getTitulo(), -1);
            }
            finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //METODO DE ESCRITURA
    public int registrarUser(Usuario usuario, SafeSocket safeSocket){
        try {
            controlador.request_write();
            try {
                int id_asignado = tUsuarios.size();
                usuario.setID(id_asignado);
                tUsuarios.add(usuario);
                tSockets.put(id_asignado, safeSocket);
                return id_asignado;
                } finally {
                    controlador.release_write(); //NOS ASEGURAMOS DE LIBERAR SIEMPRE EL SEMAFORO
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
    }

    //METODO DE LECTURA
    public List<Usuario> getUsuarios() {
        try {
            controlador.request_read();
            try {
                List<Usuario> copia = new ArrayList<>(tUsuarios);
                return copia;
            } finally {
                controlador.release_read();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //METODO DE LECTURA
    public Map<String, Libro> getLibros() {
        try {
            controlador.request_read();
            try {
                Map<String,Libro> copia = new HashMap<>(tLibros);
                return copia;
            } finally {
                controlador.release_read();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    //METODO DE LECTURA
    public InfoSolicitud recopilarInfo(){
        try {
            controlador.request_read();
            try {
                List<Usuario> listaUsuarios = new ArrayList<>(tUsuarios);
                List<String> nombresLibros = new ArrayList<>();
                List<String> usuariosLibros = new ArrayList<>();

                Iterator<Map.Entry<String, Integer>> it = infoLibros.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Integer> entrada = it.next();
                    nombresLibros.add(entrada.getKey());
                    int id = entrada.getValue();
                    
                    String nombre_usuario;

                    if(id == -1){
                        nombre_usuario = "Biblioteca";
                    }
                    else {
                        nombre_usuario = tUsuarios.get(id).getNombre();
                    }

                    usuariosLibros.add(nombre_usuario);
                }

                return new InfoSolicitud(nombresLibros, usuariosLibros, listaUsuarios);
            } finally {
            controlador.release_read();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    //METODO DE LECTURA
    public Map<String, Integer> getInfoLibros() {
        try {
            controlador.request_read();
            try {
                Map<String,Integer> copia = new HashMap<>(infoLibros);
                return copia;
            } finally {
                controlador.release_read();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    //METODO DE LECTURA
    public Map<Integer, SafeSocket> getSockets() {
        try {
            controlador.request_read();
            try {
                Map<Integer,SafeSocket> copia = new HashMap<>(tSockets);
                return copia;
            } finally {
                controlador.release_read();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    //METODO DE ESCRITURA
    public ResultadoSolicitud prestamoLibro(String titulo, int id_usuario) {
        try {
            controlador.request_write();
            try {
                if(!infoLibros.containsKey(titulo)){ //No existe el libro
                    return null;
                }
                int propietario_actual = infoLibros.get(titulo);
                if(propietario_actual == -1) { //El libro esta en la biblioteca
                    infoLibros.put(titulo, id_usuario); //Actualizamos el propietario
                    Libro libro = tLibros.remove(titulo); //Eliminamos el libro de la biblioteca
                    return new ResultadoSolicitud(libro);
                }
                SafeSocket s = tSockets.get(propietario_actual);
                if(s == null){ //El cliente se ha marchado
                    return null;
                }
                else {
                    return new ResultadoSolicitud(s);
                }
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    //METODO DE LECTURA
    public SafeSocket buscarCanal(int id) {
        try {
            controlador.request_read();
            try {
                if (tSockets.containsKey(id)) {
                    return tSockets.get(id);
                } else {
                    return null;
                }
            } finally {
                controlador.release_read();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    //METODO DE ESCRITURA
    public void recuperaLibros(List<Libro> listaLibros, int id_usuario) {
        try {
            controlador.request_write();
            try {
                tUsuarios.set(id_usuario, null);
                tSockets.remove(id_usuario);
                for (Libro libro : listaLibros) {
                    String titulo = libro.getTitulo();
                    tLibros.put(titulo, libro);
                    infoLibros.put(titulo, -1);
                }
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    // METODO ESCRITURA
    public void cambiarPropietario(String titulo_libro, int id_propietario){
        try {
            controlador.request_write();
            try {
                infoLibros.put(titulo_libro, id_propietario);
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // METODO LECTURA
    public void registrarLibroYDevolverlistaSocketsEsperando1(Libro libro) {
        try {
            controlador.request_write();
            try {
                //Registramos el libro en la biblioteca
                String titulo = libro.getTitulo();
                tLibros.put(titulo, libro);
                infoLibros.put(titulo, -1);
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     // METODO LECTURA
    public List<SafeSocket> registrarLibroYDevolverlistaSocketsEsperando2(List<Integer> listaID_Esperando) {
        try {
            controlador.request_write();
            try {
                //Cogemos los sockets para avisar a los clientes
                List<SafeSocket> res = new ArrayList<>();

                for(Integer id : listaID_Esperando) {
                    if(tSockets.get(id) != null) { // Si no ponemos != null aquí, puede saltar NullPointerException cuando vaya a cerrar el socket
                        res.add(tSockets.get(id));
                    }
                    
                }

                return res;

            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // METODO LECTURA
    public String nombreUsuario(int id) {
            try {
                controlador.request_write();
            try {
                return tUsuarios.get(id).getNombre();
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
