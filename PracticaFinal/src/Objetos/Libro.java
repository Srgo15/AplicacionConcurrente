package Objetos;

import java.io.Serializable;

import Agentes.Usuario;

public class Libro implements Serializable{

    private String titulo;
    private String autor;
    private Usuario usuario;

    public Libro(String titulo, String autor, Usuario usuario) {
        this.titulo = titulo;
        this.autor = autor;
        this.usuario = usuario;
    }

    public String toString() {
        return "Titulo: " + this.titulo + " | Autor: " + this.autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
}
