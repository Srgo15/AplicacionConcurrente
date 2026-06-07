package Concurrencia.Monitores;

import Concurrencia.ControladorPC;

public class GestorPCUnario_Synchronized implements ControladorPC {

	private int full;
	
    public GestorPCUnario_Synchronized() {
    	full = 0;
    }

    @Override
    public synchronized void preProducir() throws InterruptedException {
        while(full > 0) {
        	wait();
        }
    }

    @Override
    public synchronized void postProducir() throws InterruptedException {
        full = 1;
        notifyAll();
    }

    @Override
    public void preConsumir() throws InterruptedException {
        while(full == 0) {
            wait();
        }
    }

    @Override
    public void postConsumir() throws InterruptedException {
        full = 0;
        notifyAll();
    }

    

    
}