package modulos.token;

/**
 * Enumeración TipoToken que representa los diferentes tipos de tokens que
 * pueden ser identificados durante el análisis léxico.
 * Cada tipo de token tiene un símbolo asociado que lo representa
 * correspondiente al archivo de gramatica.txt (en el directorio resources)
 */
public enum TipoToken {
    SUMA("'+'"),
    NEGACION("'!'"),
    COMA("','"),
    PUNTOCOMA("';'"),
    ABREPARENTESIS("'('"),
    CIERRAPARENTESIS("')'"),
    ABRECORCHETE("'{'"),
    CIERRACORCHETE("'}'"),
    ASIGNACION("'='"),
    COMPARADOR("EQ_OP"),
    ASIGNACIONSUMA("ADD_OP"),
    FINDEFICHERO("$end"),
    ENTERO("ENTERO"),
    CADENA("CADENA"),
    ID("ID"),
    PALABRARESERVADA("");

    private final String symbol;

    /**
     * Constructor para inicializar un tipo de token con su símbolo asociado.
     * 
     * @param symbol El símbolo asociado al tipo de token.
     */
    TipoToken(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Procesa un texto y devuelve el tipo de token correspondiente.
     * 
     * @param texto El texto a procesar.
     * @return El tipo de token correspondiente al texto, o null si no se encuentra.
     */
    public static TipoToken procesarTipoToken(String texto) {
        for (TipoToken tipoToken : TipoToken.values()) {
            if (tipoToken.symbol.equalsIgnoreCase(texto)) {
                return tipoToken;
            }
        }
        return null;
    }
}
