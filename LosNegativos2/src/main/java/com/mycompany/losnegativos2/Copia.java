package com.mycompany.losnegativos2;

import java.util.LinkedList;

/**
 *
 * @author JMINOTA
 */
public class Copia extends Libro{
    
    
    /**
     * ESTADO (DISPONIBLE - PRESTADO)
     * PUEDE SER BOOL TRUE=DISPONIBLE FALSE=PRESTADO
     **/
    int id;
    String estado;
    
    /**
     *
     * @param id
     * @param estado
     * @param nombre
     * @param tipo
     * @param editorial
     * @param anio
     * @param autores
     */
    public Copia(int id, String estado, String nombre, String tipo, String editorial, int anio, LinkedList<Autor> autores) {
        super (nombre, tipo, editorial, anio, autores);
        this.id = id;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
      public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String getEditorial() {
        return editorial;
    }

    @Override
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    @Override
    public int getAnio() {
        return anio;
    }

    @Override
    public void setAnio(int anio) {
        this.anio = anio;
    }

    public LinkedList<Autor> getAutores() {
        return autores;
    }

    public void setAutores(LinkedList<Autor> autores) {
        this.autores = autores;
    }
    
    
}