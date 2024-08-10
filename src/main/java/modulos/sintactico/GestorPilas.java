package modulos.sintactico;

import java.util.Stack;

/**
 * Clase GestorPilas que maneja las pilas de estados y símbolos
 * para el análisis sintáctico de un lenguaje.
 * Proporciona métodos para acceder y manipular dichas pilas.
 */
public class GestorPilas {

    private Stack<Integer> pilaEstados;
    private Stack<String> pilaSimbolos;

    private static final String FIN_DE_FICHERO = "FINDEFICHERO";

    // Instancia única de la clase
    private static GestorPilas instancia;

    /**
     * Constructor privado de la clase GestorPilas.
     * Inicializa las pilas de estados y símbolos, y agrega el estado inicial (0)
     * y el símbolo de fin de fichero.
     */
    private GestorPilas() {
        this.pilaEstados = new Stack<>();
        this.pilaSimbolos = new Stack<>();
        pilaEstados.push(0);
        pilaSimbolos.push(FIN_DE_FICHERO);
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GestorPilas.
     */
    public static synchronized GestorPilas getInstance() {
        if (instancia == null) {
            instancia = new GestorPilas();
        }
        return instancia;
    }

    /**
     * Devuelve la pila de estados.
     * 
     * @return La pila de estados.
     */
    public Stack<Integer> getPilaEstados() {
        return pilaEstados;
    }

    /**
     * Devuelve la pila de símbolos.
     * 
     * @return La pila de símbolos.
     */
    public Stack<String> getPilaSimbolos() {
        return pilaSimbolos;
    }

    /**
     * Reinicia el gestor de pilas a su estado inicial.
     */
    public void resetGestorPilas() {
        this.pilaEstados = new Stack<>();
        this.pilaSimbolos = new Stack<>();
        pilaEstados.push(0);
        pilaSimbolos.push(FIN_DE_FICHERO);
    }
}
