package Concurrencia.Monitores;

import Concurrencia.ControladorLE;

public class GestorLE_Synchronized implements ControladorLE {
    private int nr, nw;

    public GestorLE_Synchronized() {
        nr = 0;
        nw = 0;
    }

    @Override
    public synchronized void request_read() throws InterruptedException {
        while (nw > 0) {
            wait();
        }
        nr++;
    }

    @Override
    public synchronized void release_read() throws InterruptedException {
        nr--;
        notifyAll();
    }

    @Override
    public synchronized void request_write() throws InterruptedException {
        while (nw > 0 || nr > 0) {
            wait();
        }
        nw++;
    }

    @Override
    public synchronized void release_write() throws InterruptedException {
        nw--;
        notifyAll();
    }
}
