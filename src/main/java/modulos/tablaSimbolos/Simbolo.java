package modulos.tablaSimbolos;

import java.util.List;

import modulos.tablaSimbolos.enums.Modo;
import modulos.tablaSimbolos.enums.Tipo;

/**
 * Clase que representa un símbolo en la tabla de símbolos.
 * Los símbolos pueden ser variables o funciones y contienen información sobre
 * su tipo, nombre, desplazamiento y parámetros.
 */
public class Simbolo {

    private String nombre;
    private Tipo tipo;
    private Tipo tipoRetorno;

    // Parámetros para los símbolos normales
    private Integer desplazamiento;
    private Integer bytes;

    // Parámetros para los símbolos de las funciones
    private Integer numeroParametros;
    private List<Tipo> tipoParametros;
    private List<Modo> modoPaso;

    /**
     * Constructor para crear un símbolo.
     * 
     * @param tipo           El tipo del símbolo.
     * @param nombre         El nombre del símbolo.
     * @param desplazamiento El desplazamiento del símbolo en la memoria.
     * @param bytes          El ancho del símbolo en la memoria.
     */
    public Simbolo(Tipo tipo, String nombre, Integer desplazamiento, Integer bytes) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.desplazamiento = desplazamiento;
        this.bytes = bytes;
        this.tipoRetorno = null;
    }

    /**
     * Obtiene el nombre del símbolo.
     * 
     * @return El nombre del símbolo.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene el tipo del símbolo.
     * 
     * @return El tipo del símbolo.
     */
    public Tipo getTipo() {
        return this.tipo;
    }

    /**
     * Establece el tipo del símbolo.
     * 
     * @param tipo El tipo del símbolo.
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el ancho del símbolo en la memoria.
     * 
     * @return El ancho del símbolo.
     */
    public Integer getBytes() {
        return this.bytes;
    }

    /**
     * Establece el ancho del símbolo en la memoria.
     * 
     * @param ancho El ancho del símbolo.
     */
    public void setBytes(Integer ancho) {
        this.bytes = ancho;
    }

    /**
     * Obtiene el desplazamiento del símbolo en la memoria.
     * 
     * @return El desplazamiento del símbolo.
     */
    public Integer getDesplazamiento() {
        return this.desplazamiento;
    }

    /**
     * Establece el desplazamiento del símbolo en la memoria.
     * 
     * @param desplazamiento El desplazamiento del símbolo.
     */
    public void setDesplazamiento(Integer desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

    /**
     * Obtiene el tipo de retorno del símbolo si es una función.
     * 
     * @return El tipo de retorno del símbolo.
     */
    public Tipo getTipoRetorno() {
        return this.tipoRetorno;
    }

    /**
     * Establece el tipo de retorno del símbolo si es una función.
     * 
     * @param retorno El tipo de retorno del símbolo.
     */
    public void setTipoRetorno(Tipo retorno) {
        this.tipoRetorno = retorno;
    }

    /**
     * Obtiene el número de parámetros del símbolo si es una función.
     * 
     * @return El número de parámetros del símbolo.
     */
    public Integer getNumeroParametros() {
        return this.numeroParametros;
    }

    /**
     * Establece el número de parámetros del símbolo si es una función.
     * 
     * @param numeroParametros El número de parámetros del símbolo.
     */
    public void setNumeroParametros(Integer numeroParametros) {
        this.numeroParametros = numeroParametros;
    }

    /**
     * Obtiene la lista de tipos de los parámetros del símbolo si es una función.
     * 
     * @return La lista de tipos de los parámetros del símbolo.
     */
    public List<Tipo> getTipoParametros() {
        return this.tipoParametros;
    }

    /**
     * Establece la lista de tipos de los parámetros del símbolo si es una función.
     * 
     * @param tipoParametros La lista de tipos de los parámetros del símbolo.
     */
    public void setTipoParametros(List<Tipo> tipoParametros) {
        this.tipoParametros = tipoParametros;
    }

    /**
     * Obtiene la lista de modos de paso de los parámetros del símbolo si es una
     * función.
     * 
     * @return La lista de modos de paso de los parámetros del símbolo.
     */
    public List<Modo> getModoPaso() {
        return this.modoPaso;
    }

    /**
     * Establece la lista de modos de paso de los parámetros del símbolo si es una
     * función.
     * 
     * @param modoPaso La lista de modos de paso de los parámetros del símbolo.
     */
    public void setModoPaso(List<Modo> modoPaso) {
        this.modoPaso = modoPaso;
    }
}
