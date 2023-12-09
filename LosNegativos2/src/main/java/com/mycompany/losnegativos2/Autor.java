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
public class Autor {

    String nombre, nacionalidad, hbd;
    LinkedList<Libro> libros = new LinkedList<>();

    public Autor(String nombre, String nacionalidad, String hbd, LinkedList<Libro> libros) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.hbd = hbd;
        this.libros = libros;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getHbd() {
        return hbd;
    }

    public void setHbd(String hbd) {
        this.hbd = hbd;
    }
    
}