package modulos.lexico;

import java.util.ArrayList;
import java.util.List;

import modulos.lexico.enums.EstadoFinal;
import modulos.tablaSimbolos.GestorSimbolos;
import modulos.tablaSimbolos.GestorTablas;
import modulos.tablaSimbolos.GestorZonasEspeciales;
import modulos.tablaSimbolos.Simbolo;
import modulos.tablaSimbolos.TablaSimbolos;
import modulos.token.*;
import util.GestorErrores;

/**
 * Clase GeneradorToken que se encarga de generar tokens en base a los estados
 * finales y caracteres actuales durante el análisis léxico.
 */
public class GeneradorToken {

    // Token generado
    private Token token;

    // Gestor de tablas de simbolos
    private GestorTablas gestorTablas;
    private GestorSimbolos gestorSimbolos;
    private GestorZonasEspeciales gestorZonas;

    // Simbolos a la espera de ser enviados a Tabla de Simbolos
    private List<Simbolo> simbolosPorEnviar;

    // Ultimo token es PUNTOyCOMA
    private Boolean ultimoTokenPuntoComa;

    // Máximo número de caracteres en una cadena
    private static final int MAX_CARACTERES_CADENA = 64;

    // Máximo valor para una constante entera
    private static final int MAX_VALOR_ENTERO = 32767;

    // Instancia única de la clase
    private static GeneradorToken instancia;

    /**
     * Constructor privado por defecto de GeneradorToken.
     */
    private GeneradorToken() {
        this.gestorTablas = GestorTablas.getInstance();
        this.gestorSimbolos = GestorSimbolos.getInstance();
        this.gestorZonas = GestorZonasEspeciales.getInstance();
        this.ultimoTokenPuntoComa = false;
        this.simbolosPorEnviar = new ArrayList<>();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GeneradorToken.
     */
    public static GeneradorToken getInstance() {
        if (instancia == null) {
            synchronized (GeneradorToken.class) {
                if (instancia == null) {
                    instancia = new GeneradorToken();
                }
            }
        }
        return instancia;
    }

    /**
     * Procesa el token actual en base al estado final alcanzado.
     * Este método se encarga de generar un token apropiado según el estado final
     * del análisis léxico y el lexema actual.
     * 
     * @param estadoFinal El estado final alcanzado en el análisis léxico.
     * @param charActual  El carácter actual en el análisis.
     * @param lexema      El lexema actual a procesar.
     * @return El token procesado o null si no hay token.
     */
    public Token generarToken(EstadoFinal estadoFinal, Character charActual, String lexema) {

        token = null;

        switch (estadoFinal) {
            case PENDIENTE:
            case FINCOMENTARIO:
                // Emitir un token nulo
                break;

            case FINDEFICHERO:
            case NEGACION:
            case COMA:
            case PUNTOCOMA:
            case ABREPARENTESIS:
            case CIERRAPARENTESIS:
            case ABRECORCHETE:
            case CIERRACORCHETE:
            case COMPARADOR:
            case ASIGNACION:
            case ASIGNACIONSUMA:
            case SUMA:
                token = crearTokenSimple(estadoFinal);
                break;

            case PALABRARESERVADA:
                validarPalabraReservada(lexema);
                token = new Token(TipoToken.PALABRARESERVADA, lexema);
                break;

            case IDENTIFICADOR:
                token = procesarIdentificador(lexema);
                break;

            case CADENA:
                validarCadena(lexema);
                token = new Token(TipoToken.CADENA, lexema);
                break;

            case ENTERO:
                token = procesarEntero(lexema);
                break;

            default:
                GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO,
                        GestorErrores.ESTADO_FINAL_NO_MANEJADO + estadoFinal);
        }

        gestionarSimbolosPorEnviar();

        actualizarZonaDeclaracion(token);

        return token;
    }

    /**
     * Crea un token simple basado en el estado final proporcionado.
     * 
     * @param estadoFinal El estado final alcanzado en el análisis léxico.
     * @return El token creado.
     */
    private Token crearTokenSimple(EstadoFinal estadoFinal) {
        return new Token(TipoToken.valueOf(estadoFinal.name()), "");
    }

