package Concurrencia.Monitores;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Concurrencia.ControladorPC;

public class GestorPCNario_Locks implements ControladorPC {

    private int count;
    private int N;
    private Lock mutex;
    private Condition producir;
    private Condition consumir;

    public GestorPCNario_Locks(int N) {
        this.N = N;
        this.count = 0;
        this.mutex = new ReentrantLock();
        this.producir = mutex.newCondition();
        this.consumir = mutex.newCondition();
    }


    @Override
    public void preProducir() throws InterruptedException {
        mutex.lock();
        try {
            while(count==N) {
                producir.await();
            }
        }
        catch(InterruptedException e) {
            mutex.unlock();
            throw e;
        }
    }

    @Override
    public void postProducir() throws InterruptedException {
        try {
            count++;
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
            while(count == 0){
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
            count--;
            producir.signal();
        }
        finally {
            mutex.unlock();
        }
    }

}
