package modulos.lexico;

import modulos.lexico.enums.EstadoFinal;
import modulos.lexico.enums.EstadoTransito;
import modulos.lexico.enums.PalabraReservada;
import util.GestorErrores;

/**
 * Clase GestorEstados que maneja los estados de transición y los estados
 * finales durante el análisis léxico de un conjunto de caracteres.
 */
public class GestorEstados {

    // Estado actual de transición durante el análisis léxico.
    private EstadoTransito estadoTransito;

    // Estado final alcanzado tras el análisis de una serie de caracteres.
    private EstadoFinal estadoFinal;

    // Instancia única de la clase
    private static GestorEstados instancia;

    /**
     * Constructor privado de GestorEstados. Inicializa el estado de transición y
     * el estado final.
     */
    private GestorEstados() {
        this.estadoTransito = EstadoTransito.INICIO;
        this.estadoFinal = EstadoFinal.PENDIENTE;
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GestorEstados.
     */
    public static GestorEstados getInstance() {
        if (instancia == null) {
            synchronized (GestorEstados.class) {
                if (instancia == null) {
                    instancia = new GestorEstados();
                }
            }
        }
        return instancia;
    }

    /**
     * Obtiene el estado actual de transición.
     * 
     * @return El estado actual de transición.
     */
    public EstadoTransito getEstadoTransito() {
        return estadoTransito;
    }

    /**
     * Obtiene el estado final alcanzado.
     * 
     * @return El estado final alcanzado.
     */
    public EstadoFinal getEstadoFinal() {
        return estadoFinal;
    }

    /**
     * Actualiza el estado de transición del analizador en base al carácter actual.
     * 
     * @param charActual El carácter actual a procesar.
     * @param lexema     El lexema actual a procesar.
     */
    public void actualizarEstado(Character charActual, String lexema) {

        estadoFinal = EstadoFinal.PENDIENTE;

        switch (estadoTransito) {
            case INICIO:
                procesarEstadoInicial(charActual);
                break;

            case COMENTARIO:
                if (charActual == '/') {
                    estadoTransito = EstadoTransito.TEXTOCOMENTARIO;
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO,
                            GestorErrores.CARACTER_NO_ESPERADO_COMENTARIO);
                }
                break;

            case TEXTOCOMENTARIO:
                if (charActual == '\n' || (int) charActual == 65535) {
                    estadoFinal = EstadoFinal.FINCOMENTARIO;
                }
                break;

            case SIMBOLOIGUAL:
                if (charActual == '=') {
                    estadoFinal = EstadoFinal.COMPARADOR;
                } else {
                    estadoFinal = EstadoFinal.ASIGNACION;
                }
                break;

            case SIMBOLOSUMA:
                if (charActual == '=') {
                    estadoFinal = EstadoFinal.ASIGNACIONSUMA;
                } else {
                    estadoFinal = EstadoFinal.SUMA;
                }
                break;

            case LEXEMA:
                if (!(Character.isLetterOrDigit(charActual) || charActual == '_')) {
                    if (PalabraReservada.contiene(lexema)) {
                        estadoFinal = EstadoFinal.PALABRARESERVADA;
                    } else {
                        estadoFinal = EstadoFinal.IDENTIFICADOR;
                    }
                }
                break;

            case CARACTERNUMERICO:
                if (!Character.isDigit(charActual)) {
                    estadoFinal = EstadoFinal.ENTERO;
                }
                break;

            case TEXTOCADENA:
                if (charActual == '\"') {
                    estadoFinal = EstadoFinal.CADENA;
                }
                break;
        }

        // En el caso de pasar a un estado final, regresar al estado de transito inicial
        if (estadoFinal != EstadoFinal.PENDIENTE) {
            estadoTransito = EstadoTransito.INICIO;
        }
    }

    /**
     * Procesa el estado inicial del analizador en base al carácter actual.
     * 
     * @param charActual El carácter actual a procesar.
     * @throws IllegalStateException Si se encuentra un error en el procesamiento
     *                               del estado inicial.
     */
    private void procesarEstadoInicial(Character charActual) {

        switch (charActual) {

            // Transiciones a estados finales
            case '!':
                estadoFinal = EstadoFinal.NEGACION;
                break;

            case ',':
                estadoFinal = EstadoFinal.COMA;
                break;

            case ';':
                estadoFinal = EstadoFinal.PUNTOCOMA;
                break;

            case '(':
                estadoFinal = EstadoFinal.ABREPARENTESIS;
                break;

            case ')':
                estadoFinal = EstadoFinal.CIERRAPARENTESIS;
                break;

            case '{':
                estadoFinal = EstadoFinal.ABRECORCHETE;
                break;

            case '}':
                estadoFinal = EstadoFinal.CIERRACORCHETE;
                break;

            // Transiciones a estados intermedios
            case '/':
                estadoTransito = EstadoTransito.COMENTARIO;
                break;

            case '+':
                estadoTransito = EstadoTransito.SIMBOLOSUMA;
                break;

            case '=':
                estadoTransito = EstadoTransito.SIMBOLOIGUAL;
                break;

            case '\"':
                estadoTransito = EstadoTransito.TEXTOCADENA;
                break;

            // Otros casos
            default:
                if ((int) charActual == 65535) {
                    estadoFinal = EstadoFinal.FINDEFICHERO;
                } else if (Character.isLetter(charActual) || charActual == '_') {
                    estadoTransito = EstadoTransito.LEXEMA;
                } else if (Character.isDigit(charActual)) {
                    estadoTransito = EstadoTransito.CARACTERNUMERICO;
                } else if (Character.isWhitespace(charActual) || charActual == '\t' || charActual == '\n') {
                    // Permanecer en el estado inicial
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO,
                            GestorErrores.CARACTER_NO_ESPERADO + charActual);
                }
                break;
        }
    }

    /**
     * Reinicia el GestorEstados a su estado inicial.
     */
    public void resetGestorEstados() {
        this.estadoTransito = EstadoTransito.INICIO;
        this.estadoFinal = EstadoFinal.PENDIENTE;
    }
}
