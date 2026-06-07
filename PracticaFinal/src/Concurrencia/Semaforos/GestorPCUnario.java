package Concurrencia.Semaforos;

import java.util.concurrent.Semaphore;

import Concurrencia.ControladorPC;

public class GestorPCUnario implements ControladorPC{
	
	private Semaphore empty, full;

	public GestorPCUnario() {
		empty = new Semaphore(1);
		full = new Semaphore(0);
	}
	
    @Override
    public void preProducir() throws InterruptedException {
    	empty.acquire();
    }

    @Override
    public void postProducir() throws InterruptedException {
        full.release();
    }

    @Override
    public void preConsumir() throws InterruptedException {
    	full.acquire();
    }

    @Override
    public void postConsumir() throws InterruptedException {
		empty.release();
    }

}