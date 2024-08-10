package modulos.sintactico;

import java.util.Map;

import modulos.sintactico.accion.Accion;
import modulos.sintactico.accion.AccionAceptar;
import modulos.sintactico.accion.AccionDesplazar;
import modulos.token.*;
import util.GestorErrores;

/**
 * Clase AnalizadorSintactico para procesar tokens y aplicar reglas de análisis
 * sintáctico LR(1).
 */
public class AnalizadorSintactico {

    private GestorPilas gestorPilas;
    private Boolean tokenProcesado;

    // Instancia única de la clase
    private static AnalizadorSintactico instancia;

    /**
     * Constructor privado del analizador sintáctico para evitar la creación de
     * instancias.
     */
    private AnalizadorSintactico() {
        this.tokenProcesado = false;
        gestorPilas = GestorPilas.getInstance();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de AnalizadorSintactico.
     */
    public static synchronized AnalizadorSintactico getInstance() {
        if (instancia == null) {
            instancia = new AnalizadorSintactico();
        }
        return instancia;
    }

    /**
     * Procesa un token y aplica las reglas sintácticas correspondientes.
     * 
     * @param token El token a procesar.
     * @return Lista de reglas aplicadas durante el procesamiento.
     */
    public Integer procesarToken(Token token) {
        tokenProcesado = false;
        Accion accion = obtenerAccion(token);

        Integer reglaAplicada = accion.ejecutar();

        // Si la acción es de aceptación, finalizar el procesamiento
        if (accion instanceof AccionAceptar) {
            tokenProcesado = true;
        }

        // Caso especial para el fin de fichero sin aceptación: continuar procesando
        if (token.getTipo().equals(TipoToken.FINDEFICHERO)) {
            return reglaAplicada;
        }

        // En caso de ser una acción de desplazar, ir a por el siguiente token
        if (accion instanceof AccionDesplazar) {
            tokenProcesado = true;
        }

        return reglaAplicada;
    }

    /**
     * Obtiene la acción correspondiente a un token y un estado.
     * 
     * @param token El token.
     * @return La acción correspondiente.
     */
    private Accion obtenerAccion(Token token) {
        String textoToken = obtenerContenidoToken(token);
        Integer estadoCima = gestorPilas.getPilaEstados().peek();
        Map<String, Accion> accionesEstado = ParserGramatica.getInstance().getTablaAccion().get(estadoCima);
        Accion accion = accionesEstado.getOrDefault(textoToken, accionesEstado.get("$DEFAULT"));

        if (accion == null) {
            GestorErrores.lanzarError(
                    GestorErrores.TipoError.SINTACTICO,
                    GestorErrores.ERROR_TOKEN_NO_ESPERADO + textoToken);
        }

        return accion;
    }

    /**
     * Obtiene el contenido del token procesado en la posición actual del análisis.
     * Si el token es una palabra reservada, devuelve su atributo en mayúsculas.
     * 
     * @param token El token a procesar.
     * @return El contenido del token.
     */
    private String obtenerContenidoToken(Token token) {
        // Se devuelve el tipo o el atributo dependiendo de si es palabra reservada
        return token.getTipo().equals(TipoToken.PALABRARESERVADA) ? String.valueOf(token.getAtributo()).toUpperCase()
                : String.valueOf(token.getTipo()).toUpperCase();
    }

    public boolean isTokenProcesado() {
        return this.tokenProcesado;
    }

    /**
     * Reinicia el analizador sintactico a su estado inicial.
     */
    public void resetAnalizadorSintactico() {
        this.tokenProcesado = false;
        ParserGramatica.getInstance().resetParserGramatica();
        gestorPilas.resetGestorPilas();
        gestorPilas = GestorPilas.getInstance();
    }

}
