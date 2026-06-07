package Concurrencia.Semaforos;

import java.util.concurrent.Semaphore;

import Concurrencia.ControladorPC;

public class GestorPCNario implements ControladorPC{
	
	private int N;
	private Semaphore empty, full;
	private Semaphore mutexP, mutexC;

	public GestorPCNario(int N) {
		this.N = N;
		empty = new Semaphore(N);
		full = new Semaphore(0);
		mutexP = new Semaphore(1);
		mutexC = new Semaphore(1);
	}
	
    @Override
    public void preProducir() throws InterruptedException {
    	empty.acquire();
		mutexP.acquire();
    }

    @Override
    public void postProducir() throws InterruptedException {
        mutexP.release();
        full.release();
    }

    @Override
    public void preConsumir() throws InterruptedException {
    	full.acquire();
		mutexC.acquire();
    }

    @Override
    public void postConsumir() throws InterruptedException {
    	mutexC.release();
		empty.release();
    }

}
