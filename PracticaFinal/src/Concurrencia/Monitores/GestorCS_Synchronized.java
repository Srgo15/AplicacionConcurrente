package Concurrencia.Monitores;

import Concurrencia.ControladorCS;

public class GestorCS_Synchronized implements ControladorCS {

    private int contador;

    public GestorCS_Synchronized(){
        this.contador = 0;
    }

    @Override
    public synchronized void request_access() {
        contador++;
        while (contador > 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void release_access() {
        contador--;
        if (contador > 0) {
            notifyAll();
        }
    }

}
