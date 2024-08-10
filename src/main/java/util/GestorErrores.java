package util;

/**
 * Clase que contiene mensajes de error constantes para la aplicación,
 * agrupados por tipo de error: léxicos, sintácticos, semánticos y genéricos.
 */
public final class GestorErrores {

    private static Integer linea = 1;

    // Enum para los tipos de errores
    public enum TipoError {
        LEXICO, SINTACTICO, SEMANTICO, GENERICO
    }

    // Mensajes de error léxicos
    public static final String PALABRA_RESERVADA_MINUSCULAS = "Las palabras reservadas deben ser escritas en minusculas: ";
    public static final String CADENA_LARGA = "Cadena demasiado larga: ";
    public static final String CADENA_SALTO_LINEA = "Cadena no puede contener salto de linea";
    public static final String ENTERO_MAXIMO = "Se ha superado el valor numerico maximo. Valor: ";
    public static final String ESTADO_FINAL_NO_MANEJADO = "Estado final no manejado: ";
    public static final String CARACTER_NO_ESPERADO_COMENTARIO = "Caracter no esperado. Se esperaba /";
    public static final String CARACTER_NO_ESPERADO = "Error al procesar el caracter actual: ";

    // Mensajes de error sintácticos
    public static final String ERROR_TOKEN_NO_ESPERADO = "Token no esperado: ";

    // Mensajes de error semánticos
    public static final String ERROR_TIPO_BOOLEAN = "La expresion deberia ser de tipo booleano";
    public static final String ERROR_TIPOS_NO_COINCIDEN = "Los tipos de la expresion no coinciden";
    public static final String ERROR_TIPO_NO_COMPATIBLE = "Tipo de la expresion no compatible";
    public static final String ERROR_LLAMADA_FUNCION_NO_DECLARADA = "Se esta haciendo una llamada a una funcion no declarada";
    public static final String ERROR_VARIABLE_REDECLARADA = "Se esta redeclarando una variable";
    public static final String ERROR_TIPO_RETORNO_FUNCION = "Tipo retorno de la funcion y tipo de funcion no coinciden";
    public static final String ERROR_NUMERO_PARAMETROS = "El numero de parametros no coinciden con los de la funcion";
    public static final String ERROR_TIPO_PARAMETROS = "El tipos de parametros no coinciden con los de la funcion";
    public static final String ERROR_REGLA_NO_IMPLEMENTADA = "Se ha recibido una regla no esperada";

    // Mensajes de error genéricos
    public static final String ARCHIVO_GRAMATICA_NO_ENCONTRADO = "El archivo de gramatica.output no se ha encontrado (directorio resources)";

    // Constructor privado para evitar instanciación
    private GestorErrores() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no debe ser instanciada.");
    }

    /**
     * Lanza una excepción con el mensaje de error especificado y el tipo de error.
     *
     * @param tipoError El tipo de error.
     * @param mensaje   El mensaje de error.
     */
    public static void lanzarError(TipoError tipoError, String mensaje) {
        String tipo = tipoError.name().toLowerCase();
        throw new IllegalStateException("Error " + tipo + ": " + mensaje + " en linea " + linea);
    }

    public static void incrementarLinea() {
        linea++;
    }
}
