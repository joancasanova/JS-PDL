package main;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import modulos.lexico.AnalizadorLexico;
import modulos.semantico.AnalizadorSemantico;
import modulos.sintactico.AnalizadorSintactico;
import modulos.tablaSimbolos.GestorTablas;
import modulos.token.TipoToken;
import modulos.token.Token;
import util.GestorErrores;

/**
 * Clase principal del analizador que se encarga de procesar el archivo de
 * entrada.
 * 
 * @autor Juan Francisco Casanova Ferrer
 * @institution Universidad Politécnica de Madrid
 * @contact juancasanovaferrer@gmail.com
 */
public class Analizador {

    private final static String DIRECTORIO_ENTRADA = "input";
    private final static String ARCHIVO_INPUT = Paths.get(DIRECTORIO_ENTRADA, "input.txt").toString();

    private final static GestorTablas gestorTablas = GestorTablas.getInstance();
    private final static AnalizadorLexico analizadorLexico = AnalizadorLexico.getInstance();
    private final static AnalizadorSintactico analizadorSintactico = AnalizadorSintactico.getInstance();
    private final static AnalizadorSemantico analizadorSemantico = AnalizadorSemantico.getInstance();

    public static void main(String[] args) {
        String rutaArchivo;
        if (args.length > 0) {
            rutaArchivo = args[0];
        } else {
            rutaArchivo = ARCHIVO_INPUT;
        }

        try {
            FileReader fichero = new FileReader(rutaArchivo);
            procesarFichero(fichero);
            System.out.println(
                    "Análisis completo. Se han generado los archivos de tokens, reglas, y tabla de símbolos.");
            fichero.close();
        } catch (IllegalStateException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (IOException e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, "Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, e.getMessage());
        }
    }

    /**
     * Procesa el archivo fuente utilizando los analizadores léxico, sintáctico y
     * semántico.
     *
     * @param fichero El FileReader del archivo fuente.
     * @throws IOException Si ocurre un error de lectura.
     */
    private static void procesarFichero(FileReader fichero) throws IOException {
        List<Token> listaTokens = new ArrayList<>();
        List<Integer> listaReglas = new ArrayList<>();

        Boolean finDeFichero = false;
        do {
            char caracter = (char) fichero.read();
            do {
                Token token = analizadorLexico.procesarCaracter(caracter);
                if (token != null) {
                    listaTokens.add(token);
                    if (token.getTipo().equals(TipoToken.FINDEFICHERO)) {
                        finDeFichero = true;
                    }

                    do {
                        Integer regla = analizadorSintactico.procesarToken(token);
                        if (regla != null) {
                            listaReglas.add(regla);
                            analizadorSemantico.procesarRegla(regla);
                        }
                    } while (!analizadorSintactico.isTokenProcesado());
                }
            } while (!analizadorLexico.isCaracterProcesado());
            if (caracter == '\n') {
                GestorErrores.incrementarLinea();
            }
        } while (!finDeFichero);

        GestorSalida.escribirSalida(listaTokens, listaReglas, gestorTablas.getImpresionTablas());

        gestorTablas.resetGestorTablas();
        analizadorLexico.resetAnalizadorLexico();
        analizadorSintactico.resetAnalizadorSintactico();
        analizadorSemantico.resetAnalizadorSemantico();
    }
}
