package modulos.semantico;

import util.GestorErrores;

import java.util.*;

import modulos.tablaSimbolos.*;
import modulos.tablaSimbolos.enums.Modo;
import modulos.tablaSimbolos.enums.Tipo;

/**
 * Clase AnalizadorSemantico que se encarga de procesar las reglas semánticas.
 */
public class AnalizadorSemantico {

    private static AnalizadorSemantico instancia;

    private GestorTablas gestorTablas;
    private GestorSimbolos gestorSimbolos;
    private GestorZonasEspeciales gestorZonas;

    private Stack<Tipo> pilaTipos;
    private GestorParametros gestorParametros;

    private Boolean returnEjecutado;
    private Tipo tipoReturnCondicional;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private AnalizadorSemantico() {
        this.gestorTablas = GestorTablas.getInstance();
        this.gestorSimbolos = GestorSimbolos.getInstance();
        this.gestorZonas = GestorZonasEspeciales.getInstance();
        this.pilaTipos = new Stack<>();
        this.gestorParametros = GestorParametros.getInstance();
        this.returnEjecutado = false;
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     *
     * @return La instancia única de AnalizadorSemantico.
     */
    public static synchronized AnalizadorSemantico getInstance() {
        if (instancia == null) {
            instancia = new AnalizadorSemantico();
        }
        return instancia;
    }

    /**
     * Procesa una regla específica en función de su número.
     *
     * @param numeroRegla El número de la regla a procesar.
     */
    public void procesarRegla(Integer numeroRegla) {
        switch (numeroRegla) {
            case 1: // Aceptar
                aceptar();
                break;
            case 2: // P: B P
            case 3: // P: F P
            case 4: // P: lambda
            case 9: // B: S
            case 16: // H: T
            case 19: // A: VOID
            case 21: // K: lambda
            case 25: // E: U
            case 27: // U: V
            case 29: // V: W
            case 42: // L: lambda
            case 44: // Q: lambda
            case 45: // Z: E
                break;
            case 5: // B: IF ( E ) S
            case 6: // B: WHILE ( E ) { C }
                procesarIfWhile();
                break;
            case 7: // B: LET ID T ;
                procesarDeclaracion();
                break;
            case 8: // B: LET ID T = E ;
                procesarAsignacion();
                break;
            case 10: // T: INT
                pilaTipos.add(Tipo.INT);
                break;
            case 11: // T: BOOLEAN
                pilaTipos.add(Tipo.BOOLEAN);
                break;
            case 12: // T: STRING
                pilaTipos.add(Tipo.STRING);
                break;
            case 13: // F: F1 { C }
                procesarFuncion();
                break;
            case 14: // F1: F2 ( A )
                procesarParametrosFuncion();
                break;
            case 15: // F2: FUNCTION ID H
                procesarFuncionID();
                break;
            case 17: // H: VOID
                pilaTipos.add(Tipo.VOID);
                break;
            case 18: // A: T ID K
            case 20: // K: , T ID K
                procesarParametro();
                break;
            case 22: // C: B C
                procesarSentencia();
                break;
            case 23: // C: lambda
                pilaTipos.add(Tipo.OK);
                break;
            case 24: // E: E1 == U
                procesarComparacion();
                break;
            case 26: // U: U1 + V
                procesarSuma();
                break;
            case 28: // V: !W
                procesarNegacion();
                break;
            case 30: // W: ID
                procesarIdentificador();
                break;
            case 31: // W: ( E )
                procesarParentesis();
                break;
            case 32: // W: ID ( L )
                procesarLlamadaFuncion();
                break;
            case 33: // W: ENTERO
                pilaTipos.add(Tipo.INT);
                break;
            case 34: // W: CADENA
                pilaTipos.add(Tipo.STRING);
                break;
            case 35: // S: ID = E ;
            case 36: // S: ID += E ;
                procesarAsignacionConOperacion();
                break;
            case 37: // S: ID ( L ) ;
                procesarLlamadaFuncionConParametros();
                break;
            case 38: // S: PUT E ;
                procesarPut();
                break;
            case 39: // S: GET ID ;
                procesarGet();
                break;
            case 40: // S: RETURN Z ;
                procesarReturn();
                break;
            case 41: // L: E Q
            case 43: // Q: , E Q
                gestorParametros.addParametro(pilaTipos.pop());
                break;
            case 46: // Z: lambda
                pilaTipos.add(Tipo.VOID);
                break;
            default:
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_REGLA_NO_IMPLEMENTADA);
        }
    }

