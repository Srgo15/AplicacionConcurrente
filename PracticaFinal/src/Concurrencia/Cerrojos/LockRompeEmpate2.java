package Concurrencia.Cerrojos;

import Concurrencia.Locks;

public class LockRompeEmpate2 implements Locks {
    private boolean in1, in2;
    private int last;

    public LockRompeEmpate2() {
        this.in1 = false;
        this.in2 = false;
        this.last = 0;
    }

    @Override
    public void lock(int id) {
        if (id == 1) {
            in1 = true;
            last = 1;
            while (in2 && last == 1)
                ;
        } else if (id == 2) {
            in2 = true;
            last = 2;
            while (in1 && last == 2)
                ;
        }
    }

    @Override
    public void unlock(int id) {
        if (id == 1) {
            in1 = false;
        } else if (id == 2) {
            in2 = false;
        }
    }
}
