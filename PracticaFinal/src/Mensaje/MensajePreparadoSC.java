package Mensaje;

public class MensajePreparadoSC extends Mensaje{

    private int num_puerto;
    private String ip;
    private String titulo_libro;
    private String nombre_destino;

    public MensajePreparadoSC(int num_puerto, String ip, String titulo_libro, String nombre_destino){
        this.num_puerto = num_puerto;
        this.ip = ip;
        this.titulo_libro = titulo_libro;
        this.nombre_destino = nombre_destino;
    }

    public int getPuerto(){
        return num_puerto;
    }

    public String getIP(){
        return ip;
    }

    public String getTitulo() {
        return titulo_libro;
    }

    public String getNombreDestino() {
        return nombre_destino;
    }

    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_PREPARADO_SC;
    }

}
