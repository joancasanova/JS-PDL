package modulos.lexico.enums;

/**
 * Enumeración de las palabras reservadas en el lenguaje de programación.
 * Esta enumeración contiene todas las palabras clave que tienen un significado
 * especial en el lenguaje y que no pueden ser utilizadas como identificadores.
 */
public enum PalabraReservada {
    BOOLEAN,
    FUNCTION,
    IF,
    INT,
    LET,
    PUT,
    RETURN,
    STRING,
    VOID,
    WHILE,
    GET;

    /**
     * Verifica si un texto dado corresponde a una palabra reservada del lenguaje.
     * 
     * @param texto El texto a verificar.
     * @return true si el texto es una palabra reservada, false en caso contrario.
     */
    public static Boolean contiene(String texto) {
        for (PalabraReservada palabraReservada : PalabraReservada.values()) {
            if (palabraReservada.name().equalsIgnoreCase(texto)) {
                return true;
            }
        }
        return false;
    }
}
