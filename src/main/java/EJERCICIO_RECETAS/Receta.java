package EJERCICIO_RECETAS;

import java.io.Serializable;

public class Receta implements Serializable {

    private int id;
    private String nombre;
    private String categoria;
    private int tiempoPreparacion;

    public Receta (int id, String nombre, String categoria, int tiempoPreparacion) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "id: " + id +
                "nombre: " + nombre +
                "categoria: " + categoria +
                "tiempo de preparacion: "+tiempoPreparacion;
    }
}
