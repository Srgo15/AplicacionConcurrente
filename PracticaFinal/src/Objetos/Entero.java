package Objetos;

public class Entero {
    private volatile int minum;
	
	public Entero(){
		this.minum = 0;
	}
	
	public void incrementar() {
		minum++;
	}
	
	public void decrementar() {
		minum--;
	}
	
	public int getNum() {
		return minum;
	}
	
	public void setNum(int n) {
		minum = n;
	}
}
