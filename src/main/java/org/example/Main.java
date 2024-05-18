package org.example;
import mpi.*;

public class Main {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int root = 0;
        int myRank = MPI.COMM_WORLD.Rank();
        int numProcs = MPI.COMM_WORLD.Size();

        // Inicializar las matrices A y B solo en el proceso raíz (rango 0)
        double[][] A = myRank == root ? new double[][]{{5, 22, 4}, {45, 6, 51}, {8, 10, 25}} : null;
        double[][] B = myRank == root ? new double[][]{{6, 28, 14}, {41, 11, 12}, {10, 7, 5}} : null;

        // Aplanar las matrices a una dimensión
        double[] flatA = myRank == root ? flatten(A) : new double[A.length * A[0].length];
        double[] flatB = myRank == root ? flatten(B) : new double[B.length * B[0].length];

        // Distribuir las filas de las matrices A y B a todos los procesos utilizando la función Bcast() de MPI
        MPI.COMM_WORLD.Bcast(flatA, 0, flatA.length, MPI.DOUBLE, root);
        MPI.COMM_WORLD.Bcast(flatB, 0, flatB.length, MPI.DOUBLE, root);

        // Reformar las matrices a dos dimensiones
        A = reshape(flatA, A.length, A[0].length);
        B = reshape(flatB, B.length, B[0].length);

        // Cada proceso calculará una porción de la matriz resultante C utilizando el algoritmo de multiplicación de matrices
        int size = A.length / numProcs;
        double[][] C = new double[size][B[0].length];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < B[0].length; j++)
                for (int k = 0; k < B.length; k++)
                    C[i][j] += A[i + myRank * size][k] * B[k][j];

        // Aplanar la matriz C a una dimensión
        double[] flatC = flatten(C);

        // Reunir los resultados parciales de cada proceso en el proceso raíz utilizando la función Gather() de MPI
        double[] result = myRank == root ? new double[A.length * B[0].length] : null;
        MPI.COMM_WORLD.Gather(flatC, 0, flatC.length, MPI.DOUBLE, result, 0, flatC.length, MPI.DOUBLE, root);

        // Reformar la matriz resultante a dos dimensiones
        if (myRank == root) {
            double[][] finalResult = reshape(result, A.length, B[0].length);

            // Imprimir la matriz resultante en el proceso raíz
            for (double[] row : finalResult)
                System.out.println(java.util.Arrays.toString(row));
        }

        MPI.Finalize();
    }

    // Función para aplanar una matriz bidimensional a una dimensión
    private static double[] flatten(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] array = new double[rows * cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                array[i * cols + j] = matrix[i][j];
        return array;
    }

    // Función para reformar una matriz unidimensional a dos dimensiones
    private static double[][] reshape(double[] array, int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                matrix[i][j] = array[i * cols + j];
        return matrix;
    }
}
