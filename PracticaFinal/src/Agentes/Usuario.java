package Agentes;
import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private int id;
    private String ip;

    public Usuario(String nombre, String ip) {
        this.nombre = nombre;
        this.id = -1;
        this.ip = ip;
    }

    public String toString() {
        return "Nombre: " + this.nombre + " | ID: " + this.id + " | IP: " + this.ip;
    }
    
    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getNombre(){
        return nombre;
    }

    public void setID(int id) {
        this.id = id;
    }


}
