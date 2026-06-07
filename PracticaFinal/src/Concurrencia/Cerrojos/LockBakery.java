package Concurrencia.Cerrojos;

import Concurrencia.Locks;
import Objetos.Entero;

public class LockBakery implements Locks {

    private Entero[] turnos;
    private int M;

    public LockBakery(int M) {
        turnos = new Entero[M];
        this.M = M;
        for (int i = 0; i < M; i++) {
            turnos[i] = new Entero();
            turnos[i].setNum(0);
        }
    }

    public boolean decide(int a, int b, int c, int d) {
        return (a > c || (a == c && b > d));
    }

    public int getMax() {
        int max = turnos[0].getNum();
        for (int i = 1; i < M; ++i) {
            if (turnos[i].getNum() > max) {
                max = turnos[i].getNum();
            }
        }
        return max;
    }

    @Override
    public void lock(int id) {
        turnos[id].setNum(1); // Ponemos a 1 el turno[id]
        turnos[id].setNum(getMax() + 1); // Ponemos el turno[id] = max(turno) + 1
        for (int k = 0; k < M; ++k) {
            while (k != id && turnos[k].getNum() != 0 && decide(turnos[id].getNum(), id, turnos[k].getNum(), k)) {
                Thread.yield();
            }
        }

    }

    @Override
    public void unlock(int id) {
        turnos[id].setNum(0);
    }
}
