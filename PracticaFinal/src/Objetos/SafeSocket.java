package Objetos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Concurrencia.Locks;
import Concurrencia.Cerrojos.LockTicket;
import Mensaje.Mensaje;

public class SafeSocket {

    private Socket socket;
    private ObjectOutputStream fout;
    private ObjectInputStream fin;
    private Locks lock;

    public SafeSocket(Socket socket) {
        try {
            this.socket = socket;
            this.fout = new ObjectOutputStream(socket.getOutputStream());
            fout.flush();
            this.fin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock = new LockTicket(100);
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return fout;
    }

    public ObjectInputStream getObjectInputStream() {
        return fin;
    }

    public void writeSafe(Mensaje mensaje) {
        lock.lock(0);
        try {
            fout.writeObject(mensaje);
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(0);
        }
    }

    public Mensaje readSafe() { //No usamos lock ya que nuestra arquitectura nos garantiza que solo leen los Oyentes
        try {
            return (Mensaje) fin.readObject();
        } catch (Exception e) {
            System.out.println("Ha surgido un problema durante la lectura del mensaje");
            e.printStackTrace();
            return null;
        }
    }

    public void cerrarSocket() {
        try {
            lock.lock(0);
            fout.close();
            fin.close();
            socket.close();
            lock.unlock(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
