package com.mycompany.losnegativos2;

import static spark.Spark.*;
import com.google.gson.Gson;
import static com.google.gson.internal.bind.TypeAdapters.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Biblioteca {

    public static void main(String[] args) {

        Gson gson = new Gson();
       
       
        LinkedList<Libro> libros = new LinkedList<>();
       
        LinkedList<Autor> autoresLibro1 = new LinkedList<>();
        LinkedList<Autor> autoresGenericos = new LinkedList<>();
       
        LinkedList<Copia> copias = new LinkedList<>();
        LinkedList<Lector> lectores = new LinkedList<>();
       
        LinkedList<Prestamo> prestamos = new LinkedList<>();
       
        LinkedList<Multa> multas = new LinkedList<>();
       
       
        /** Obtener la fecha actual **/
        LocalDate fechaActual = LocalDate.now();
       
        /** Obtener el mes actual como un objeto Month **/
        int mesActual = fechaActual.getMonthValue();
        int anioActual = fechaActual.getYear();
       
         autoresLibro1.add(addAutor("Gabriel Garcia Marquez", "Colombia", " 6 de marzo de 1927", null));
         autoresGenericos.add(addAutor("Paulo Coelho", "Brasil", "24 de agosto de 1947", null));
         autoresGenericos.add(addAutor("Charles Dickens", "reino unido", "1802", null));

       
        libros.add(addLibro("100 años de Soledad", "Novela", "editorial Sudamericana", 1967, autoresLibro1));
        libros.add(addLibro("El Señor de los Anillos", "Novela", "edit ", 1967, autoresLibro1));
        libros.add(addLibro("El principito", "Novela", "ecoditorial", 1943, autoresLibro1));
        libros.add(addLibro("Historia de dos ciudades", "Novela", "editorial chilena", 1859, autoresLibro1));
        libros.add(addLibro("Don Quijote de la Mancha", "Novela", "Editorial Tirant Lo Blanch", 1954, autoresLibro1));
       
       
        int indiceCopia = 1;
        for (Libro libroCopiar : libros) {
           
            copias.add(addCopia(
                    indiceCopia,
                    "DISPONIBLE",
                    libroCopiar.nombre,
                    libroCopiar.tipo,
                    libroCopiar.editorial,
                    libroCopiar.anio,
                    libroCopiar.autores
            ));
            indiceCopia++;
           
            copias.add(addCopia(
                    indiceCopia,
                    "DISPONIBLE",
                    libroCopiar.nombre,
                    libroCopiar.tipo,
                    libroCopiar.editorial,
                    libroCopiar.anio,
                    libroCopiar.autores
            ));
            indiceCopia++;
        }
     
       
        lectores.add(addLector(11111, "Alvaro", "Rubie", "Uberrimo") );
        lectores.add(addLector(66666, "Gustavo", "Petro", "Casa Nariño") );
       
        Lector lectorPrestar = null;
        for (Lector lector : lectores) {
            if(lector.getIdentificador() == 11111){
               // Con esto ahora tenemos el objeto del lector deseado
               lectorPrestar = lector;
            }
        }
       
        Copia copiaPrestar = null;
        for (Copia copia : copias) {
            if(copia.getId() == 2){
              copiaPrestar = copia;  
              copiaPrestar.setEstado("PRESTADO");
            }
        }
       
        if (copiaPrestar != null && lectorPrestar != null) {
            prestamos.add(
                    nuevoPrestamo(101, 201, 216, copiaPrestar, lectorPrestar)
            );
        }
       
        int idPrestamo = 101;
       
       
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getId() == idPrestamo) {
               
                // Añadimos la logica de las fechas
                int fechaInicioMulta = prestamo.getFechaFin();
                int fechaFinMulta = mesActual;
               
                //obtenemos el lector y tambien obtenemos el prestamos
                Lector lectorMulta = prestamo.getLector();
                Prestamo prestamoMulta = prestamo;
               
                // creamos un objeto de multa y luego lo añadimos al array de multas y tambien al lector
                Multa multaGenerada = nuevaMulta(
                        idPrestamo,
                        fechaInicioMulta,
                        fechaFinMulta,
                        lectorMulta,
                        prestamoMulta
                );
               
                multas.add(multaGenerada);
            }
        }
       
       
        Spark.port(4567);
        // Endpoint para agregar un libro
        post("/libros", (req, res) -> {
            res.type("application/json");

            // Extraer datos del cuerpo de la solicitud
            String requestBody = req.body();
            Libro nuevoLibro = gson.fromJson(requestBody, Libro.class);

            // Agregar el nuevo libro a la lista de libros
            libros.add(nuevoLibro);

            return gson.toJson(nuevoLibro);
        });

       
        get("/libros", (req, res) -> {
            res.type("application/json");
            return gson.toJson(libros);
        });
       
        get("/copias", (req, res) -> {
            res.type("application/json");
            return gson.toJson(copias);
        });

        get("/lectores", (req, res) -> {
            res.type("application/json");
            return gson.toJson(lectores);
        });

        get("/prestamos", (req, res) -> {
            res.type("application/json");
            return gson.toJson(prestamos);
        });
       

        get("/multas/:idPrestamo", (req, res) -> {
        res.type("application/json");

        // Obtener el ID del préstamo desde los parámetros de la ruta

        // Buscar el préstamo correspondiente en la lista de préstamos
        Prestamo prestamoSeleccionado = null;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getId() == idPrestamo) {
                prestamoSeleccionado = prestamo;
                break;
            }
        }

        // Verificar si se encontró el préstamo
        if (prestamoSeleccionado != null) {
            // Obtener el lector y el préstamo asociados al préstamo seleccionado
            Lector lectorMulta = prestamoSeleccionado.getLector();
            Prestamo prestamoMulta = prestamoSeleccionado;

            // Calcular la multa
            int fechaInicioMulta = prestamoMulta.getFechaFin();
            int fechaFinMulta = fechaActual.getMonthValue(); // Mes actual

            // Calcular el valor a pagar por la multa (con costo de $5000 por día de retraso)
           int valorMulta = calcularValorMulta(fechaInicioMulta, fechaFinMulta);

            // Crear y agregar la multa a la lista de multas
            Multa multaGenerada = nuevaMulta(idPrestamo, fechaInicioMulta, fechaFinMulta, lectorMulta, prestamoMulta);
            multas.add(multaGenerada);

            // Devolver la información de la multa generada en formato JSON
            String mensaje = "El valor a pagar por la multa es: $" + valorMulta;
            return gson.toJson(mensaje);
        } else {
            // Devolver un mensaje de error si no se encuentra el préstamo
            res.status(404);
            return gson.toJson("Préstamo no encontrado");
        }
    });
       
        get("/libros/cantidad-disponible", (req, res) -> {
        res.type("application/json");

        // Mapa para almacenar los libros y su cantidad disponible
        HashMap<String, Integer> librosConCantidadDisponible = new HashMap<>();

        // Iterar sobre la lista de libros y contar las copias disponibles
        for (Libro libro : libros) {
            int cantidadDisponible = 0;
            for (Copia copia : copias) {
                if (copia.getEstado().equals("DISPONIBLE") && copia.getNombre().equals(libro.getNombre())) {
                    cantidadDisponible++;
                }
            }
            // Si hay al menos una copia disponible, agregamos el libro al mapa
            if (cantidadDisponible > 0) {
                librosConCantidadDisponible.put(libro.getNombre(), cantidadDisponible);
            }
        }

        // Devolver el mapa de libros y su cantidad disponible en formato JSON
        return gson.toJson(librosConCantidadDisponible);
    });
       
       
        post("/agregarPrestamos", (req, res) -> {
        res.type("application/json");

        // Extraer datos del cuerpo de la solicitud
        String requestBody = req.body();
        PedidoPrestamo prestamoRequest = gson.fromJson(requestBody, PedidoPrestamo.class);

        // Buscar el libro y el lector en base a los datos proporcionados en la solicitud
        Libro libroPrestamo = buscarLibroPorNombre(prestamoRequest.getNombreLibro());
        Lector lectorPrestamo = buscarLectorPorIdentificador(prestamoRequest.getIdentificadorLector());

        // Verificar si se encontraron el libro y el lector
        if (libroPrestamo != null && lectorPrestamo != null) {
            // Verificar si hay copias disponibles para el libro
            Copia copiaDisponible = buscarCopiaDisponible(libroPrestamo);
            if (copiaDisponible != null) {
                // Actualizar el estado de la copia a "PRESTADO"
                copiaDisponible.setEstado("PRESTADO");

                // Obtener la fecha actual para el préstamo
                LocalDate fechaPrestamo = LocalDate.now();

                // Calcular la fecha de devolución (por ejemplo, 15 días después)
                LocalDate fechaDevolucion = fechaPrestamo.plusDays(15);

                // Crear el nuevo préstamo
                Prestamo nuevoPrestamo = nuevoPrestamo(prestamos.size() + 1, fechaPrestamo.getMonthValue(), fechaDevolucion.getMonthValue(), copiaDisponible, lectorPrestamo);

                // Agregar el préstamo a la lista de préstamos
                prestamos.add(nuevoPrestamo);

                // Devolver información del préstamo en formato JSON
                return gson.toJson(nuevoPrestamo);
            } else {
                // Devolver un mensaje de error si no hay copias disponibles
                res.status(400);
                return gson.toJson("No hay copias disponibles para el libro");
            }
        } else {
            // Devolver un mensaje de error si no se encuentra el libro o el lector
            res.status(400);
            return gson.toJson("Libro o lector no encontrado");
        }
});
   
       
}

    class PedidoPrestamo {
        private String nombreLibro;
        private int identificadorLector;

       
        // getters y setters

        public String getNombreLibro() {
            return nombreLibro;
        }

        public void setNombreLibro(String nombreLibro) {
            this.nombreLibro = nombreLibro;
        }

        public int getIdentificadorLector() {
            return identificadorLector;
        }

        public void setIdentificadorLector(int identificadorLector) {
            this.identificadorLector = identificadorLector;
        }
    }

    class RespuestaPrestamo {
        private String mensaje;

        public RespuestaPrestamo(String mensaje) {
            this.mensaje = mensaje;
        }
       
        // getters y setters

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
       
    }
   
   
    public static Libro addLibro(String nombre, String tipo, String editorial, int anio, LinkedList<Autor> autores) {
        Libro libro = new Libro(nombre, tipo, editorial, anio, autores);
        return libro;
    }
   
    public static Autor addAutor(String nombre, String nacionalidad, String hbd, LinkedList<Libro> libros) {
        Autor autor = new Autor(nombre, nacionalidad, hbd, libros);
        return autor;
    }
   
    public static Copia addCopia(int id, String estado, String nombre, String tipo, String editorial, int anio, LinkedList<Autor> autores) {
        Copia copia = new Copia(id, estado, nombre, tipo, editorial, anio, autores);
        return copia;
    }
   
    public static Lector addLector(int id, String nombre, String apellido, String dir) {
        // dejamos el ultimo en null porque este no tiene multas apenas lo creamos
        Lector lector = new Lector(id, nombre, apellido, dir, null);
        return lector;
    }
   
    public static Prestamo nuevoPrestamo(int id, int fechaInicio, int fechaFin, Copia copia, Lector lector) {
        Prestamo prestamo = new Prestamo(id, fechaInicio, fechaFin, copia, lector);
        return prestamo;
    }
   
    public static Multa nuevaMulta(int id, int fechaInicio, int fechaFin, Lector lector, Prestamo prestamo) {
        Multa multa = new Multa(id, fechaInicio, fechaFin, lector, prestamo);
        return multa;
    }
   
    public static int calcularValorMulta(int fechaInicio, int fechaFin) {
    // Supongamos que hay 30 días en un mes
    int diasDeRetraso = ((-fechaFin) + (fechaInicio)); // Calculamos el número de días de retraso
    return diasDeRetraso * 5000; // Multa de $5000 por cada día de retraso
    }
   
    private static LinkedList<Libro> libros = new LinkedList<>();
    private static LinkedList<Lector> lectores = new LinkedList<>();
    private static LinkedList<Copia> copias = new LinkedList<>();
   
    public static Libro buscarLibroPorNombre(String nombreLibro) {
        for (Libro libro : libros) {
            if (libro.getNombre().equals(nombreLibro)) {
                return libro;
            }
        }
        return null;
    }

    public static Lector buscarLectorPorIdentificador(int identificadorLector) {
        for (Lector lector : lectores) {
            if (lector.getIdentificador() == identificadorLector) {
                return lector;
            }
        }
        return null;
    }

    public static Copia buscarCopiaDisponible(Libro libro) {
        for (Copia copia : copias) {
            if (copia.getNombre().equals(libro.getNombre()) && copia.getEstado().equals("DISPONIBLE")) {
                return copia;
            }
        }
        return null;
    }
       
}
