package modulos.lexico.enums;

/**
 * Enumeración de posibles estados de transición durante el análisis léxico.
 * Estos estados representan los diferentes pasos intermedios en los que puede
 * encontrarse el analizador léxico mientras procesa un conjunto de caracteres.
 */
public enum EstadoTransito {
    INICIO, // Estado inicial del análisis léxico
    COMENTARIO, // Estado al inicio de un comentario
    TEXTOCOMENTARIO, // Estado dentro de un comentario
    SIMBOLOIGUAL, // Estado después de encontrar un símbolo de igual
    SIMBOLOSUMA, // Estado después de encontrar un símbolo de suma
    LEXEMA, // Estado al procesar un identificador o palabra reservada
    CARACTERNUMERICO, // Estado al procesar un número
    TEXTOCADENA // Estado al procesar una cadena de texto
}
