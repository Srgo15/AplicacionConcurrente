package Concurrencia.Cerrojos;

import Objetos.Entero;
import Concurrencia.Locks;

public class LockBakery2 implements Locks {
    private Entero[] turnos;

    public LockBakery2() {
        turnos = new Entero[2];
        turnos[0] = new Entero();
        turnos[0].setNum(0);
        turnos[1] = new Entero();
        turnos[0].setNum(0);
    }

    public boolean decide(int a, int b, int c, int d) {
        return (a > c || (a == c && b > d));
    }

    public void lock(int id) {
        turnos[id - 1].setNum(1);
        turnos[id - 1].setNum(turnos[id % 2].getNum() + 1);
        while (turnos[id % 2].getNum() != 0 && decide(turnos[id - 1].getNum(), id - 1, turnos[id % 2].getNum(), id % 2))
            ;
    }

    public void unlock(int id) {
        turnos[id - 1].setNum(0);
    }
}
