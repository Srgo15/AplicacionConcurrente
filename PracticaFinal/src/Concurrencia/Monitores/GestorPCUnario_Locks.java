package Concurrencia.Monitores;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Concurrencia.ControladorPC;

public class GestorPCUnario_Locks implements ControladorPC {

    private int full;

    private final Lock mutex;

    private final Condition producir;
    private final Condition consumir;

    public GestorPCUnario_Locks() {
        full = 0;
        mutex = new ReentrantLock();
        producir = mutex.newCondition();
        consumir = mutex.newCondition();
    }


    @Override
    public void preProducir() throws InterruptedException {
        mutex.lock();
        try {
            while(full > 0) {
            producir.await();
        }
        }
        catch(InterruptedException e) { // Por si se cancela el hilo productor que no se quede con el mutex
            mutex.unlock();
            throw e;
        }
    }

    @Override
    public void postProducir() throws InterruptedException {
        try {
            full = 1;
            consumir.signal();
        }
        finally {
            mutex.unlock();
        }
    }

    @Override
    public void preConsumir() throws InterruptedException {
        mutex.lock();
        try {
            while(full == 0){
                consumir.await();
            }
        }
        catch(InterruptedException e) {
            mutex.unlock();
            throw e;
        }
    }

    @Override
    public void postConsumir() throws InterruptedException {
        try {
            full = 0;
            producir.signal();
        }
        finally {
            mutex.unlock();
        }
    }
    
}
