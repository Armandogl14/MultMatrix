# Requerimientos para ejecutar

## Para ejecutar el codigo es necesario:

1. Descargar la libreria MPI, puede descargarla [aquí](http://mpjexpress.org).
2. Luego de descargar el zip y extraer sus contenidos, es necesario crear una variable del sistema llamada MPJ_HOME, el valor de esta será la dirección de la carpeta que contiene lo extraído del zip.
3. Luego de esto, es necesario agregar el starter .jar como librería del proyecto.
4. Finalmente, se debe agregar la variable MPJ_HOME antes creada al compilador.

# **Explicación del código fuente**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211836.png)

**Inicialización de MPI:** MPI.Init(args) inicializa el entorno de MPI. Esto debe ser llamado antes de cualquier otra llamada de MPI.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211842.png)

**Obtención del rango y tamaño del comunicador:** MPI.COMM_WORLD.Rank() devuelve el rango (ID) del proceso en el comunicador. MPI.COMM_WORLD.Size() devuelve el número total de procesos en el comunicador.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211857.png)

**Inicialización de las matrices:** Las matrices A y B se inicializan solo en el proceso raíz (rango 0). Los otros procesos inicializan A y B como null.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211902.png)

**Aplanamiento de las matrices:** Las matrices A y B se aplanan a una dimensión utilizando la función flatten (mostrada al final de la explicación). Esta función convierte una matriz bidimensional en una matriz unidimensional.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211918.png)

**Distribución de las matrices:** Las matrices aplanadas flatA y flatB se distribuyen a todos los procesos utilizando la función Bcast de MPI. Bcast envía el valor de la raíz a todos los otros procesos en el comunicador.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211924.png)

**Reforma de las matrices:** Las matrices aplanadas flatA y flatB se reforman a dos dimensiones utilizando la función reshape (mostrada al final junto con flatten). Esta función convierte una matriz unidimensional en una matriz bidimensional.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211954.png)

**Cálculo de la matriz resultante:** Cada proceso calcula una porción de la matriz resultante C utilizando el algoritmo de multiplicación de matrices. La porción que cada proceso calcula se determina dividiendo el número de filas de A por el número de procesos (A.length / numProcs).

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20211959.png)

**Aplanamiento de la matriz resultante:** La matriz resultante C se aplana a una dimensión utilizando la función flatten.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20212012.png)

**Recopilación de los resultados:** Los resultados parciales de cada proceso se recogen en el proceso raíz utilizando la función Gather de MPI. Gather recoge los valores de todos los procesos en el comunicador y los reúne en el proceso raíz.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20212017.png)

**Reforma de la matriz resultante:** La matriz resultante aplanada se reforma a dos dimensiones utilizando la función reshape.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20212025.png)

**Impresión de la matriz resultante:** Si el proceso es la raíz, imprime la matriz resultante.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20212030.png)

**Finalización de MPI:** MPI.Finalize() finaliza el entorno de MPI. Esto debe ser llamado después de todas las otras llamadas de MPI.

## Funciones extra

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20212037.png)

La función flatten toma una matriz bidimensional como entrada. Crea una nueva matriz unidimensional (array) con una longitud igual al número total de elementos en la matriz bidimensional (rows * cols). Luego, recorre cada elemento de la matriz bidimensional y lo coloca en la posición correspondiente en la matriz unidimensional.

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20212037.png)

La función reshape toma una matriz unidimensional y las dimensiones deseadas para la matriz bidimensional como entrada. Crea una nueva matriz bidimensional (matrix) con las dimensiones especificadas. Luego, recorre cada elemento de la matriz unidimensional y lo coloca en la posición correspondiente en la matriz bidimensional.

### Casos de prueba

**Matriz 2x2**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20221607.png)

**Salida**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20221620.png)

**Matriz 3x3**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20221238.png)

**Salida**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20221250.png)

**Matriz 4x4**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20221439.png)

**Salida**

![Captura del codigo](https://github.com/Armandogl14/MultMatrix/blob/main/imgs/Screenshot%202024-05-17%20221500.png)
