package Concurrencia.Cerrojos;

import java.util.concurrent.atomic.AtomicInteger;

import Concurrencia.Locks;

public class LockTicket implements Locks {
    private volatile int next;
    private AtomicInteger number;
    // El turno se puede declarar como una variable local ya que cada uno usa el suyo
    private int N; // Numero maximo de procesos, lo usamos para evitar el desbordamiento

    public LockTicket(int N) {
        next = 1;
        number = new AtomicInteger(1);
        this.N = N;

    }

    @Override
    public void lock(int id) {
        int turno = number.getAndIncrement();
        // Instrucciones para asegurarnos de que no se produce desbordamiento
        if (turno == N) {
            number.addAndGet(-N);
        }
        if (turno >= N) {
            turno = turno - N;
        }
        while (turno != next) {
            Thread.yield();
        }
    }

    @Override
    public void unlock(int id) {
        next = (next + 1) % N;
    }
}
