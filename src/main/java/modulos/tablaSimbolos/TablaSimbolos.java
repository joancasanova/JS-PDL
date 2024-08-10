package modulos.tablaSimbolos;

import java.util.List;
import java.util.Map;

import modulos.tablaSimbolos.enums.Modo;
import modulos.tablaSimbolos.enums.Tipo;

import java.util.HashMap;

/**
 * Clase TablaSimbolos que representa una tabla de símbolos utilizada en un
 * compilador.
 * La tabla de símbolos almacena información sobre identificadores como
 * variables y funciones.
 */
public class TablaSimbolos {

    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    // Número identificador para la tabla de símbolos
    private Integer numeroTabla;
    private Integer desplazamiento;

    /**
     * Constructor que inicializa la tabla de símbolos con un número de tabla dado.
     * 
     * @param numeroTabla El número identificador de la tabla de símbolos.
     */
    public TablaSimbolos(Integer numeroTabla) {
        this.tabla = new HashMap<>();
        this.numeroTabla = numeroTabla;
        this.desplazamiento = 0;
    }

    /**
     * Agrega un símbolo a la tabla.
     * 
     * @param simbolo El símbolo a agregar.
     */
    public void agregarSimbolo(Simbolo simbolo) {
        tabla.put(tabla.size(), simbolo);
    }

    /**
     * Establece el desplazamiento actual en la tabla de símbolos.
     * 
     * @param desplazamiento El desplazamiento a establecer.
     */
    public void setDesplazamiento(int desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

    /**
     * Obtiene el desplazamiento actual en la tabla de símbolos.
     * 
     * @return El desplazamiento actual.
     */
    public Integer getDesplazamiento() {
        return this.desplazamiento;
    }

    /**
     * Obtiene la posición de un símbolo existente en la tabla.
     * 
     * @param simbolo El símbolo cuya posición se busca.
     * @return La posición del símbolo en la tabla, o null si no se encuentra.
     */
    public Integer obtenerPosicionSimbolo(Simbolo simbolo) {
        for (Map.Entry<Integer, Simbolo> entry : tabla.entrySet()) {
            if (entry.getValue().equals(simbolo)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Verifica si un símbolo ya existe en la tabla.
     * 
     * @param nombre El nombre del símbolo a verificar.
     * @return true si el símbolo existe, false en caso contrario.
     */
    public boolean simboloExiste(String nombre) {
        for (Simbolo simbolo : tabla.values()) {
            if (simbolo.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene un símbolo de la tabla por su nombre.
     * 
     * @param nombre El nombre del símbolo a buscar.
     * @return El símbolo correspondiente, o null si no se encuentra.
     */
    public Simbolo obtenerSimboloPorNombre(String nombre) {
        for (Simbolo simbolo : tabla.values()) {
            if (simbolo.getNombre().equals(nombre)) {
                return simbolo;
            }
        }
        return null;
    }

    /**
     * Imprime el contenido de la tabla de símbolos.
     * 
     * @return Una cadena que representa el contenido de la tabla de símbolos.
     */
    public String imprimirTabla() {

        StringBuilder sb = new StringBuilder();

        sb.append("CONTENIDOS DE LA TABLA #" + numeroTabla + ":\n");

        for (Map.Entry<Integer, Simbolo> entrada : tabla.entrySet()) {

            // Obtenemos parametros de la entrada
            Simbolo simbolo = entrada.getValue();
            String nombre = simbolo.getNombre().toString();
            Tipo tipo = simbolo.getTipo();
            Integer desplazamientoSimbolo = simbolo.getDesplazamiento();
            Tipo tipoRetorno = simbolo.getTipoRetorno();

            // Imprimimos la entrada

            if (tipoRetorno == null) {
                sb.append("\n*\t");
                sb.append("LEXEMA\t:\t");
                sb.append("'" + nombre + "'\n");

                sb.append("Atributos: ");
                sb.append("\n+ tipo: ");
                sb.append("'" + tipo + "'\t");

                sb.append("\n+ despl: ");
                sb.append(desplazamientoSimbolo);
            } else {
                Integer numParam = simbolo.getNumeroParametros();
                List<Tipo> parametros = simbolo.getTipoParametros();
                List<Modo> modoParametros = simbolo.getModoPaso();

                sb.append("\n*\t");
                sb.append("LEXEMA\t:\t");
                sb.append("'" + nombre + "'\n");

                sb.append("Atributos: ");
                sb.append("\n+ tipo: ");
                sb.append("'" + tipo + "'\t");

                sb.append("\n+ numParam: ");
                sb.append("'" + numParam + "'\t");

                int i = 0;
                for (Tipo tipoParametro : parametros) {
                    sb.append("\n+ TipoParam" + i + ": ");
                    sb.append("'" + tipoParametro + "'\t");
                    sb.append("\n+ ModoParam" + i + ": ");
                    sb.append("'" + modoParametros.get(i) + "'\t");
                    i++;
                }

                sb.append("\n+ TipoRetorno: ");
                sb.append("'" + tipoRetorno + "'\t");

                sb.append("\n+ EtiqFuncion: ");
                sb.append("'" + nombre + "'\t");
            }
        }

        sb.append("\n");
        sb.append("\n");

        return sb.toString();
    }

}
