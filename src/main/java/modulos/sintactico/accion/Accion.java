package modulos.sintactico.accion;

/**
 * Clase abstracta que representa una acción sintáctica.
 */
public abstract class Accion {

    /**
     * Ejecuta la acción sobre el gestor de pilas.
     * 
     * @return El número de la regla aplicada, si corresponde.
     */
    public abstract Integer ejecutar();
}
