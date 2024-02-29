import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class hilos {

    private static final int NUMERO_FILAS = 3800;
    private static final int NUMERO_COLUMNAS = 2100;
    public static double Tinicio, Tiempo;

    public static void main(String[] args) {

        //CALCULO NUMERO DE NUCLEOS TOTALES ORDENADOR
        int numNucleos = Runtime.getRuntime().availableProcessors();

        //INICIALIZO LA MATRI
        int[][] matriz = new int[NUMERO_FILAS][NUMERO_COLUMNAS];
        inicializarMatriz(matriz);

        //HAGO EL CALCULO TOTAL DE ITERACIONES POR HILO 
        int iteracionesTotales = NUMERO_FILAS * NUMERO_COLUMNAS;
        int iteracionesPorNucleo = iteracionesTotales / numNucleos;

        //SACO EL TIEMPO TOTAL DEL PROCESO
        Tiempo = System.nanoTime() - Tinicio;
        System.out.println("En tiempo:" + (Tiempo / 1000000) + " milisegundos");

        //NUMERO DE ITERACIONES TOTALES
        System.out.println("Iteraciones totales: " + iteracionesTotales);

        ExecutorService executorService = Executors.newFixedThreadPool(numNucleos);
        Future<?>[] futures = new Future[numNucleos];

        for (int i = 0; i < numNucleos; i++) {
            int inicio = i * iteracionesPorNucleo;
            int fin = (i == numNucleos - 1) ? iteracionesTotales : (i + 1) * iteracionesPorNucleo;

            Runnable tarea = new TareaMatriz(matriz, inicio, fin, i);
            futures[i] = executorService.submit(tarea);
        }

        for (int i = 0; i < numNucleos; i++) {
            try {
                futures[i].get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    private static void inicializarMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = (int) (Math.random() * 256);
            }
        }
    }
}

class TareaMatriz implements Runnable {
    private final int[][] matriz;
    private final int inicio;
    private final int fin;
    private final int idHilo;

    public TareaMatriz(int[][] matriz, int inicio, int fin, int idHilo) {
        this.matriz = matriz;
        this.inicio = inicio;
        this.fin = fin;
        this.idHilo = idHilo;
    }

    @Override
    public void run() {
        int iteracionesHilo = 0;
        for (int i = inicio; i < fin; i++) {
            int fila = i / matriz[0].length;
            int columna = i % matriz[0].length;

            // Obtener el valor aleatorio original
            int valorOriginal = matriz[fila][columna];

            int mitad = 128;
            if (valorOriginal > mitad) {
                matriz[fila][columna] = Math.max(mitad, valorOriginal - 10);
            } else {
                matriz[fila][columna] = Math.min(mitad, valorOriginal + 10);
            }
            iteracionesHilo++;
        }
        System.out.println("Iteraciones por hilo " + idHilo + ": " + iteracionesHilo);
    }
}
