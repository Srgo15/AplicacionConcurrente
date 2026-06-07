package Concurrencia.Monitores;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Concurrencia.ControladorLE;

public class GestorLE_Locks implements ControladorLE{
    private int nr, nw;
    private Lock mutex;
    private Condition readers;
    private Condition writers;

    public GestorLE_Locks(){
        nr = 0;
        nw = 0;
        mutex = new ReentrantLock();
        readers = mutex.newCondition();
        writers = mutex.newCondition();
    }

    public void request_read() throws InterruptedException{
        mutex.lock();
        while(nw > 0){
            readers.await();
        }
        nr++;
        mutex.unlock();
    }

    public void release_read() throws InterruptedException{
        mutex.lock();
        nr--;
        if(nr == 0){
            writers.signal();
        }
        mutex.unlock();
    }

    public void request_write() throws InterruptedException{
        mutex.lock();
        while(nw > 0 || nr > 0) {
            writers.await();
        }
        nw++;
        mutex.unlock();
    }

    public void release_write() throws InterruptedException{
        mutex.lock();
        nw--;
        writers.signal();
        readers.signalAll();
        mutex.unlock();
    }
}
