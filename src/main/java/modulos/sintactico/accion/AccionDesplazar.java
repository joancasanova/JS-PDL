package modulos.sintactico.accion;

import modulos.sintactico.GestorPilas;

/**
 * Clase que representa la acción de desplazamiento en el análisis sintáctico.
 */
public class AccionDesplazar extends Accion {
    private Integer estado;
    private String token;

    /**
     * Constructor de AccionDesplazar.
     * 
     * @param estado El estado al que se desplaza.
     * @param token  El token que se desplaza.
     */
    public AccionDesplazar(Integer estado, String token) {
        this.estado = estado;
        this.token = token;
    }

    /**
     * Ejecuta la acción de desplazamiento sobre el gestor de pilas.
     * 
     * @param gestorPilas El gestor de pilas que maneja los estados y símbolos.
     * @return Siempre retorna null, ya que no aplica una regla.
     */
    @Override
    public Integer ejecutar() {
        GestorPilas gestorPilas = GestorPilas.getInstance();
        gestorPilas.getPilaEstados().push(estado);
        gestorPilas.getPilaSimbolos().push(token);
        return null;
    }
}