    /**
     * Valida que el lexema de una palabra reservada esté en minúsculas.
     * 
     * @param lexema El lexema a validar.
     * @throws IllegalStateException Si la palabra reservada no está en minúsculas.
     */
    private void validarPalabraReservada(String lexema) throws IllegalStateException {
        if (!lexema.chars().allMatch(Character::isLowerCase)) {
            GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO,
                    GestorErrores.PALABRA_RESERVADA_MINUSCULAS + lexema);
        }
    }

    /**
     * Procesa un identificador, gestionando su simbolo en las tablas de símbolos.
     * 
     * @param lexema El lexema del identificador.
     * @return El token del identificador.
     */
    private Token procesarIdentificador(String lexema) {
        String nombre = lexema;
        Simbolo simbolo;
        Integer posicionTS;

        simbolo = obtenerSimbolo(nombre);

        // Establecer símbolo como pendiente de gestionar
        if (!ultimoTokenPuntoComa) {
            gestorSimbolos.setUltimoSimbolo(simbolo);
        } else {
            simbolosPorEnviar.add(simbolo);
        }

        posicionTS = gestorTablas.obtenerTablaActual().obtenerPosicionSimbolo(simbolo);

        return new Token(TipoToken.ID, posicionTS);
    }

    /**
     * Obtiene el símbolo correspondiente al nombre del identificador, ya sea desde
     * la tabla actual o la tabla global.
     * 
     * @param nombre El nombre del identificador.
     * @return El símbolo correspondiente al identificador.
     */
    private Simbolo obtenerSimbolo(String nombre) {
        Simbolo simbolo;
        TablaSimbolos tablaActual = gestorTablas.obtenerTablaActual();
        TablaSimbolos tablaGlobal = gestorTablas.obtenerTablaGlobal();

        // Buscar simbolo en tabla actual
        if (tablaActual.simboloExiste(nombre)) {
            simbolo = tablaActual.obtenerSimboloPorNombre(nombre);
        }

        // Sino, buscar simbolo en tabla global
        // (siempre que no se esté en una zona especial)
        else if (tablaGlobal.simboloExiste(nombre)
                && !gestorZonas.getZonaDeclaracion()
                && !gestorZonas.getZonaParametros()) {
            simbolo = tablaGlobal.obtenerSimboloPorNombre(nombre);
        }

        // Sino, crear un nuevo simbolo
        else {
            simbolo = new Simbolo(null, nombre, null, null);
            tablaActual.agregarSimbolo(simbolo);
        }

        return simbolo;
    }

    /**
     * Valida que una cadena no exceda la longitud máxima y no contenga saltos de
     * línea.
     * 
     * @param lexema La cadena a validar.
     * @throws IllegalStateException Si la cadena es demasiado larga o contiene
     *                               saltos de línea.
     */
    private void validarCadena(String lexema) throws IllegalStateException {
        if (lexema.length() - 2 >= MAX_CARACTERES_CADENA) {
            GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO, GestorErrores.CADENA_LARGA + lexema);
        }
        if (lexema.contains("\n")) {
            GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO, GestorErrores.CADENA_SALTO_LINEA);
        }
    }

    /**
     * Procesa un entero, asegurando que no exceda el valor máximo permitido.
     * 
     * @param lexema El lexema que representa el entero.
     * @return El token del entero.
     * @throws IllegalStateException Si el entero excede el valor máximo permitido.
     */
    private Token procesarEntero(String lexema) throws IllegalStateException {
        Integer valorEntero = Integer.valueOf(lexema);

        // Se comprueba si el valor del entero no supera el valor máximo
        if (valorEntero <= MAX_VALOR_ENTERO) {
            return new Token(TipoToken.ENTERO, valorEntero);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.LEXICO, GestorErrores.ENTERO_MAXIMO + valorEntero);
        }
        return null;
    }

    /**
     * Gestiona la lista de símbolos por enviar, estableciéndolos en la tabla de
     * símbolos si es necesario.
     */
    private void gestionarSimbolosPorEnviar() {
        if (!ultimoTokenPuntoComa) {
            simbolosPorEnviar.forEach(gestorSimbolos::setUltimoSimbolo);
            simbolosPorEnviar.clear();
        }
    }

    /**
     * Actualiza el estado de la zona de declaración según el tipo de token actual.
     * 
     * @param token El token procesado.
     */
    private void actualizarZonaDeclaracion(Token token) {
        if (token != null) {
            if (TipoToken.PALABRARESERVADA.equals(token.getTipo()) && "let".equals(token.getAtributo())) {
                gestorZonas.setZonaDeclaracion(true);
            }
            if (TipoToken.PUNTOCOMA.equals(token.getTipo()) || TipoToken.ID.equals(token.getTipo())) {
                ultimoTokenPuntoComa = true;
                gestorZonas.setZonaDeclaracion(false);
            } else {
                ultimoTokenPuntoComa = false;
            }
        }
    }

    /**
     * Reinicia el GeneradorToken a su estado inicial.
     */
    public void resetGeneradorToken() {
        this.gestorTablas = GestorTablas.getInstance();
        this.gestorSimbolos = GestorSimbolos.getInstance();
        this.gestorZonas = GestorZonasEspeciales.getInstance();
        this.ultimoTokenPuntoComa = false;
        this.simbolosPorEnviar = new ArrayList<>();
    }
}
