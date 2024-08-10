package modulos.lexico;

import modulos.lexico.enums.EstadoFinal;
import modulos.lexico.enums.EstadoTransito;
import modulos.token.*;

/**
 * Clase que implementa un analizador léxico para un lenguaje de programación.
 * Esta clase es responsable de leer un archivo fuente y generar tokens a partir
 * de los lexemas identificados. Utiliza un enfoque de análisis caracter por
 * caracter para identificar los diferentes tipos de tokens.
 */
public class AnalizadorLexico {

    // Almacena los caracteres leídos para la formación de tokens
    private StringBuilder bufferCaracteres;

    // Controla si el caracter se ha terminado de procesar
    private Boolean caracterProcesado;

    // Gestor de estados para el análisis léxico
    private GestorEstados gestorEstados;

    // Generador de tokens en base a los estados y lexemas identificados
    private GeneradorToken generadorDeTokens;

    // Instancia única de la clase
    private static AnalizadorLexico instancia;

    /**
     * Constructor privado que inicializa el analizador léxico.
     * Inicializa un StringBuilder para el buffer de caracteres y crea instancias
     * de GestorEstados y GeneradorToken.
     */
    private AnalizadorLexico() {
        this.caracterProcesado = false;
        this.bufferCaracteres = new StringBuilder();
        this.gestorEstados = GestorEstados.getInstance();
        this.generadorDeTokens = GeneradorToken.getInstance();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de AnalizadorLexico.
     */
    public static AnalizadorLexico getInstance() {
        if (instancia == null) {
            synchronized (AnalizadorLexico.class) {
                if (instancia == null) {
                    instancia = new AnalizadorLexico();
                }
            }
        }
        return instancia;
    }

    /**
     * Procesa un caracter y actualiza el estado del analizador léxico.
     * 
     * @param caracterPorProcesar Caracter a procesar.
     * @return Token identificado tras procesar el caracter.
     */
    public Token procesarCaracter(Character caracterPorProcesar) {
        caracterProcesado = false;

        String lexema = bufferCaracteres.toString();

        // Actualiza el estado según el caracter actual entrante
        gestorEstados.actualizarEstado(caracterPorProcesar, lexema);

        // Generar token y almacenarlo si no es nulo
        Token token = generadorDeTokens.generarToken(
                gestorEstados.getEstadoFinal(),
                caracterPorProcesar,
                lexema);

        // Actualizar buffer de caracteres dependiendo de si es estado inicial o no
        if (gestorEstados.getEstadoTransito() == EstadoTransito.INICIO) {
            bufferCaracteres.setLength(0);
        } else {
            bufferCaracteres.append(caracterPorProcesar);
        }

        // Consumir el caracter si el analizador no se encuentra en ciertos estados
        if (debeConsumirCaracter(gestorEstados.getEstadoFinal())) {
            caracterProcesado = true;
            caracterPorProcesar = null;
        }

        return token;
    }

    public Boolean isCaracterProcesado() {
        return caracterProcesado;
    }

    /**
     * Determina si se debe consumir el caracter actual en base al estado final.
     * 
     * @param estado Estado final actual.
     * @return true si se debe consumir el caracter, false en caso contrario.
     */
    private boolean debeConsumirCaracter(EstadoFinal estado) {
        return !(estado == EstadoFinal.SUMA || estado == EstadoFinal.ASIGNACION ||
                estado == EstadoFinal.IDENTIFICADOR || estado == EstadoFinal.ENTERO ||
                estado == EstadoFinal.PALABRARESERVADA);
    }

    /**
     * Reinicia el AnalizadorLexico a su estado inicial.
     */
    public void resetAnalizadorLexico() {
        this.caracterProcesado = false;
        this.bufferCaracteres = new StringBuilder();
        gestorEstados.resetGestorEstados();
        generadorDeTokens.resetGeneradorToken();
        this.gestorEstados = GestorEstados.getInstance();
        this.generadorDeTokens = GeneradorToken.getInstance();
    }
}
