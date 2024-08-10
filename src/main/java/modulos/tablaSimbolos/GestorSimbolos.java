package modulos.tablaSimbolos;

import java.util.Stack;

import util.GestorErrores;

import java.util.LinkedList;

/**
 * Gestiona los simbolos que se van encontrando en el input para su asignación y
 * comprobación de tipos.
 */
public class GestorSimbolos {

    // Simbolos que aún no tienen un tipo asignado (FIFO)
    private LinkedList<Simbolo> simbolosSinTipo;

    // Ultimos simbolos insertados en la tabla (LIFO)
    private Stack<Simbolo> ultimosSimbolos;

    // Instancia única de la clase
    private static GestorSimbolos instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorSimbolos() {
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GestorSimbolos.
     */
    public static synchronized GestorSimbolos getInstance() {
        if (instancia == null) {
            instancia = new GestorSimbolos();
        }
        return instancia;
    }

    /**
     * Añade a la lista de últimos símbolos añadidos un símbolo.
     * Si el simbolo no tiene tipo, se añade a la lista de simbolos sin tipo.
     * 
     * @param simbolo El símbolo a añadir.
     */
    public void setUltimoSimbolo(Simbolo simbolo) {
        ultimosSimbolos.push(simbolo);

        if (simbolo.getTipo() == null) {
            simbolosSinTipo.add(simbolo);
        }
    }

    /**
     * Obtiene el último símbolo insertado en la tabla.
     * 
     * @return El último símbolo insertado.
     */
    public Simbolo getUltimoSimbolo() {
        return ultimosSimbolos.pop();
    }

    /**
     * Obtiene el último símbolo de función insertado en la tabla.
     * 
     * @return El último símbolo de función.
     */
    public Simbolo getUltimoSimboloFuncion() {
        Stack<Simbolo> tempStack = new Stack<>();
        Simbolo simbolo = null;

        // Buscar el elemento mientras la pila original no esté vacía
        while (!ultimosSimbolos.isEmpty()) {
            Simbolo elemento = ultimosSimbolos.pop();
            tempStack.push(elemento);

            if (elemento.getNumeroParametros() != null) {
                simbolo = elemento;
                tempStack.pop();
                break;
            }
        }

        // Restaurar el stack original
        while (!tempStack.isEmpty()) {
            ultimosSimbolos.push(tempStack.pop());
        }

        return simbolo;
    }

    /**
     * Consume y devuelve un símbolo sin tipo de la lista.
     * 
     * @return El símbolo sin tipo.
     */
    public Simbolo consumirSimboloSinTipo() {
        if (simbolosSinTipo.size() < 1) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_REDECLARADA);
        }

        Simbolo simboloSinTipo = null;

        if (GestorZonasEspeciales.getInstance().getZonaParametros()) {
            simboloSinTipo = simbolosSinTipo.removeLast();
        } else {
            simboloSinTipo = simbolosSinTipo.removeFirst();
        }

        return simboloSinTipo;
    }

    /**
     * Elimina un simbolo sin tipo de la lista.
     *
     * @param simbolo El simbolo que se desea eliminar de la lista.
     */
    public void eliminarSimboloSinTipo(Simbolo simbolo) {
        simbolosSinTipo.remove(simbolo);
    }

    /**
     * Reinicia el gestor de simbolos a su estado inicial.
     */
    public void resetGestorSimbolos() {
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
    }
}
