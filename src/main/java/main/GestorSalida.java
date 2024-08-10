package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import modulos.token.Token;

/**
 * Clase GestorSalida que se encarga de gestionar la salida de tokens, reglas
 * aplicadas y contenido de la tabla de símbolos.
 */
public class GestorSalida {

    // Directorio de salida
    private final static String DIRECTORIO_SALIDA = "output";
    // Archivo de salida para las reglas aplicadas
    private final static String ARCHIVO_REGLAS = Paths.get(DIRECTORIO_SALIDA, "reglasAplicadas.txt").toString();
    // Archivo de salida para la tabla de símbolos
    private final static String ARCHIVO_TS = Paths.get(DIRECTORIO_SALIDA, "archivoTablaSimbolos.txt").toString();
    // Archivo de salida para los tokens
    private final static String ARCHIVO_TOKENS = Paths.get(DIRECTORIO_SALIDA, "archivoTokens.txt").toString();

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorSalida() {
    }

    /**
     * Escribe los tokens, reglas aplicadas y el contenido de la tabla de símbolos
     * en los archivos correspondientes.
     *
     * @param listaTokens            Lista de tokens a escribir.
     * @param listaReglas            Lista de reglas aplicadas a escribir.
     * @param contenidoTablaSimbolos Contenido de la tabla de símbolos a escribir.
     * @throws IOException Si ocurre un error al escribir los archivos.
     */
    public static void escribirSalida(List<Token> listaTokens, List<Integer> listaReglas, String contenidoTablaSimbolos)
            throws IOException {
        crearDirectorioSalida();
        escribirTokens(listaTokens);
        escribirReglasAplicadas(listaReglas);
        escribirTablaSimbolos(contenidoTablaSimbolos);
    }

    /**
     * Crea el directorio de salida y los archivos necesarios.
     *
     * @throws IOException Si ocurre un error al crear el directorio o los archivos.
     */
    private static void crearDirectorioSalida() throws IOException {
        Path pathDirectorio = Paths.get(DIRECTORIO_SALIDA);
        if (Files.notExists(pathDirectorio)) {
            Files.createDirectory(pathDirectorio);
        }

        for (String archivo : List.of(ARCHIVO_REGLAS, ARCHIVO_TOKENS, ARCHIVO_TS)) {
            Path pathArchivo = Paths.get(archivo);
            if (Files.exists(pathArchivo)) {
                Files.delete(pathArchivo);
            }
            Files.createFile(pathArchivo);
        }
    }

    /**
     * Escribe la lista de tokens en el archivo correspondiente.
     *
     * @param listaTokens Lista de tokens a escribir.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private static void escribirTokens(List<Token> listaTokens) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TOKENS))) {
            for (Token item : listaTokens) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Escribe la lista de reglas aplicadas en el archivo correspondiente.
     *
     * @param listaReglas Lista de reglas aplicadas a escribir.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private static void escribirReglasAplicadas(List<Integer> listaReglas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_REGLAS))) {
            writer.write("A\n");
            for (Integer item : listaReglas) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Escribe el contenido de la tabla de símbolos en el archivo correspondiente.
     *
     * @param contenido Contenido de la tabla de símbolos a escribir.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private static void escribirTablaSimbolos(String contenido) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TS))) {
            writer.write(contenido);
        }
    }
}
