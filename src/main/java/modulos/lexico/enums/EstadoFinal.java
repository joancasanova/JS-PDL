package modulos.lexico.enums;

/**
 * Enumeración de posibles estados finales tras el análisis de un lexema.
 * Estos estados indican el tipo de token que se ha identificado durante el
 * análisis léxico de un programa.
 */
public enum EstadoFinal {
    PENDIENTE,
    FINDEFICHERO,
    FINCOMENTARIO,
    NEGACION,
    SUMA,
    ASIGNACIONSUMA,
    ASIGNACION,
    COMPARADOR,
    IDENTIFICADOR,
    PALABRARESERVADA,
    ENTERO,
    CADENA,
    COMA,
    PUNTOCOMA,
    ABREPARENTESIS,
    CIERRAPARENTESIS,
    ABRECORCHETE,
    CIERRACORCHETE
}
