package modulos.sintactico;

import java.io.*;
import java.util.*;

import modulos.sintactico.accion.Accion;
import modulos.sintactico.accion.AccionAceptar;
import modulos.sintactico.accion.AccionDesplazar;
import modulos.sintactico.accion.AccionReducir;
import modulos.token.TipoToken;
import util.GestorErrores;

/**
 * Clase que se encarga de parsear un archivo de texto generado por Bison.
 * Crea estructuras de datos para ser utilizadas por un analizador sintáctico
 * LR(1).
 */
public class ParserGramatica {

    private static final String GRAMMAR_SECTION = "Grammar";
    private static final String TERMINALS_SECTION = "Terminals";
    private static final String NONTERMINALS_SECTION = "Nonterminals";
    private static final String STATE_SECTION = "State";
    private static final String FILE_PATH = "src/main/resources/gramatica.output";

    private Map<Integer, Map<String, Accion>> tablaAccion;
    private Map<Integer, Map<String, Integer>> tablaGoTo;
    private Map<Integer, List<String>> reglas;
    private Set<String> terminales;
    private Set<String> noTerminales;

    // Instancia única de la clase
    private static ParserGramatica instancia;

    /**
     * Constructor privado para evitar la creación de instancias fuera de la clase.
     */
    private ParserGramatica() {
        tablaAccion = new HashMap<>();
        tablaGoTo = new HashMap<>();
        reglas = new HashMap<>();
        terminales = new HashSet<>();
        noTerminales = new HashSet<>();

        generarTabla(FILE_PATH);
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de ParserGramatica.
     */
    public static ParserGramatica getInstance() {
        if (instancia == null) {
            synchronized (ParserGramatica.class) {
                if (instancia == null) {
                    instancia = new ParserGramatica();
                }
            }
        }
        return instancia;
    }

    /**
     * Genera las tablas de acción y de goto a partir del archivo de gramática.
     * 
     * @param filePath La ruta del archivo de gramática.
     */
    private void generarTabla(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = reader.readLine()) != null) {

                if (linea.contains("conflicts") || linea.contains("unused") || linea.contains("useless")) {
                    continue;
                }

                switch (getSeccion(linea)) {
                    case GRAMMAR_SECTION:
                        procesarReglas(reader);
                        break;
                    case TERMINALS_SECTION:
                        procesarTerminales(reader);
                        break;
                    case NONTERMINALS_SECTION:
                        procesarNoTerminales(reader);
                        break;
                    case STATE_SECTION:
                        procesarEstado(reader, linea);
                        break;
                }
            }
        } catch (IOException e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SINTACTICO,
                    GestorErrores.ARCHIVO_GRAMATICA_NO_ENCONTRADO);
        }
    }

    /**
     * Obtiene la sección del archivo de gramática a la que pertenece una línea.
     * 
     * @param linea La línea a evaluar.
     * @return La sección correspondiente, o null si no corresponde a ninguna
     *         sección conocida.
     */
    private String getSeccion(String linea) {
        if (linea.startsWith(GRAMMAR_SECTION)) {
            return GRAMMAR_SECTION;
        } else if (linea.startsWith(TERMINALS_SECTION)) {
            return TERMINALS_SECTION;
        } else if (linea.startsWith(NONTERMINALS_SECTION)) {
            return NONTERMINALS_SECTION;
        } else if (linea.startsWith(STATE_SECTION)) {
            return STATE_SECTION;
        }
        return "";
    }

    /**
     * Procesa las reglas de gramática del archivo.
     * 
     * @param reader El BufferedReader para leer el archivo.
     * @throws IOException Si ocurre un error de lectura.
     */
    private void procesarReglas(BufferedReader reader) throws IOException {
        String line;
        int contadorLineasVacias = 0;

        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line == null)
                break;

            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            }

            contadorLineasVacias = 0;
            String[] partes = line.split("\\s+");
            int numeroRegla = Integer.parseInt(partes[1]);
            List<String> contenidoRegla = new ArrayList<>();

            for (String parte : partes) {
                if (esNoNumericaYNoVacia(parte)) {
                    if (parte.equals("|")) {
                        parte = reglas.get(numeroRegla - 1).get(0);
                    }
                    String terminalProcesado = procesarTerminal(parte.replaceAll("[:?]", ""));
                    if (!parte.equals("ε")) {
                        contenidoRegla.add(terminalProcesado);
                    }
                }
            }

            reglas.put(numeroRegla, contenidoRegla);
        }
    }

    /**
     * Verifica si una cadena no es numérica y no está vacía.
     * 
     * @param cadena La cadena a verificar.
     * @return true si la cadena no es numérica y no está vacía, false en caso
     *         contrario.
     */
    private boolean esNoNumericaYNoVacia(String cadena) {
        return !cadena.matches("-?\\d+") && !cadena.isEmpty();
    }

    /**
     * Procesa los terminales de la gramática del archivo.
     * 
     * @param reader El BufferedReader para leer el archivo.
     * @throws IOException Si ocurre un error de lectura.
     */
    private void procesarTerminales(BufferedReader reader) throws IOException {
        String line;
        int contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            }

            String terminalSinProcesar = line.split("\\s+")[1].replace("'", "");

            // Añadir los terminales sin las comillas simples
            terminales.add(procesarTerminal(terminalSinProcesar));
        }
    }

    /**
     * Procesa un terminal de la gramática.
     * 
     * @param terminalSinProcesar El terminal sin procesar.
     * @return El terminal procesado.
     */
    private String procesarTerminal(String terminalSinProcesar) {
        TipoToken tipoToken = TipoToken.procesarTipoToken(terminalSinProcesar);

        if (tipoToken != null) {
            return tipoToken.name();
        } else {
            // Caso para palabra reservada
            return terminalSinProcesar.toUpperCase();
        }
    }

    /**
     * Procesa los no terminales de la gramática del archivo.
     * 
     * @param reader El BufferedReader para leer el archivo.
     * @throws IOException Si ocurre un error de lectura.
     */
    private void procesarNoTerminales(BufferedReader reader) throws IOException {
        String line;
        int contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            } else if (line.contains("left") || line.contains("right")) {
                continue;
            }

            noTerminales.add(line.split("\\s+")[1]);
        }
    }

    /**
     * Procesa los estados de la gramática del archivo.
     * 
     * @param reader    El BufferedReader para leer el archivo.
     * @param firstLine La primera línea del estado.
     * @throws IOException Si ocurre un error de lectura.
     */
    private void procesarEstado(BufferedReader reader, String firstLine) throws IOException {
        int stateNumber = Integer.parseInt(firstLine.split("\\s+")[1]);
        Map<String, Accion> actionMap = new HashMap<>();
        Map<String, Integer> gotoMap = new HashMap<>();

        String line;
        int contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            } else {
                contadorLineasVacias = 0;
            }

            String[] parts = line.trim().split("\\s+");
            String simbolo = parts[0];
            if (parts[1].equals("shift,")) {
                int estado = Integer.parseInt(parts[6]);
                String token = procesarTerminal(parts[0]);
                AccionDesplazar accionDesplazar = new AccionDesplazar(estado, token);
                actionMap.put(procesarTerminal(simbolo), accionDesplazar);
            } else if (parts[1].equals("reduce")) {
                int regla = Integer.parseInt(parts[4].replaceAll("[^0-9]", ""));
                String noTerminal = parts[5].replaceAll("\\(|\\)", "");
                Integer numeroDesapilar = reglas.get(regla).size() - 1;
                AccionReducir accionReducir = new AccionReducir(regla, noTerminal, numeroDesapilar);
                actionMap.put(procesarTerminal(simbolo), accionReducir);
            } else if (parts[1].equals("accept")) {
                AccionAceptar accionAceptar = new AccionAceptar();
                actionMap.put(procesarTerminal(simbolo), accionAceptar);
            } else if (parts[1].equals("go")) {
                int goToState = Integer.parseInt(parts[4]);
                gotoMap.put(simbolo, goToState);
            }

        }

        tablaAccion.put(stateNumber, actionMap);
        tablaGoTo.put(stateNumber, gotoMap);
    }

    /**
     * Obtiene la tabla de acción.
     * 
     * @return La tabla de acción.
     */
    public Map<Integer, Map<String, Accion>> getTablaAccion() {
        return this.tablaAccion;
    }

    /**
     * Obtiene la tabla GoTo.
     * 
     * @return La tabla GoTo.
     */
    public Map<Integer, Map<String, Integer>> getTablaGoTo() {
        return this.tablaGoTo;
    }

    /**
     * Reinicia el Parser Gramatica a su estado inicial.
     */
    public void resetParserGramatica() {
        tablaAccion = new HashMap<>();
        tablaGoTo = new HashMap<>();
        reglas = new HashMap<>();
        terminales = new HashSet<>();
        noTerminales = new HashSet<>();

        generarTabla(FILE_PATH);
    }
}
