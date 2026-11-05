package EJERCICIO_RECETAS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class GestionRecetas {

    //declaracion de rutas
    private static final String RUTA_FICHERO =
            "C:\\Users\\JuanManuelCabreraL\\IdeaProjects\\XML_EJERCICIO\\src\\main\\java\\EJERCICIO_RECETAS\\";
    private static final String NOMBRE_FICHERO = "recetas.dat";
    private static final String NOMBRE_FICHERO2 = "recetas.xml";
    private static final String NOMBRE_FICHERO3 = "configuracion.json";

    static File fichero = new File(RUTA_FICHERO + NOMBRE_FICHERO);

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Existe el fichero: " + fichero.exists());

        int opcionMenu;

        do {
            System.out.println("Menú de Recetas");
            System.out.println("1. Crear una receta");
            System.out.println("2. Ver todas las recetas almacenadas");
            System.out.println("3. Buscar una receta (por su id)");
            System.out.println("4. Modificar el tiemo de preparación de una receta");
            System.out.println("5. Añadir nuevas recetas");
            System.out.println("6. Salir");
            System.out.println("Seleccione una opción: ");
            opcionMenu = sc.nextInt();

            switch (opcionMenu) {
                case 1:
                    if (!fichero.exists()){
                        crearCatalogoInicial();

                        System.out.println("Existe el fichero: " + fichero.exists());
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (opcionMenu!= 6);
        sc.close();
    }


    //método para verificar si se ha creado el fichero
    public static void crearCatalogoInicial () throws IOException {
        if(fichero.createNewFile()){
            System.out.println("fichero creado correctamente ");
        }
        Receta [] recetas = {
                new Receta(01, "pasta carbonara", "italiana", 20),
                new Receta(2, "arroz con pollo", "arroces", 40),
                new Receta(3, "fajitas de pollo", "mexicana", 25),
                new Receta(4, "arepas rellenas", "colombiana", 20)
        };

        try (
                ObjectOutputStream escribirFichero = new ObjectOutputStream
                        (new FileOutputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            for (Receta receta : recetas) {
                escribirFichero.writeObject(receta);
            }
            System.out.println("Escrito");
        } catch (IOException e){
            System.out.println("Error al escribir en el fichero");
        }
    }



}

