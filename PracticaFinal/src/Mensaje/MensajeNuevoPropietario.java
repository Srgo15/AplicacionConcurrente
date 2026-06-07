package Mensaje;

public class MensajeNuevoPropietario extends Mensaje{
    private String titulo;
    private int id_nuevo;

    public MensajeNuevoPropietario(String titulo, int id){
        this.titulo = titulo;
        this.id_nuevo = id;
    }

    public String getTitulo(){
        return titulo;
    }

    public int getPropietario(){
        return id_nuevo;
    }


    @Override
    public KindMensaje getTipo() {
        return KindMensaje.MENSAJE_NUEVO_PROPIETARIO;
    }
}
