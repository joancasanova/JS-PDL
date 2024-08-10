package modulos.tablaSimbolos;

/**
 * Gestiona si se encuentra en una zona de parametros o de declaracion
 */
public class GestorZonasEspeciales {

    // Indica si el programa se encuentra en la zona de parametros de una funcion
    private Boolean zonaParametros;

    // Indica si el programa se encuentra en una sentencia de declaracion
    private Boolean zonaDeclaracion;

    // Instancia única de la clase
    private static GestorZonasEspeciales instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorZonasEspeciales() {
        zonaParametros = false;
        zonaDeclaracion = false;
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GestorZonas.
     */
    public static synchronized GestorZonasEspeciales getInstance() {
        if (instancia == null) {
            instancia = new GestorZonasEspeciales();
        }
        return instancia;
    }

    /**
     * Obtiene el estado de la zona de declaración.
     * 
     * @return true si está en zona de declaración, false en caso contrario.
     */
    public Boolean getZonaDeclaracion() {
        return zonaDeclaracion;
    }

    /**
     * Establece el estado de la zona de declaración.
     * 
     * @param value true para activar la zona de declaración, false para
     *              desactivarla.
     */
    public void setZonaDeclaracion(Boolean value) {
        zonaDeclaracion = value;
    }

    /**
     * Obtiene el estado de la zona de parámetros.
     * 
     * @return true si está en zona de parámetros, false en caso contrario.
     */
    public Boolean getZonaParametros() {
        return zonaParametros;
    }

    /**
     * Establece el estado de la zona de parámetros.
     * 
     * @param value true para activar la zona de parámetros, false para
     *              desactivarla.
     */
    public void setZonaParametros(Boolean value) {
        zonaParametros = value;
    }

    /**
     * Reinicia el gestor de zonas a su estado inicial.
     */
    public void resetGestorZonas() {
        zonaParametros = false;
        zonaDeclaracion = false;
    }
}
