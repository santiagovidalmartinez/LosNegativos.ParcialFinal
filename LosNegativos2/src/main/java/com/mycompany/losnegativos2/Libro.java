/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.losnegativos2;

import java.util.LinkedList;

/**
 *
 * @author JMINOTA
 */
public class Libro {
    String nombre, tipo, editorial;
    int anio;
    LinkedList<Autor> autores = new LinkedList<>();

    public Libro(String nombre, String tipo, String editorial, int anio, LinkedList<Autor> autores) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.editorial = editorial;
        this.anio = anio;
        this.autores = autores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
    
    
    
}