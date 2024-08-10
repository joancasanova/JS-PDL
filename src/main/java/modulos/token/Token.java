package modulos.token;

/**
 * Clase Token que representa un token en el análisis léxico y sintáctico.
 * Un token es una unidad de información que incluye un tipo y un atributo
 * asociado.
 */
public class Token {
    private TipoToken tipo;
    private Object atributo;

    /**
     * Constructor para crear un nuevo token con un tipo y un atributo.
     * 
     * @param tipo     El tipo del token.
     * @param atributo El atributo asociado al token.
     */
    public Token(TipoToken tipo, Object atributo) {
        this.tipo = tipo;
        this.atributo = atributo;
    }

    /**
     * Obtiene el atributo del token.
     * 
     * @return El atributo del token.
     */
    public Object getAtributo() {
        return this.atributo;
    }

    /**
     * Obtiene el tipo del token.
     * 
     * @return El tipo del token.
     */
    public TipoToken getTipo() {
        return tipo;
    }

    /**
     * Devuelve una representación en cadena del token.
     * 
     * @return Una cadena que representa el token en el formato <tipo, atributo>.
     */
    @Override
    public String toString() {
        return "<" + tipo + ", " + atributo + ">";
    }
}
