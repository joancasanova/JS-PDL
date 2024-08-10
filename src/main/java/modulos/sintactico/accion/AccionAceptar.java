package modulos.sintactico.accion;

/**
 * Clase que representa la acción de aceptación en el análisis sintáctico.
 */
public class AccionAceptar extends Accion {
    /**
     * Constructor de AccionAceptar.
     */
    public AccionAceptar() {
    }

    /**
     * Ejecuta la acción de aceptación sobre el gestor de pilas.
     * 
     * @return El número de la regla aplicada, que en este caso es 1.
     */
    @Override
    public Integer ejecutar() {
        return 1;
    }
}
