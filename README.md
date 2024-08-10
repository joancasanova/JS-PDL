# Procesador de Lenguaje JS-PdL

Este proyecto ha sido desarrollado para la asignatura **Procesadores de Lenguajes** en la **Universidad Politécnica de Madrid** durante el curso 2023-24.

- **Autor**: Juan Francisco Casanova Ferrer
- **Email**: juancasanovaferrer@gmail.com

Para cualquier duda, no dudes en contactar conmigo, estaré encantado de ayudarte.

**Son bienvenidas las sugerencias de mejora del proyecto y su ampliación.**

En el desarrollo de este proyecto también ha colaborado **Manuel Pérez Redondo**:
- **Email**: redondoperezmanuel@gmail.com

## Contenidos

0. [Descripción del Proyecto](#descripcion-del-proyecto)
1. [Funcionalidades Implementadas](#funcionalidades-implementadas)
2. [Guía Completa para el Desarrollo de la Práctica - JS-PdL](#guia-completa-para-el-desarrollo-de-la-practica---js-pdl)
    - [0. Definir Tokens](#0-definir-tokens)
    - [1. Tabla de Símbolos](#1-tabla-de-simbolos)
    - [2. Analizador Léxico](#2-analizador-lexico)
    - [3. Analizador Sintáctico](#3-analizador-sintactico)
    - [4. Analizador Semántico](#4-analizador-semantico)
    - [5. Pruebas](#5-pruebas)
    - [6. Memoria](#6-memoria)
    - [7. Presentación](#7-presentacion)
    - [Visualización de Árboles Sintácticos con VASt](#visualizacion-de-arboles-sintacticos-con-vast)
3. [Cómo Ejecutar el Proyecto](#como-ejecutar-el-proyecto)
4. [Motivación para el Desarrollo de esta Guía](#motivacion-para-el-desarrollo-de-esta-guia)
    - [Problemática de la Asignatura](#problematica-de-la-asignatura)
    - [Solución Propuesta](#solucion-propuesta)

## Descripcion del Proyecto

Este programa implementa un procesador de lenguaje para el lenguaje **JS-PdL**, una variante de JavaScript diseñada específicamente para la práctica de esta asignatura. Cabe destacar que las características de JS-PdL no coinciden al 100% con el estándar de JavaScript, ya que es una versión simplificada de éste.

## Funcionalidades Implementadas

Para esta práctica, se requiere desarrollar una serie de funcionalidades obligatorias y opcionales. Las funcionalidades completas pueden consultarse en la siguiente página: [Funciones JS-PdL](https://dlsiis.fi.upm.es/procesadores/IntroJavaScript.html).

Además de las funcionalidades comunes para todos los grupos, las funcionalidades opcionales implementadas en este proyecto son:

- **Sentencias**: Sentencia repetitiva (`while`)
- **Operadores especiales**: Asignación con suma (`+=`)
- **Técnicas de Análisis Sintáctico**: Ascendente LR
- **Comentarios**: Comentario de línea (`//`)
- **Cadenas**: Con comillas dobles (`" "`)

## Guia Completa para el Desarrollo de la Practica - JS-PdL

A continuación se presenta una guía detallada basada en el código de este repositorio para el desarrollo de la práctica.

**AVISO 1: Esta es una guía de elaboración propia e independiente del equipo docente. Debe tomarse como una referencia imperfecta. Se aconseja al alumno que dedique tiempo a comprender todos los conceptos que se deben aplicar para el desarrollo de su práctica. En el caso de que no se comprenda bien las bases teóricas, no será capaz de desarrollar la práctica ni siquiera siguiendo esta guía.**

**AVISO 2: Se aconseja que en caso de dudas sobre los contenidos teóricos se concerten tutorías con el equipo docente, además de acudir a clase siempre que sea posible.**

**AVISO 3: Se han detectado errores en el código, especialmente en el módulo de análisis semántico, que aún no han sido corregidos. Se valoran las sugerencias y pull requests para solucionar estos problemas. Algunos de los errores detectados están en la sección de [issues](https://github.com/joancasanova/PDL/issues), aunque es posible que existan más.**

**AVISO 4: El código publicado en este repositorio está destinado únicamente como referencia para resolver dudas. Cualquier acto de copia constituye una violación de los derechos de propiedad intelectual y una infracción de las normas éticas de la UPM, lo que podría acarrear sanciones tanto por mi parte como por parte de la UPM.**

### Fases del Desarrollo

El desarrollo de la práctica se divide en las siguientes fases:

0. **Definir Tokens**
1. **Tabla de Símbolos**
2. **Analizador Léxico**
3. **Analizador Sintáctico**
4. **Analizador Semántico**
5. **Pruebas**
6. **Memoria**
7. **Presentación**

### 0. Definir Tokens

Un token es la unidad léxica más pequeña e indivisible con significado propio. Cada token pertenece a una categoría léxica diferente. Por ejemplo: PALABRARESERVADA, SUMA, o COMPARADOR.

- Implementación completa: `src/main/java/modulos/token`

Pasos a seguir:

0. **Definir los Tokens:** Se deben definir los tokens de la práctica, que dependen de las opciones de que se deben implementar.

1. **Implementar: Ejemplo en `TipoToken.java`:**
    - Se especifican todos los tokens, incluyendo `FINDEFICHERO`.
    - Se indica, entre paréntesis, al lado del token, el carácter que representa cada token:
        ```java
        SUMA("'+'"),
        NEGACION("'!'")
        ```
    - Si el token está representado por varios caracteres, se escribe una palabra que lo represente:
        ```java
        ENTERO("ENTERO"),
        CADENA("CADENA")
        ```
    - Para FINDEFICHERO y PALABRARESERVADA se especifica así:
        ```java
        FINDEFICHERO("$end"),
        PALABRARESERVADA("")
        ```
    - **NOTA**: Los nombres especificados entre paréntesis se utilizarán posteriormente para generar el archivo `gramatica.y` empleado en el analizador sintáctico y deben coincidir con éste.

### 1. Tabla de Simbolos

La tabla de símbolos es una estructura esencial que guarda los identificadores (variables y funciones) presentes en el código fuente de JS-PdL.

- Implementación completa: `src/main/java/modulos/tablaSimbolos`

A tener en cuenta:

0. **En fichero `GestorTablas.java`:** Las constantes `BYTES_STRING`, `BYTES_BOOLEAN`, `BYTES_INT`, y `BYTES_VOID` deben ser adaptadas a las especificaciones del enunciado.

### 2. Analizador Lexico

El analizador léxico recibe caracter a caracter hasta generar un token.

- Implementación completa: `src/main/java/modulos/lexico`

Pasos a seguir y consideraciones a tener en cuenta:

0. **Crear el autómata finito determinista, las acciones semánticas y errores:** Puedes ver un ejemplo en la memoria adjunta en `docs/Memoria.pdf`. Las bases teóricas se enseñan en clase y existe documentación en Moodle, además de contar con el equipo docente para consultar dudas en tutorías.

1. **Creación de estructuras: Enums** (`src/main/java/modulos/lexico/enums`):

    - Fichero de ejemplo `PalabraReservada.java`: Aquí se recogen todas las palabras reservadas del lenguaje.
    - Fichero de ejemplo `EstadoTransito.java`: Aquí se definen los estados de tránsito del autómata. Ejemplos:
        - `SIMBOLOIGUAL`: Se ha detectado un caracter igual. Estado intermedio entre los tokens `=` ASIGNACION y `==` COMPARADOR.
        - `LEXEMA`: Estado intermedio entre los tokens IDENTIFICADOR y PALABRARESERVADA.
        - **Atención**: En esta implementación es necesario el estado `INICIO`.

    - Fichero de ejemplo `EstadoFinal.java`: Aquí se definen los estados finales del autómata. Ejemplo:
        - `ABREPARENTESIS`: Corresponde al token `ABREPARENTESIS`.
        - **Atención**: En esta implementación son necesarios los estados `PENDIENTE` y `FINDEFICHERO`.

2. **En fichero `AnalizadorLexico.java`:**

    - Función `debeConsumirCaracter(EstadoFinal estado)`:
        - Se incluyen todos los estados finales que implican haber leído un carácter extra que al final no se ha utilizado o consumido: 
        - Por ejemplo `SUMA (+)`, `NEGACION (!)`, o `ASIGNACION (=)` si existen `+=`, `!=`, y `==`.

3. **En fichero `GestorEstados.java`:**

    - Función `procesarEstadoInicial(Character charActual)`:
        - Dependiendo del carácter recibido, se pasa a un estado final o intermedio.
        - **Atención**: Aquí se gestiona si un identificador puede empezar por barra baja `_`.

    - Función `actualizarEstado(Character charActual, String lexema)`:
        - Adaptar el switch para manejar cada estado de tránsito en el autómata.

4. **En fichero `GeneradorToken.java`:**

    - Se especifica el valor adecuado para los atributos `MAX_CARACTERES_CADENA` y `MAX_VALOR_ENTERO`, que dependen del enunciado.
    - Función `generarToken(EstadoFinal estadoFinal, Character charActual, String lexema)`:
        - Se incluyen los casos para cada estado final. Son esenciales los casos `PENDIENTE` y `FINDEFICHERO`.

5. **Probar el Analizador Léxico:**

    - En `Analizador.java` (`src/main/java/main/Analizador.java`), se comenta todo lo relativo a `analizadorSintactico` y `analizadorSemantico`.
    - Ejecutar el programa y comprobar que se generan los tokens adecuadamente:
        1. Crear un archivo `input.txt` y guardarlo en el directorio `input`.
        2. Ejecutar el programa. (["Cómo ejecutar el proyecto"](#como-ejecutar-el-proyecto)).
        3. Comprobar que los tokens en `output/archivoTokens.txt` son correctos.

### 3. Analizador Sintactico

El analizador sintáctico recibe token a token y genera como output las reglas una a una.

Se ha implementado un analizador sintáctico **ASCENDENTE LR**. Es decir, se aconseja elegir esta opción en la práctica si se desea seguir esta guía.

- Implementación completa: `src/main/java/modulos/sintactico`

La tarea principal es crear la gramática sintáctica correspondiente a la práctica y procesarla con bison (se explica a continuación cómo).

0. **Crear la gramática del analizador sintáctico:** Como con el analizador léxico, las bases teóricas se enseñan en clase y existe documentación en Moodle, además de contar con el equipo docente para consultar dudas en tutorías.

1. **Instalar Bison**:

    - En Linux:
        ```bash
        sudo apt update
        sudo apt install bison
        ```
    - En Windows: descarga WSL y ejecuta los comandos desde ahí, o utiliza una máquina virtual de Linux.

2. **Crear el archivo `gramatica.y`**:

    - Este archivo sirve como input para Bison.
    - En este archivo se especifica la gramática del analizador sintáctico.
    - **IMPORTANTE**: Los nombres que se han puesto entre paréntesis en el fichero `TipoToken.java` se deben utilizar aquí en el apartado `%token`. Pero solo los nombres, no aquellos que sean caracteres solo como '+', '-', '(', etc... Incluir también las palabras reservadas. No incluir `$end`.
    - Ejemplo de archivo `.y`:

        ```yacc
        %{
        /* Código C necesario para la integración con el analizador léxico (lexer) */
        #include <stdio.h>
        #include <stdlib.h>

        extern int yylex(void);
        void yyerror(const char *s) {
            fprintf(stderr, "Error: %s\n", s);
        }
        %}

        %token ID ENTERO CADENA PUT GET RETURN IF WHILE LET FUNCTION INT BOOLEAN STRING VOID EQ_OP ADD_OP
        %left '+' EQ_OP ADD_OP
        %right '!'

        %start P

        %%

        P: B P
         | F P
         | /* empty */
         ;

        B: IF '(' E ')' S
         | WHILE '(' E ')' '{' C '}'
         | LET ID T ';'
         | LET ID T '=' E ';'
         | S
         ;

        T: INT
         | BOOLEAN
         | STRING
         ;

        F: F1 '{' C '}'
         ;

        F1: F2 '(' A ')'
         ;

        F2: FUNCTION ID H
         ;

        H: T
         | VOID
         ;

        A: T ID K
         | VOID
         ;

        K: ',' T ID K
         | /* empty */
         ;

        C: B C
         | /* empty */
         ;

        E: E EQ_OP U
         | U
         ;

        U: U '+' V
         | V
         ;

        V: '!' W
         | W
         ;

        W: ID
         | '(' E ')'
         | ID '(' L ')'
         | ENTERO
         | CADENA
         ;

        S: ID '=' E ';'
         | ID ADD_OP E ';'
         | ID '(' L ')' ';'
         | PUT E ';'
         | GET ID ';'
         | RETURN Z ';'
         ;

        L: E Q
         | /* empty */
         ;

        Q: ',' E Q
         | /* empty */
         ;

        Z: E
         | /* empty */
         ;

        %%

        int main(void) {
            return yyparse();
        }
        ```

3. **Generar el archivo `gramatica.output`**:

    - Ejecuta en línea de comandos en el mismo directorio que `gramatica.y`:
        ```bash
        bison -d --verbose gramatica.y -Wcounterexamples
        ```
    - Si se ha generado correctamente, no aparecerán mensajes de error y se creará `gramatica.output`.

4. **`gramatica.output` se ubica en el directorio `src/main/resources`**.

5. **Implementar el Analizador Sintáctico**: Implementación completa en el directorio `src/main/java/modulos/sintactico`.

    - En esta implementación se han creado las clases:
        - `AnalizadorSintactico.java`: Procesa los tokens recibidos del analizador léxico con la función `procesarToken(Token token)`, que ejecuta la acción correspondiente y devuelve el número de la regla aplicada de la gramática del analizador sintáctico.
        - `GestorPilas.java`: Maneja las pilas de estados (`pilaEstados`) y símbolos (`pilaSimbolos`) para el análisis sintáctico.
        - `ParserGramatica.java`: Se encarga de parsear el archivo de texto generado por Bison `gramatica.output` para crear las estructuras de datos para ser utilizadas por un analizador sintáctico LR(1): la tabla de acción `tablaAccion` y la tabla de GO TO `tablaGoTo`.
        - `sintactico/accion`: Directorio que alberga las diferentes posibles acciones sintácticas (desplazar, reducir, aceptar) con su correspondiente lógica.

5. **Probar el Analizador Sintáctico**:

    - En `Analizador.java` (`src/main/java/main/Analizador.java`), comentar todo lo relativo a `analizadorSemantico`.
    - Comprobar que se generan correctamente las reglas:
        1. Crear un archivo `input.txt` y guardarlo en el directorio `input`.
        2. Ejecutar el programa. (["Cómo ejecutar el proyecto"](#como-ejecutar-el-proyecto)).
        3. Comprobar que las reglas en `output/reglasAplicadas.txt` son correctas.
        4. Usar el programa **VASt** para representar el árbol sintáctico [(se explica más adelante cómo usar VASt.)](#visualizacion-de-arboles-sintacticos-con-vast)

### 4. Analizador Semantico

El analizador semántico procesa las reglas una a una generadas por el analizador sintáctico y comprueba que son correctas.

- Implementación completa `src/main/java/modulos/semantico`.

La lógica del analizador semántico se encuentra encapsulada principalmente en la clase `AnalizadorSemantico.java`.

A tener en cuenta:

0. **En fichero `AnalizadorSemantico.java`:**

    - En la función `procesarRegla(Integer numeroRegla)` se debe tener un caso por cada regla correspondiente a la gramática del analizador sintáctico hecha en el paso anterior.

1. **Se debe adaptar la funcionalidad de cada función** a las reglas correspondientes a las opciones de vuestra práctica.

2. **Probar el Analizador Semántico**:

    - En `Analizador.java` (`src/main/java/main/Analizador.java`), descomentar todo.
    - Comprobar que se genera correctamente la tabla de símbolos:
        1. Crear un archivo `input.txt` y guardarlo en el directorio `input`.
        2. Ejecutar el programa. (["Cómo ejecutar el proyecto"](#como-ejecutar-el-proyecto)).
        3. Comprobar que las reglas en `output/archivoTablaSimbolos.txt` son correctas.

### 5. Pruebas

En el directorio `src/test/archivosTest` se han creado una serie de pruebas. Se aconseja adaptar estas pruebas a las opciones correspondientes a cada práctica. Al ejecutar las pruebas de `AnalizadorTest.java`, se deberán superar todas.

### 6. Memoria
La [página web de la asignatura](https://dlsiis.fi.upm.es/procesadores/Practica.html) especifica las siguientes condiciones para la memoria en la convocatoria 2023 - 24:
 
- Extensión máxima de 30 páginas sin contar el anexo
- Una descripción del diseño final del Procesador, según las opciones correspondientes al grupo de prácticas, así como cualquier otro aspecto o característica que se desee hacer notar por su interés, sin incluir listados fuente del procesador ni detalles de la implementación. Esta memoria deberá incluir al menos:
    - Diseño del Analizador Léxico actualizado: tokens, gramática, autómata, acciones semánticas y errores.
    - Diseño del Analizador Sintáctico actualizado: gramática, demostración de que la gramática es adecuada para el método de Análisis Sintáctico asignado, y las tablas, autómata o procedimientos de dicho Analizador.
    - Diseño del Analizador Semántico: Traducción Dirigida por la Sintaxis con las acciones semánticas.
    - Diseño de la Tabla de Símbolos completa: descripción de su estructura final y organización.
    - Diseño del Gestor de Errores: manejo de los mensajes de error, gestión del número de línea...
- Anexo con 10 casos de prueba. Deberá incluirse en la memoria un anexo con los 10 casos listados. La mitad de ellos serán correctos y la otra mitad erróneos, de tal manera que permitan observar el comportamiento del Procesador. Para los ejemplos correctos se incluirá el listado de tokens, el árbol de análisis sintáctico (que se generará obligatoriamente utilizando la herramienta VASt) y el volcado de la Tabla de Símbolos. Para los 5 ejemplos erróneos se incluirá el mensaje o mensajes de error obtenidos. Los resultados de los casos de prueba deben ser generados por el procesador implementado, sin ningún tipo de edición manual.

En el directorio `docs` se puede encontrar la memoria desarrollada para este proyecto y tenerla como referencia.

### 7. Presentacion

La presentación consiste en procesar un fichero de input. Durante la presentación no se realizan preguntas acerca del proceso de desarrollo, ni se pide que se explique el diseño del procesador, ni se pregunta sobre conceptos relativos a la asignatura. Esta presentación se reduce exclusivamente a comprobar que los archivos de output son correctos, se genera el árbol sintáctico con VASt, y se han identificado los errores en el input.

En otras palabras, se trata de superar una prueba, el método para superarla es indiferente. A este tipo de testing se le llama pruebas de caja negra, sirve para evaluar que se cumplen los requisitos funcionales de un producto sin tener en cuenta su diseño interno.

Una consecuencia de este tipo de evaluación es que el código que se ejecute durante esta presentación puede ser el desarrollado por el alumno, o no, ya que no se realiza ningún tipo de comprobación. Y, aunque el código sea realmente desarrollado por el alumno, este puede no seguir ninguno de los principios impartidos en la asignatura y tener un diseño completamente alternativo o deficiente. Esta problemática se comenta más adelante en la sección ["Motivación para el Desarrollo de esta Guía"](#motivacion-para-el-desarrollo-de-esta-guia).

### Visualizacion de Arboles Sintacticos con VASt

0. **Descargar VASt** de la sección de herramientas de la página web de la asignatura: [Herramientas Procesadores](https://dlsiis.fi.upm.es/procesadores/Herramientas.html)

1. **Crear el archivo `gramatica.txt`** que sirve de input para el programa. 

    - Este archivo representa la gramática utilizada para el analizador sintáctico. A continuación se muestra el utilizado para esta práctica:
        
        ```
        NoTerminales = { P1 P E U V W X S L Q Z B T F F1 F2 H A K C }

        Terminales = { == + ! , id ( ) ent cad = ; put get return += if while let int boolean string { } function void $ }

        Axioma = P1

        Producciones = {
            P1 -> P $
            P -> B P
            P -> F P
            P -> lambda
            B -> if ( E ) S
            B -> while ( E ) { C }
            B -> let id T ;
            B -> let id T = E ;
            B -> S
            T -> int
            T -> boolean
            T -> string
            F -> F1 { C }
            F1 -> F2 ( A )
            F2 -> function id H
            H -> T
            H -> void
            A -> T id K
            A -> void
            K -> , T id K
            K -> lambda
            C -> B C
            C -> lambda
            E -> E == U
            E -> U
            U -> U + V
            U -> V
            V -> ! W
            V -> W
            W -> id
            W -> ( E )
            W -> id ( L )
            W -> ent
            W -> cad
            S -> id = E ;	
            S -> id += E ;
            S -> id ( L ) ;
            S -> put E ;
            S -> get id ;
            S -> return Z ;
            L -> E Q
            L -> lambda
            Q -> , E Q
            Q -> lambda
            Z -> E
            Z -> lambda
        }
        ```

2. **Ejecutar el programa (visualizador)**.

3. **Archivo > Abrir archivo Gramática...** > seleccionar el archivo de gramática creado.

4. En la pestaña de **Parse**, poner las reglas generadas por el analizador sintáctico.

5. **Archivo > Generar Árbol**.

## Como Ejecutar el Proyecto

Para ejecutar el proyecto usando Gradle, sigue estos pasos:

0. Navega al directorio del proyecto:
    ```sh
    cd /ruta/al/directorio/del/proyecto
    ```

1. Asegúrate de que tienes Gradle instalado. Puedes verificarlo con:
    ```sh
    gradle -v
    ```

2. Compila el proyecto:
    ```sh
    gradle build
    ```

3. Ejecuta el proyecto:
    ```sh
    gradle run
    ```

Asegúrate de que el archivo `input.txt` está en el directorio `input` antes de ejecutar el programa.

Si necesitas más detalles, no dudes en preguntar.

## Motivacion para el Desarrollo de esta Guia

### Problematica de la Asignatura

**Ratio elevado de suspensos:**
La asignatura de Procesadores de Lenguajes tiene un alto índice de suspensos, convirtiéndose en un cuello de botella para los estudiantes de Ingeniería Informática. Este elevado ratio de suspensos tiene como consecuencia la desmotivación de los alumnos, el retrasar su progreso académico, e incluso alentar al abandono del grado. En la [página web de la asignatura](https://dlsiis.fi.upm.es/procesadores/FAQ.html) se revela que tan solo el 67% de los estudiantes que "se han esforzado" aprueban la práctica. Esto es una estadística reveladora, ya que implica que un 33% de los alumnos que se esfuerzan, suspenden. Es decir, un tercio de los alumnos que se mantienen al día no son capaces de superar la práctica. Esta situación se lleva arrastrando desde hace años y refleja una problemática estructural.

**Cantidad de trabajo desproporcionada para 3 ECTS:**
La carga de trabajo que se exige a los estudiantes para completar esta asignatura es considerablemente elevada en comparación con los 3 ECTS asignados. Esto genera una desproporción entre el esfuerzo requerido y los créditos obtenidos, lo que crea unas falsas expecativas respecto al tiempo que se debe invertir en esta asignatura, y que puede acabar derivando en sensaciones de desmotivación y agotamiento. La percepción general, y manifestada en las encuestas, es que el esfuerzo necesario para aprobar esta asignatura no se ve reflejado en los créditos otorgados.

**Falta de orientación en el desarrollo del software:**
Aunque el equipo docente ofrece una amplia gama de recursos, como clases presenciales, sesiones prácticas y la página web de Draco, estos no abordan completamente el verdadero desafío que enfrentan los alumnos con esta práctica. El problema no radica solo en comprender los conceptos teóricos, sino en la complejidad de organizar y desarrollar un software no trivial. En este aspecto, muchos alumnos se sienten solos y desorientados, ya que aún no han consolidado las competencias necesarias para desarrollar software de calidad. En otras palabras, necesitan más experiencia para poder programar un proyecto de esta magnitud y tienen dificultades para trasladar la teoría al código. Además, la falta de feedback adecuado dificulta aún más el progreso en la implementación del proyecto, lo que aumenta la frustración y la dificultad para superar la asignatura.

**Reparto desequilibrado de trabajo entre compañeros:**
A menudo, los trabajos en grupo presentan un reparto desequilibrado de tareas, donde no todos los miembros del grupo contribuyen equitativamente. Esto puede derivar en situaciones no deseadas donde el esfuerzo de unos pocos se diluye en el grupo, afectando negativamente a la evaluación individual. Este problema es agravado por la falta de mecanismos efectivos para evaluar la contribución individual dentro de los grupos en esta asignatura. En el caso concreto de esta práctica, este problema se ve acentuado ya que, debido a las dimensiones del proyecto, si no se consigue un reparto equitativo de las tareas, la carga de trabajo pasa a ser desmesurada para el alumno. Este problema es muy común, ya que no es sencillo encontrar compañeros que tengan las habilidades y la predisposición necesarias para abordar la práctica, y es mera cuestión de suerte encontrarlos.

**Corrección que no tiene en cuenta el trabajo realizado:**
La corrección de los trabajos se lleva a cabo bajo un enfoque de caja negra, donde solo se evalúa el resultado de la ejecución de una serie de pruebas, sin considerar el diseño, los conocimientos realmente adquiridos por los estudiantes ni el esfuerzo invertido en el desarrollo del proyecto. Este tipo de evaluación no refleja adecuadamente las competencias adquiridas en la asignatura y puede ser negligente para los alumnos que han trabajado concienzudamente en su proyecto, pero que, debido a un error menor, presentan fallos en el resultado final.

**Concepción errónea de que el examen demuestra conocimientos sobre la asignatura:**
Existe una percepción errónea de que el examen final demuestra el conocimiento y las habilidades adquiridas en la asignatura. Es cierto que muchos de los alumnos que aprueban el examen sí han adquirido las competencias, pero la forma más eficiente de superar el examen y maximizar la nota es aprendiendo los ejercicios tipo y repitiendo las respuestas de memoria, sin necesariamente tener una comprensión profunda de los conceptos. Esto no fomenta un aprendizaje significativo ni el desarrollo de competencias reales.

**Compra de prácticas:**
Debido a la elevada complejidad y las exigencias de la práctica, algunos estudiantes recurren a la compra de prácticas para poder superar la asignatura. Este fenómeno, aunque difícil de cuantificar, es una consecuencia directa de la falta de orientación en el desarrollo, el elevado ratio de suspensos y la percepción de una carga de trabajo desproporcionada. La compra de prácticas no solo mina la integridad académica, sino que también distorsiona la evaluación del verdadero desempeño de los estudiantes, generando una situación injusta tanto para quienes siguen el proceso de manera ética como para el conjunto de la comunidad académica.

**Problemática prolongada en el tiempo:**
Esta es una situación que se dilata en el tiempo y que no muestra signos de cambio. La persistencia de estos problemas a lo largo de años sin la intervención adecuada está originando un entorno académico hostil y a menudo desalentador para los estudiantes.

### Solucion Propuesta
Con esta guía se pretende abordar y paliar los problemas mencionados. De este modo, los alumnos que acudan a esta guía podrán tener una asistencia extra en el desarrollo de su práctica. Se espera una reacción constructiva por parte del equipo docente de la asignatura, y se proponen las siguientes medidas:

- **Facilitar un esqueleto de la práctica:**
Se propone que el equipo docente facilite un esqueleto o estructura básica de la práctica, similar a lo que se hace en otras asignaturas. Esto proporcionaría a los estudiantes una base sobre la cual construir su proyecto, reduciendo la desorientación inicial y ayudando a enfocar sus esfuerzos en la implementación de los aspectos más complejos y relevantes del desarrollo de software. Facilitar un esqueleto podría mejorar la organización del trabajo, permitir un uso más eficiente del tiempo y reducir la carga cognitiva en los estudiantes, ayudando a cerrar la brecha entre teoría y práctica.

- **Evaluación del diseño e implementación:**

    1. **Realizar preguntas en la sesión de presentación a todos los miembros del grupo:** Durante la presentación del proyecto, se propone formular preguntas relativas al desarrollo de la práctica a todos los miembros del grupo para asegurar que todos han participado activamente y poseen un entendimiento del trabajo realizado.

    2. **Exposición de los módulos implementados:** Los estudiantes deben ser capaces de presentar y explicar los módulos que han implementado, detallando su funcionalidad y su integración en el proyecto global.

    3. **Inspección del código desarrollado:** Se propone inspeccionar directamente el código y evaluar en función de si aplica correctamente los conceptos impartidos. Esto incluye la implementación de diferentes módulos utilizando la lógica correspondiente y siguiendo los algoritmos que se enseñan en la asignatura.

    4. **Comprobación de que el código utilizado en la exposición está realmente desarrollado por los alumnos:** Es fundamental verificar que el código presentado ha sido desarrollado por los alumnos y no ha sido plagiado, garantizando la integridad académica.

- **Valoración del trabajo acorde a los créditos de la asignatura:**
La evaluación del trabajo debe ser proporcional a los créditos asignados a la asignatura (3 ECTS). Esto implica ajustar la carga de trabajo y la complejidad de los proyectos a un nivel razonable, que permita a los estudiantes alcanzar los objetivos de aprendizaje sin una carga excesiva de trabajo. Para valorar el trabajo de los alumnos se puede recurrir a las siguientes herramientas:

    1. **Exposición de retos encontrados en el desarrollo y soluciones aplicadas:** Los alumnos deben ser capaces de describir los retos que enfrentaron durante el desarrollo del proyecto y las soluciones que aplicaron, demostrando la dedicación, persistencia, la capacidad para resolver problemas y aplicar los conocimientos teóricos en la práctica.

    2. **Valoración completa y en su conjunto del trabajo realizado:** La evaluación debería considerar el trabajo en su totalidad, independientemente de la superación o no de casos de prueba concretos, reconociendo el esfuerzo y el conocimiento aplicados en el desarrollo del proyecto.

- **Realizar una práctica de forma individual y más corta:**
Se propone la creación de una práctica individual, más corta y factible de realizar por uno mismo. Esta práctica tendría un nivel de dificultad ajustado a las competencias que los alumnos ya han desarrollado, permitiéndoles aplicar los conocimientos teóricos en un entorno más manejable y obtener una experiencia práctica más alineada con su nivel de aprendizaje. Esta medida solucionaría varios problemas:

    1. Moticación extra para abordar la práctica por uno mismo: Al ser más corta y menos compleja, la práctica individual permitiría a los estudiantes organizar mejor su tiempo, reduciendo el estrés asociado con la entrega de un proyecto extenso, especialmente para aquellos alumnos que deben que acabar haciendo la práctica sin ayuda de compañeros. Así, los estudiantes tendrán la percepción de que se trata de una tarea abordable, por lo que es posible que aumente la motivación para realizarla por uno mismo.

    2. Prevención del reparto desequilibrado de trabajo: Al no requerir trabajo en grupo, se eliminarían los problemas relacionados con el desequilibrio en la distribución de tareas, garantizando que cada estudiante es responsable de todo el proceso de desarrollo.

---

Esta guía busca no solo facilitar la superación de la asignatura, sino también promover una evaluación más justa y representativa de los conocimientos y habilidades adquiridos por los estudiantes.

---

### Datos sobre el proyecto: líneas de código, lenguajes, y archivos

| Language   | files | blank | comment | code |
|------------|-------|-------|---------|------|
| Java       | 28    | 406   | 1008    | 1832 |
| Markdown   | 2     | 337   | 0       | 1278 |
| HTML       | 5     | 5     | 0       | 1242 |
| yacc       | 1     | 24    | 1       | 80   |
| Gradle     | 2     | 13    | 7       | 60   |
| YAML       | 1     | 12    | 0       | 49   |
| **SUM**    | **39**| **797**| **1016** | **4541** |

---
¡Gracias por tu atención y buena suerte con el desarrollo de tu práctica!

Si necesitas más detalles o quieres sugerir alguna modificación o mejora, no dudes en enviar un correo electrónico a juancasanovaferrer@gmail.com
