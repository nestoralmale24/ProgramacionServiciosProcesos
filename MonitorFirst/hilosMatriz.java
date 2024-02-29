import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class hilosMatriz {

    private static int[][] imagen; // Matriz de la imagen
    private static int puntoMedio;
    public static double Tinicio, Tiempo;
    static int numNucleos = 0;
    static int Rango = 1;
    public static int iteracionPorNucleo;
    public static AtomicInteger cont = new AtomicInteger(0); // Contador atómico para llevar un registro del progreso

    public static Object Lock = new Object(); // Objeto de bloqueo para la sincronización de hilos
    public static int[] iteracionesPorHilo; // para almacenar iteraciones por cada hilo

    public static void main(String[] args) {

        //Obtenemos el numero de nucleos totales
        Runtime runtime = Runtime.getRuntime();
        numNucleos = runtime.availableProcessors();

        // Generamos un número total de iteraciones aleatoriamente
        int iteracionTotal = 3800 * 2100;
        iteracionPorNucleo = iteracionTotal / numNucleos;

        // hilos e iteraciones por hilo
        Thread[] hilos = new Thread[numNucleos];
        iteracionesPorHilo = new int[numNucleos];

        Tinicio = System.nanoTime();

        for (int i = 0; i < numNucleos; i++) {
            hilos[i] = new Thread(new tarea(i * iteracionPorNucleo, (i + 1) * iteracionPorNucleo, i));
            hilos[i].start();
        }

        for (int i = 0; i < numNucleos; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Tiempo = System.nanoTime() - Tinicio;
        System.out.println("En tiempo:" + (Tiempo / 1000000) + " milisegundos");
        System.out.println("Numero de nucleos" + "=" + " " + numNucleos);


        // Imprimir las iteraciones por cada hilo
        for (int i = 0; i < numNucleos; i++) {
            System.out.println("Iteraciones por hilo " + i + ": " + iteracionesPorHilo[i]);
        }

        int iteracionTotalCalculada = 0;
        for (int i = 0; i < numNucleos; i++) {
            iteracionTotalCalculada += iteracionesPorHilo[i];
        }

        System.out.println("Iteracion total calculada: " + iteracionTotalCalculada);

        imagen = new int[3800][2100];
        inicializarMatriz();

        calcularPuntoMedio();

        int numHilos = Runtime.getRuntime().availableProcessors();

        ExecutorService executorService = Executors.newFixedThreadPool(numHilos);

        //Dividir la matriz en filas y asignar tareas a los hilos
        int filasPorHilo = imagen.length / numHilos;
        int inicio = 0;
        int fin = filasPorHilo;

        try {
            for (int i = 0; i < numHilos; i++) {
                Runnable worker = new ProcesadorImagen(inicio, fin, imagen, puntoMedio);
                executorService.execute(worker);
                inicio = fin;
                fin = Math.min(fin + filasPorHilo, imagen.length);
            }
            executorService.shutdown();
            while (!executorService.isTerminated()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metodo para inicializar la matriz
    private static void inicializarMatriz() {
        for (int i = 0; i < imagen.length; i++) {
            for (int j = 0; j < imagen[i].length; j++) {
                imagen[i][j] = (int) (Math.random() * 255);
            }
        }
    }

    //Metodo para calcular el punto medio de la matriz
    private static void calcularPuntoMedio() {
        int sumaTotal = 0;
        for (int i = 0; i < imagen.length; i++) {
            for (int j = 0; j < imagen[i].length; j++) {
                sumaTotal += imagen[i][j];
            }
        }
        puntoMedio = sumaTotal / (imagen.length * imagen[0].length);
    }
}

class tarea implements Runnable {
    private final int inicio;
    private final int fin;
    private final int idHilo;

    public tarea(int inicio, int fin, int idHilo) {
        this.inicio = inicio;
        this.fin = fin;
        this.idHilo = idHilo;
    }

    //Incrementar el contador de iteraciones por hilo
    @Override
    public void run() {
        for (int i = inicio; i < fin; i++) {
            hilosMatriz.iteracionesPorHilo[idHilo]++;
            hilosMatriz.cont.incrementAndGet();
        }
    }
}

    //RUNNABLE para poder procesar la imagen
class ProcesadorImagen implements Runnable {
    private final int inicioFila;
    private final int finFila;
    private final int[][] imagen;
    private final int puntoMedio;

    public ProcesadorImagen(int inicioFila, int finFila, int[][] imagen, int puntoMedio) {
        this.inicioFila = inicioFila;
        this.finFila = finFila;
        this.imagen = imagen;
        this.puntoMedio = puntoMedio;
    }

    @Override
    public void run() {
        for (int i = inicioFila; i < finFila; i++) {
            for (int j = 0; j < imagen[i].length; j++) {
                if (imagen[i][j] > puntoMedio) {
                    imagen[i][j] = Math.min(imagen[i][j], puntoMedio);
                } else {
                    imagen[i][j] = Math.max(imagen[i][j], puntoMedio);
                }
            }
        }
    }
}
