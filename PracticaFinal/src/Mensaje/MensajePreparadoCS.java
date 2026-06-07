package Mensaje;

public class MensajePreparadoCS extends Mensaje{

    private int puerto;
    private String ip_asignada;
    private int id;
    private String titulo_libro;
    private int id_destino;

    public MensajePreparadoCS(int puerto, String ip_asignada, int id, String titulo_libro, int id_destino){
        this.puerto = puerto;
        this.ip_asignada = ip_asignada;
        this.id = id;
        this.titulo_libro = titulo_libro;
        this.id_destino = id_destino;
    }

    public int getPuerto(){
        return puerto;
    }

    public String getIP(){
        return ip_asignada;
    }

    public int getID() {
        return id;
    }

    public String getTitulo() {
        return titulo_libro;
    }

    public int getID_Destino(){
        return id_destino;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_PREPARADO_CS;
    }
    
}
