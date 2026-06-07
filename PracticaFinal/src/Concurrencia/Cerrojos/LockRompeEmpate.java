package Concurrencia.Cerrojos;

import Concurrencia.Locks;
import Objetos.Entero;

public class LockRompeEmpate implements Locks {
    private Entero[] in;
    private Entero[] last;
    private int M;

    public LockRompeEmpate(int M) {
        in = new Entero[M];
        last = new Entero[M];
        this.M = M;
        for (int i = 0; i < M; ++i) {
            in[i] = new Entero();
            in[i].setNum(-1);
            last[i] = new Entero();
            last[i].setNum(-1);
        }
    }

    @Override
    public void lock(int id) {
        for (int j = 0; j < M; j++) {
            in[id].setNum(j);
            last[j].setNum(id);
            for (int k = 0; k < M; k++) {
                while (k != id && in[k].getNum() >= in[id].getNum() && last[j].getNum() == id) {
                    Thread.yield();
                }
            }
        }
    }

    @Override
    public void unlock(int id) {
        in[id].setNum(-1);
    }
}
