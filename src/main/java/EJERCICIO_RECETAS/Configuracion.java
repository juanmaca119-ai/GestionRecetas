package EJERCICIO_RECETAS;

public class Configuracion {
    private int ultimaOpcionSeleccionada;
    private int cantidadRecetas;

    public Configuracion () {
        //constructor vacio para instanciar el objeto al leer el json
    }

    public Configuracion (int ultimaOpcionSeleccionada, int cantidadRecetas){
        this.ultimaOpcionSeleccionada = ultimaOpcionSeleccionada;
        this.cantidadRecetas = cantidadRecetas;
    }

    @Override
    public String toString() {
        return "Ultima opción del menú: "+ ultimaOpcionSeleccionada +
                "\nNúmero total de recetas almacenadas: "+cantidadRecetas;
    }
}
