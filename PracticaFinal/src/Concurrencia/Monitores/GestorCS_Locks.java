package Concurrencia.Monitores;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Concurrencia.ControladorCS;

public class GestorCS_Locks implements ControladorCS {
    private int contador;
    private final Lock lock;
    private final Condition esperando;

    public GestorCS_Locks() {
        this.contador = 0;
        lock = new ReentrantLock();
        esperando = lock.newCondition();
    }

    public void request_access() {
        lock.lock();
        while (contador > 0) {
            try {
                esperando.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        contador++;
        lock.unlock();
    }

    public void release_access() {
        lock.lock();
        contador--;
        esperando.signalAll();
        lock.unlock();
    }
    
}
