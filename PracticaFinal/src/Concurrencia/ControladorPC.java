package Concurrencia;

public interface ControladorPC {
    public void preProducir() throws InterruptedException;
    public void postProducir() throws InterruptedException;
    public void preConsumir() throws InterruptedException;
    public void postConsumir() throws InterruptedException;
}
