package modulos.semantico;

import java.util.ArrayList;

import modulos.tablaSimbolos.enums.Modo;
import modulos.tablaSimbolos.enums.Tipo;

/**
 * Clase GestorParametros que gestiona los parámetros de las funciones
 */
public class GestorParametros {

    private static GestorParametros instancia;

    private ArrayList<Tipo> tipoParametrosFuncion;
    private ArrayList<Modo> modoPasoParametros;
    private ArrayList<Tipo> listaDeParametros;

    /**
     * Constructor privado de la clase GestorParametros.
     * Inicializa las listas de tipos y modos de paso de los parámetros.
     */
    private GestorParametros() {
        this.tipoParametrosFuncion = new ArrayList<>();
        this.modoPasoParametros = new ArrayList<>();
        this.listaDeParametros = new ArrayList<>();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     *
     * @return La instancia única de GestorParametros.
     */
    public static synchronized GestorParametros getInstance() {
        if (instancia == null) {
            instancia = new GestorParametros();
        }
        return instancia;
    }

    /**
     * Obtiene la lista de tipos de los parámetros de la función.
     *
     * @return La lista de tipos de los parámetros de la función.
     */
    public ArrayList<Tipo> getTipoParametrosFuncion() {
        return tipoParametrosFuncion;
    }

    /**
     * Obtiene la lista de modos de paso de los parámetros de la función.
     *
     * @return La lista de modos de paso de los parámetros de la función.
     */
    public ArrayList<Modo> getModoPasoParametros() {
        return modoPasoParametros;
    }

    /**
     * Obtiene la lista de parámetros.
     *
     * @return La lista de parámetros.
     */
    public ArrayList<Tipo> getListaDeParametros() {
        return listaDeParametros;
    }

    /**
     * Añade un parámetro a las listas de parámetros de la función.
     *
     * @param tipo El tipo de parámetro a añadir.
     */
    public void addParametroFuncion(Tipo tipo, Modo modo) {
        tipoParametrosFuncion.add(tipo);
        modoPasoParametros.add(modo);
    }

    /**
     * Añade un tipo de parámetro a la lista de parámetros.
     *
     * @param tipo El tipo de parámetro a añadir.
     */
    public void addParametro(Tipo tipo) {
        listaDeParametros.add(tipo);
    }

    /**
     * Obtiene el número de parámetros de la función.
     *
     * @return El número de parámetros de la función.
     */
    public int getNumeroParametrosFuncion() {
        return tipoParametrosFuncion.size();
    }

    /**
     * Reinicia las listas de tipos y modos de paso de los parámetros a su estado
     * inicial.
     */
    public void reset() {
        tipoParametrosFuncion.clear();
        modoPasoParametros.clear();
        listaDeParametros.clear();
    }
}