    /**
     * Procesa la regla de aceptación, destruyendo la tabla de símbolos actual.
     */
    private void aceptar() {
        gestorTablas.destruirTabla();
    }

    /**
     * Procesa las reglas de estructuras de control IF y WHILE.
     * 
     * case 5: // B: IF ( E ) S
     * case 6: // B: WHILE ( E ) { C }
     */
    private void procesarIfWhile() {
        Tipo tipoS = pilaTipos.pop();
        Tipo tipoE = pilaTipos.pop();

        if (!tipoE.equals(Tipo.BOOLEAN)) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_BOOLEAN);
        }

        tipoReturnCondicional = tipoS;
        pilaTipos.add(Tipo.OK);
    }

    /**
     * Procesa la declaración de una variable.
     * 
     * case 7: // B: LET ID T ;
     */
    private void procesarDeclaracion() {
        Tipo tipo = pilaTipos.pop();
        Simbolo simbolo = gestorSimbolos.consumirSimboloSinTipo();
        gestorTablas.asignarTipo(simbolo, tipo);

        pilaTipos.add(Tipo.OK);
    }

    /**
     * Procesa la asignación de una variable.
     * 
     * case 8: // B: LET ID T = E ;
     */
    private void procesarAsignacion() {
        Tipo tipoE = pilaTipos.pop();
        Tipo tipoT = pilaTipos.pop();

        if (tipoE == tipoT) {
            Simbolo simbolo = gestorSimbolos.consumirSimboloSinTipo();
            gestorTablas.asignarTipo(simbolo, tipoT);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }

        pilaTipos.add(Tipo.OK);
    }

    /**
     * Procesa una función, verificando los tipos de retorno y destruyendo la tabla
     * de símbolos.
     * 
     * case 13: // F: F1 { C }
     */
    private void procesarFuncion() {
        Tipo tipoC = pilaTipos.pop();
        Tipo tipoF1 = pilaTipos.pop();

        if (!(tipoF1.equals(tipoC))) {
            if (!(tipoF1.equals(Tipo.VOID) && tipoC == Tipo.OK)) {
                if (returnEjecutado
                        && !(tipoF1.equals(tipoReturnCondicional))) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPO_RETORNO_FUNCION);
                }
            }
        }

        gestorTablas.destruirTabla();
    }

    /**
     * Procesa los parámetros de una función, asignando tipos y modos de paso.
     * 
     * case 14: // F1: F2 ( A )
     */
    private void procesarParametrosFuncion() {

        // Asignar a la funcion el numero de parametros, y su tipo y su modo en la tabla
        // de simbolos
        Simbolo simbolo = gestorSimbolos.getUltimoSimboloFuncion();
        simbolo.setNumeroParametros(gestorParametros.getNumeroParametrosFuncion());
        simbolo.setTipoParametros(new ArrayList<>(gestorParametros.getTipoParametrosFuncion()));
        simbolo.setModoPaso(new ArrayList<>(gestorParametros.getModoPasoParametros()));

        // Se sale de la zona de parametros
        gestorZonas.setZonaParametros(false);
        gestorParametros.reset();
    }

    /**
     * Procesa la declaración de una función, creando una nueva tabla de símbolos.
     * 
     * case 15: // F2: FUNCTION ID H
     */
    private void procesarFuncionID() {

        // Crear una tabla de simbolos nueva
        gestorTablas.nuevaTabla();
        gestorZonas.setZonaParametros(true);
        returnEjecutado = false;

        // Asignar tipo de la funcion a la tabla de sinbolos
        Tipo tipo = pilaTipos.peek();
        Simbolo simbolo = gestorSimbolos.consumirSimboloSinTipo();
        simbolo.setNumeroParametros(0);
        simbolo.setTipoRetorno(tipo);
        gestorTablas.asignarTipo(simbolo, tipo);
    }

    /**
     * Procesa un parámetro de una función.
     * 
     * case 18: // A: T ID K
     * case 20: // K: , T ID K
     */
    private void procesarParametro() {
        Tipo tipo = pilaTipos.pop();
        Simbolo simbolo = gestorSimbolos.consumirSimboloSinTipo();
        gestorTablas.asignarTipo(simbolo, tipo);
        gestorParametros.addParametroFuncion(tipo, Modo.VALOR);
    }

    /**
     * Procesa una secuencia de instrucciones.
     * 
     * case 22: // C: B C
     */
    private void procesarSentencia() {
        Tipo tipoC = pilaTipos.pop();
        Tipo tipoB = pilaTipos.pop();
        if (tipoB != Tipo.OK && tipoC != Tipo.OK) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_RETORNO_FUNCION);
        } else {
            if (tipoB != Tipo.OK) {
                pilaTipos.add(tipoB);
            } else {
                pilaTipos.add(tipoC);
            }
        }
    }

    /**
     * Procesa una comparación de igualdad.
     * 
     * case 24: // E: E1 == U
     */
    private void procesarComparacion() {
        Tipo tipoE1 = pilaTipos.pop();
        Tipo tipoU = pilaTipos.pop();
        if (tipoE1 == tipoU && tipoE1.equals(Tipo.INT)) {
            pilaTipos.add(Tipo.BOOLEAN);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
    }

    /**
     * Procesa una operación de suma.
     * 
     * case 26: // U: U1 + V
     */
    private void procesarSuma() {
        Tipo tipoU1 = pilaTipos.pop();
        Tipo tipoV = pilaTipos.pop();
        if (tipoU1 == tipoV && tipoU1.equals(Tipo.INT)) {
            pilaTipos.add(Tipo.INT);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
    }

    /**
     * Procesa una negación lógica.
     * 
     * case 28: // V: !W
     */
    private void procesarNegacion() {
        Tipo tipo = pilaTipos.pop();
        if (tipo.equals(Tipo.BOOLEAN)) {
            pilaTipos.add(Tipo.BOOLEAN);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    /**
     * Procesa un identificador.
     * 
     * case 30: // W: ID
     */
    private void procesarIdentificador() {
        Simbolo simbolo = gestorSimbolos.getUltimoSimbolo();
        if (simbolo.getTipo() == null) {
            gestorSimbolos.eliminarSimboloSinTipo(simbolo);
            simbolo.setTipo(Tipo.INT);
        }
        pilaTipos.add(simbolo.getTipo());
    }

    /**
     * Procesa una expresión entre paréntesis.
     * 
     * case 31: // W: ( E )
     */
    private void procesarParentesis() {
        Tipo tipo = pilaTipos.peek();
        if (!(tipo.equals(Tipo.BOOLEAN) || tipo.equals(Tipo.INT) || tipo.equals(Tipo.STRING))) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    /**
     * Procesa una llamada a una función dentro de una expresión.
     * 
     * case 32: // W: ID ( L )
     */
    private void procesarLlamadaFuncion() {
        Simbolo simboloFuncion = gestorSimbolos.getUltimoSimboloFuncion();
        verificarLlamadaFuncion(simboloFuncion);

        if (simboloFuncion.getTipo() == null) {
            pilaTipos.add(pilaTipos.peek());
        } else {
            pilaTipos.add(simboloFuncion.getTipo());
        }
    }

    /**
     * Procesa una llamada a una función fuera de una expresión.
     * 
     * case 37: // S: ID ( L ) ;
     */
    private void procesarLlamadaFuncionConParametros() {
        Simbolo simboloFuncion = gestorSimbolos.getUltimoSimboloFuncion();
        verificarLlamadaFuncion(simboloFuncion);
        if (simboloFuncion.getTipo() == null) {
            pilaTipos.add(pilaTipos.peek());
        } else {
            pilaTipos.add(simboloFuncion.getTipo());
        }
        pilaTipos.pop();
        pilaTipos.add(Tipo.OK);
    }

    /**
     * Procesa una asignación con operación.
     * 
     * case 35: // S: ID = E ;
     * case 36: // S: ID += E ;
     */
    private void procesarAsignacionConOperacion() {
        Tipo tipoE = pilaTipos.pop();
        Simbolo simbolo = gestorSimbolos.getUltimoSimbolo();
        if (simbolo.getTipo() == null) {
            gestorSimbolos.eliminarSimboloSinTipo(simbolo);
            gestorTablas.asignarTipo(simbolo, Tipo.INT);
        }
        if (!tipoE.equals(simbolo.getTipo())) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
        pilaTipos.add(Tipo.OK);
    }

    /**
     * Procesa la instrucción PUT.
     * 
     * case 38: // S: PUT E ;
     */
    private void procesarPut() {
        Tipo tipoE = pilaTipos.pop();
        if (tipoE.equals(Tipo.INT) || tipoE.equals(Tipo.STRING)) {
            pilaTipos.add(Tipo.OK);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    /**
     * Procesa la instrucción GET.
     * 
     * case 39: // S: GET ID ;
     */
    private void procesarGet() {
        Simbolo simbolo = gestorSimbolos.getUltimoSimbolo();
        if (simbolo.getTipo() == null) {
            gestorSimbolos.eliminarSimboloSinTipo(simbolo);
            simbolo.setTipo(Tipo.INT);
        }
        if (simbolo.getTipo().equals(Tipo.INT) || simbolo.getTipo().equals(Tipo.STRING)) {
            pilaTipos.add(Tipo.OK);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    /**
     * Procesa la instrucción RETURN.
     * 
     * case 40: // S: RETURN Z ;
     */
    private void procesarReturn() {
        returnEjecutado = true;
    }

    /**
     * Verifica una llamada a una función.
     *
     * @param simboloFuncion El símbolo de la función a verificar.
     */
    private void verificarLlamadaFuncion(Simbolo simboloFuncion) {
        if (simboloFuncion == null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                    GestorErrores.ERROR_LLAMADA_FUNCION_NO_DECLARADA);
        }
        List<Tipo> parametrosFuncion = obtenerParametrosFuncion(simboloFuncion);
        if (parametrosFuncion.size() != gestorParametros.getListaDeParametros().size()) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_NUMERO_PARAMETROS);
        }
        verificarTipoParametros(parametrosFuncion);
        gestorParametros.reset();
    }

    /**
     * Obtiene los parámetros de una función.
     *
     * @param simboloFuncion El símbolo de la función.
     * @return La lista de tipos de parámetros de la función.
     */
    private List<Tipo> obtenerParametrosFuncion(Simbolo simboloFuncion) {
        if (gestorTablas.isTablaGlobal() && simboloFuncion.getNumeroParametros() == null) {
            return new ArrayList<>(gestorParametros.getTipoParametrosFuncion());
        } else {
            return simboloFuncion.getTipoParametros();
        }
    }

    /**
     * Verifica los tipos de los parámetros de una función.
     *
     * @param parametrosFuncion La lista de tipos de parámetros de la función.
     */
    private void verificarTipoParametros(List<Tipo> parametrosFuncion) {
        List<Tipo> listaDeParametros = new ArrayList<>(gestorParametros.getListaDeParametros());
        for (int i = 0; i < listaDeParametros.size(); i++) {
            Tipo tipoParametro = listaDeParametros.get(i);
            if (tipoParametro == null || !tipoParametro.equals(parametrosFuncion.get(i))) {
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_PARAMETROS);
            }
        }
    }

    /**
     * Reinicia el procesador de reglas a su estado inicial.
     */
    public void resetAnalizadorSemantico() {
        gestorParametros.reset();
        gestorSimbolos.resetGestorSimbolos();
        gestorZonas.resetGestorZonas();
        this.gestorTablas = GestorTablas.getInstance();
        this.gestorSimbolos = GestorSimbolos.getInstance();
        this.gestorZonas = GestorZonasEspeciales.getInstance();
        this.pilaTipos = new Stack<>();
        this.gestorParametros = GestorParametros.getInstance();
        this.returnEjecutado = false;
        this.tipoReturnCondicional = null;
    }
}
