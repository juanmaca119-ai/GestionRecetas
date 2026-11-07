package EJERCICIO_RECETAS;

import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;

public class GestionRecetas {

    //declaracion de rutas
    private static final String RUTA_FICHERO =
            "C:\\Users\\JUAN MANUEL\\IdeaProjects\\GestionRecetas\\src\\main\\java\\EJERCICIO_RECETAS\\";
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
                    System.out.println("Ahora el nuevo tiempo de preparación: ");
                    int nuevoTiempo = sc.nextInt();

                    modificarTiempo(codigoMod, nuevoTiempo);
                    break;
                case 5:
                    System.out.println("ingresa un nuevo ID para la receta nueva");
                    int newId = sc.nextInt();
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
                new Receta(1, " Pasta carbonara ", "Italiana ", 20),
                new Receta(2, " Arroz con pollo ", " Arroces ", 40),
                new Receta(3, " Fajitas de pollo ", " Mexicana ", 25),
                new Receta(4, " Arepas rellenas ", " clombiana ", 20)
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



    /* método auxiliar para contar las recetas,
    *     es necesario para poder modificar el tiempo de preparación, o modificar las recetas en si*/
    public static int contarRecetas () {
        int contadorRec = 0; //método: declaramos esta variable para realizar el conteo
        try (
                ObjectInputStream contador = new ObjectInputStream(
                        new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            while (true) {
                Receta receta = (Receta)contador.readObject();
                contadorRec ++;
            }
        } catch (EOFException e){

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero" + e);
        }
        return contadorRec;
    }

    //método para modificar el tiempo de preparación
    public static void modificarTiempo (int codigoModificar, int nuevoTiempo) throws IOException {
        int total = contarRecetas(); /* método: en este caso usamos el método auxiliar que usamos
                                            para contar la cantidad de recetas que tenemos*/

        Receta [] recetas = new Receta [total]; /*método: se le pasa
                                                        el int que contiene el contador de recetas*/

        boolean modificado = false;
        //leemos
        try (ObjectInputStream modificar =
                     new ObjectInputStream(
                             new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            for (int i = 0; i < total; i++) { //método: for para recorrer todas las recetas dentro de la variable "total"
                recetas[i] = (Receta) modificar.readObject(); //se instancia a la clase Receta
                if (recetas[i].getId() == codigoModificar) { /* método: se llama al Array,
                                                                    se le pasa por parámetro el recorrido el Array
                                                                    [i] para comprobar por medio de getId si coincide con
                                                                    el codigo que quiere modificar */
                    recetas[i].setTiempoPreparacion(nuevoTiempo);
                    modificado = true; //se confirma el cambio de tiempo
                }
            }
        } catch (EOFException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (modificado == true) {
            try (ObjectOutputStream actualizar = new ObjectOutputStream(
                    new FileOutputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
                for (Receta receta : recetas) {
                    actualizar.writeObject (receta);
                    System.out.println("el tiempo se ha actualizado correctamente");
                }
            }
        } else {
            System.out.println("no existe ninguna receta con ese ID");
        }
    }
    /*public static void anadirReceta (int nuevoId) {
        int total = contarRecetas();

        Receta [] recetaNueva = new Receta [total + 1]; //se le suma uno para crear un nuevo espacio para la receta nueva

        try (ObjectInputStream añadir = new ObjectInputStream(
                new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            for (int i = 0; i < total; i++) {
                recetaNueva [i] = (Receta) añadir.
            }
        }
    }*/
}

