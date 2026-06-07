package Concurrencia.Monitores;

import Concurrencia.ControladorPC;

public class GestorPCNario_Synchronized implements ControladorPC {

    private int N;
    private int cont;

    public GestorPCNario_Synchronized(int N) {
        this.N = N;
        this.cont = 0;
    }

    @Override
    public synchronized void preProducir() throws InterruptedException {
        while(cont == N) {
            wait();
        }
    }

    @Override
    public synchronized void postProducir() throws InterruptedException {
        cont++;
        notifyAll();
    }

    @Override
    public synchronized void preConsumir() throws InterruptedException {
        while(cont == 0) {
            wait();
        }
    }

    @Override
    public synchronized void postConsumir() throws InterruptedException {
        cont--;
        notifyAll();
    }
    
    
}
