package Concurrencia.Semaforos;

import java.util.concurrent.Semaphore;

import Concurrencia.ControladorLE;

public class GestorLE implements ControladorLE {

    private Semaphore mutex;
    private Semaphore r, w; // Semaforos para los readers y writers
    private int nr, dr, nw, dw;

    public GestorLE() { // añadido
    	nr = 0;
		dr = 0;
		nw = 0;
		dw = 0;
		mutex = new Semaphore(1);
		r = new Semaphore(0);
		w = new Semaphore(0);
    }
    
    @Override
    public void request_read() throws InterruptedException {
        mutex.acquire();
        if (nw > 0) {
            dr = dr + 1;
            mutex.release();
            r.acquire(); // Paso testigo
        }
        nr = nr + 1;
        if (dr > 0) {// Sirve para despertar a los readers en cadena
            dr = dr - 1;
            r.release(); // Paso testigo
        } else {
            mutex.release();
        }
    }

    @Override
    public void release_read() throws InterruptedException {
        mutex.acquire();
        nr = nr - 1;
        if (nr == 0 && dw > 0) {
            dw = dw - 1;
            w.release();// Paso de testigo
        } else {
            mutex.release();
        }
    }

    @Override
    public void request_write() throws InterruptedException {
        mutex.acquire();
        if (nr > 0 || nw > 0) {
            dw = dw + 1;
            mutex.release();
            w.acquire(); // Paso de testigo
        }
        nw = nw + 1;
        mutex.release();
    }

    @Override
    public void release_write() throws InterruptedException {
        mutex.acquire();
        nw = nw - 1;
        if (dw > 0) {
            dw = dw - 1;
            w.release(); // Paso de testigo
        } else if (dr > 0) {
            dr = dr - 1;
            r.release(); // Paso de testigo
        } else {
            mutex.release();
        }
    }
}
