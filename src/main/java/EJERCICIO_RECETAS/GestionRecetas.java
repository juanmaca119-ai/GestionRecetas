package EJERCICIO_RECETAS;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;

//imports a xml
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//imports JSON
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;


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
        guardarConfiguracion();

        int opcionMenu;
        int ultimaOpcion = 0;

        do {
            System.out.println("\nMenú de Recetas");
            System.out.println("1. Crear la receta por defecto");
            System.out.println("2. Ver todas las recetas almacenadas");
            System.out.println("3. Buscar una receta (por su id)");
            System.out.println("4. Modificar el tiemo de preparación de una receta");
            System.out.println("5. Añadir nuevas recetas");
            System.out.println("6. Exportar catálogo XML");
            System.out.println("7. Salir");
            System.out.println("Seleccione una opción: ");
            opcionMenu = sc.nextInt();

            if (opcionMenu != 7) {
                ultimaOpcion = opcionMenu;
                //para guardar la ultima operacion q realiza el ususario
            }

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
                    anadirReceta(newId, sc);
                    break;
                case 6:
                    archivoXML();
                    break;
                case 7:
                    guardarConfiguracion(ultimaOpcion);
                    System.out.println("\nGracias por usar mi gestor de recetas");
                    break;
                default:
                    System.out.println("\nOpción no válida");
            }
        } while (opcionMenu!= 7);
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
    public static void anadirReceta (int nuevoId, Scanner sc) {
        /* método: le pasamos por parámetro el Scanner, esto para pedir los demas datos
        *     de la receta */
        int total = contarRecetas();

        Receta [] recetaNueva = new Receta [total + 1]; //se le suma uno para crear un nuevo espacio para la receta nueva

        try (ObjectInputStream añadir = new ObjectInputStream(
                new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            for (int i = 0; i < total; i++) {
                recetaNueva [i] = (Receta) añadir.readObject();

                if (recetaNueva[i].getId() == nuevoId) {
                    System.out.println("Este ID ya está asignado a otra receta");
                    return; //hace que le método se detenga y nos envie al menu principal

                }
            }
        } catch (EOFException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } //método: finaliza comprobación de ID's
        System.out.println("");
        sc.nextLine();

        System.out.println("Nombre de la receta nueva: ");
        String nombre = sc.nextLine();

        System.out.println("Categoria: ");
        String categoria = sc.nextLine();

        System.out.println("Tiempo de preparacion: ");
        int tiempo = sc.nextInt();

        /* método: en este caso, creamos una nueva receta con los datos nuevos
        *     pero usamos el Array que creamos con un espacio de más */

        recetaNueva [total] = new Receta(nuevoId, nombre, categoria, tiempo);
        //usamos el ID que le asignamos por parametro en el método, y luego las demás vaiables

        //sección de escritura
        try(ObjectOutputStream escribirNuevo = new ObjectOutputStream(
                new FileOutputStream(RUTA_FICHERO+NOMBRE_FICHERO))) {
            for (Receta receta : recetaNueva) {
                escribirNuevo.writeObject(receta);
            }
            System.out.println("La receta se ha añadido exitosamente");
        } catch (IOException e) {
            System.out.println("Error al escribir una nueva receta\n");
        }
    }



    //creación de xml
    public static void archivoXML () {
        int total = contarRecetas();

        Receta [] recetas = new Receta [total];
        //seccion de lectura de recetas.dat y exportacion a recetas.xml
        try (ObjectInputStream leer = new ObjectInputStream(
                new FileInputStream(RUTA_FICHERO + NOMBRE_FICHERO))) {
            for (int i = 0; i < total; i++) {
                recetas[i] = (Receta) leer.readObject();
            }
        } catch (EOFException e) {

        } catch(IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero" + e);
        }

        //seccion para crear el DOM en memoria
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();

            //analizador del DOM
            DocumentBuilder builder = factory.newDocumentBuilder();
            //creacion del xml vacio
            Document document = builder.newDocument();

            //creacion del elemento raiz
            //en este caso, por cada receta del array, se crea la etiqueta <recetas>
            Element etiquetaRecetas = document.createElement("recetas");
            document.appendChild(etiquetaRecetas);

            /*creamos:
            un bucle para que recorra todo
                                          el array referente a las recetas
                                          y creamos las etiquetas*/

            for (int i = 0; i < recetas.length; i++) {
                //<receta>
                Element receta = document.createElement("receta");
                receta.setAttribute("id", String.valueOf(recetas[i].getId()));
                etiquetaRecetas.appendChild(receta);
                //<nombre>
                Element nombreReceta = document.createElement("nombre");
                nombreReceta.setTextContent(recetas[i].getNombre());
                etiquetaRecetas.appendChild(nombreReceta);
                //<categoria>
                Element categoriaReceta = document.createElement("categoria");
                categoriaReceta.setTextContent(recetas[i].getCategoria());
                etiquetaRecetas.appendChild(categoriaReceta);
                //<tiempoPreparacion>
                Element tiempoPreparacionReceta = document.createElement("tiempoPreparacion");
                tiempoPreparacionReceta.setTextContent(String.valueOf(recetas[i].getTiempoPreparacion()));
                etiquetaRecetas.appendChild(tiempoPreparacionReceta);
            }
            //hacer el transformador
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(new File(RUTA_FICHERO + NOMBRE_FICHERO2)));
            System.out.println("xml creado correctamente");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void guardarConfiguracion (int ultimaOpcion){
        int total = contarRecetas();

        //instanciamos el objeto config en la clase Configuracion, se le pasa por parametro el ultimaOpcion y el total de recetas
        Configuracion config = new Configuracion(ultimaOpcion, total);

        //crea el Gson con un formato bonito
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter escritor = new FileWriter(RUTA_FICHERO+NOMBRE_FICHERO3)) {
            gson.toJson(config, escritor);
            System.out.println("Se guardó la configuración");
        } catch (IOException e) {
            System.out.println("error al guardar el json");
        }
    }

    //método para leer al inicar
    public static void guardarConfiguracion (){
        //comprobamos si existe el JSON creando un objeto
        File archivoJson = new File (RUTA_FICHERO + NOMBRE_FICHERO3);
        if (archivoJson.exists()) {
            Gson gson = new Gson(); //si existe, crea el JSON

            try (FileReader lector = new FileReader(RUTA_FICHERO+NOMBRE_FICHERO3)) {
                Configuracion config = gson.fromJson(lector, Configuracion.class); //convierte el contenido del archivo de vuelta a un objeto

                System.out.println(config);

            } catch (IOException e) {
                System.out.println("error");
            }

        }

    }

}

