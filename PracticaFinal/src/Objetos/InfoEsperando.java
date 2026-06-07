package Objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Concurrencia.ControladorLE;
import Concurrencia.Semaforos.GestorLE;

public class InfoEsperando {

    private Map<String, List<Integer>> esperandoLibros;
    ControladorLE controlador;

    public InfoEsperando() {
        this.esperandoLibros = new HashMap<>();
        this.controlador = new GestorLE();
    }

    // METODO DE ESCRITURA
    public boolean addLibroEsperando(String titulo, int id_esperando) {

        try {
            controlador.request_write();
            try {
                if(esperandoLibros.containsKey(titulo)) {
                    List<Integer>  lista = esperandoLibros.get(titulo);
                    lista.add(id_esperando);
                    return false;
            }
            else {
                List<Integer> lista = new ArrayList<>();
                lista.add(id_esperando);
                esperandoLibros.put(titulo, lista);
                return true;
            }
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        
    }

    // METODO LECTURA
    public List<Integer> registrarLibroYDevolverlistaSocketsEsperando(Libro libro) {
        try {
            controlador.request_write();
            try {
                String titulo = libro.getTitulo();
                List<Integer> listaID_Esperando = esperandoLibros.remove(titulo); //Obtenemos la lista y la eliminamos a la vez
                return listaID_Esperando;
            } finally {
                controlador.release_write();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }



}
