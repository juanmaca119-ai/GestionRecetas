package EJERCICIO_RECETAS;

import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;

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
            System.out.println("\nMenú de Recetas");
            System.out.println("1. Crear la receta por defecto");
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

                        System.out.println("\nExiste la receta (fichero): " + fichero.exists());
                    } else if (fichero.exists()){
                        System.out.println("\nYa existe la receta");
                    }
                    break;
                case 2:
                    mostrarRecetas();
                    break;
                case 3:
                    System.out.println("Ingrese el ID de la receta que desea buscar (1 - 4)");
                    int codigoBuscado = sc.nextInt();
                    buscarRecetas(codigoBuscado);
                    break;
                case 4:
                    System.out.println("Ingresa el ID de la receta a la que quieres modificar su tiempo de preparación: ");
                    int codigoMod = sc.nextInt();
                    //modificarTiempo(codigoMod);
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("\nGracias por usar mi gestor de recetas");
                    break;
                default:
                    System.out.println("\nOpción no válida");
            }
        } while (opcionMenu!= 6);
        sc.close();
    }


    //método para verificar si se ha creado el fichero
    public static void crearCatalogoInicial () throws IOException {
        if(fichero.createNewFile()){
            System.out.println("\nfichero creado correctamente ");
        }
        Receta [] recetas = {
                new Receta(1, " pasta carbonara ", " italiana ", 20),
                new Receta(2, " arroz con pollo ", " arroces ", 40),
                new Receta(3, " fajitas de pollo ", " mexicana ", 25),
                new Receta(4, " arepas rellenas ", " colombiana ", 20)
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

    //método para mostar las recetas
    public static void mostrarRecetas () throws IOException {

        try (ObjectInputStream mostrar = new ObjectInputStream(new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            while (true) {
                Receta receta = (Receta)mostrar.readObject();
                System.out.println(receta);
            }
        } catch (EOFException e) {
            System.out.println("Fin del fichero");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero" + e);
        }
    }


    //método para buscar recetas por id
    public static void buscarRecetas (int codigo) throws IOException {
        boolean encontrada = false;
        try (ObjectInputStream mostrar = new ObjectInputStream(new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            while (true) {
                Receta receta = (Receta) mostrar.readObject();

                if (receta.getId() == codigo) {
                    System.out.println(receta);

                    encontrada = true;
                    break;
                }
            }
        } catch (EOFException e) {
            System.out.println("Fin del fichero");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero" + e);
        } if (!encontrada) {
            System.out.println("ID no válido, ingresa otro");
        }
    }

    //método para modificar el tiempo de preparación
    public static void modificarTiempo (int codigoModificar) throws IOException {
        boolean modificado = false;
        try (ObjectInputStream modificar = new ObjectInputStream(new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            while (true) {
                Receta [] receta = (Receta[])modificar.readObject();


            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

