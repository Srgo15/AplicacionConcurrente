package Concurrencia;

public interface ControladorLE {
    public void request_read() throws InterruptedException;
    public void release_read() throws InterruptedException;

    public void request_write() throws InterruptedException;
    public void release_write() throws InterruptedException;
}
