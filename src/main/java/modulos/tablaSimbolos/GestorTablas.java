package modulos.tablaSimbolos;

import modulos.tablaSimbolos.enums.Tipo;
import util.GestorErrores;

import java.util.Stack;

/**
 * Clase GestorTablas que gestiona las tablas de símbolos en un compilador.
 */
public class GestorTablas {

    private static final int BYTES_STRING = 128;
    private static final int BYTES_INT = 2;
    private static final int BYTES_BOOLEAN = 2;
    private static final int BYTES_VOID = 0;

    private Stack<TablaSimbolos> tablas;
    private Boolean tablaGlobal;
    private int numeroDeTablas;

    // Almacena el texto de impresión de las tablas que se van creando
    private StringBuilder impresionTabla;

    // Instancia única de la clase
    private static GestorTablas instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorTablas() {
        numeroDeTablas = 0;
        tablas = new Stack<TablaSimbolos>();
        tablas.add(new TablaSimbolos(numeroDeTablas));
        impresionTabla = new StringBuilder();
        tablaGlobal = true;
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GestorTablas.
     */
    public static synchronized GestorTablas getInstance() {
        if (instancia == null) {
            instancia = new GestorTablas();
        }
        return instancia;
    }

    /**
     * Crea una nueva tabla de símbolos y la añade a la lista de tablas.
     */
    public void nuevaTabla() {
        tablaGlobal = false;
        numeroDeTablas += 1;
        tablas.push(new TablaSimbolos(numeroDeTablas));
    }

    /**
     * Destruye la tabla de símbolos actual y la elimina de la lista de tablas.
     */
    public void destruirTabla() {
        tablaGlobal = true;
        TablaSimbolos ts = tablas.pop();
        impresionTabla.append(ts.imprimirTabla());
    }

    /**
     * Obtiene la impresión de la tabla de símbolos.
     * 
     * @String Un String con la impresión de la tabla.
     */
    public String getImpresionTablas() {
        return this.impresionTabla.toString();
    }

    /**
     * Obtiene la tabla de símbolos actual.
     * 
     * @return La tabla de símbolos actual.
     */
    public TablaSimbolos obtenerTablaActual() {
        return tablas.peek();
    }

    /**
     * Obtiene la tabla de símbolos global.
     * 
     * @return La tabla de símbolos global.
     */
    public TablaSimbolos obtenerTablaGlobal() {
        return tablas.get(0);
    }

    /**
     * Obtiene si la tabla actual es global o local.
     * 
     * @return true si es la tabla global.
     */
    public Boolean isTablaGlobal() {
        return tablaGlobal;
    }

    /**
     * Asigna un tipo a un símbolo.
     *
     * @param simbolo El símbolo al que se le asignará el tipo.
     * @param tipo    El tipo a asignar.
     */
    public void asignarTipo(Simbolo simbolo, Tipo tipo) {
        TablaSimbolos tabla = obtenerTablaActual();

        if (simbolo.getTipo() != null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_REDECLARADA);
        }
        simbolo.setTipo(tipo);
        simbolo.setBytes(calcularBytes(tipo));

        if (simbolo.getDesplazamiento() == null) {
            simbolo.setDesplazamiento(tabla.getDesplazamiento());
            tabla.setDesplazamiento(tabla.getDesplazamiento() + simbolo.getBytes());
        }
    }

    /**
     * Calcula el numero de bytes de un tipo.
     *
     * @param tipo El tipo cuyo ancho se va a calcular.
     * @return El numero de bytes que ocupa un tipo en memoria.
     */
    private int calcularBytes(Tipo tipo) {
        switch (tipo) {
            case STRING:
                return BYTES_STRING;
            case BOOLEAN:
                return BYTES_BOOLEAN;
            case INT:
                return BYTES_INT;
            case VOID:
                return BYTES_VOID;
            default:
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                return -1;
        }
    }

    /**
     * Reinicia el gestor de tablas a su estado inicial.
     */
    public void resetGestorTablas() {
        numeroDeTablas = 0;
        tablas = new Stack<TablaSimbolos>();
        tablas.add(new TablaSimbolos(numeroDeTablas));
        impresionTabla = new StringBuilder();
        tablaGlobal = true;
    }
}
