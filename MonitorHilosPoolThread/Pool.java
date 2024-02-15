package MonitorHilosPoolThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pool implements Runnable {

    public static int[][][] Imagenes;
    public static int NumNucleos;
    public static double inicio, Tiempo;
    private int id;
    static int iteracionesTotales;
    private Monitor monitor;
    private int imagenPX;

    Pool(Monitor monitor, int id, int imagenPX) {
        this.monitor = monitor;
        this.imagenPX = imagenPX;
        this.id = id;
    }

    public void run() {
        long threadId = Thread.currentThread().threadId();
        monitor.sumarPixeles(imagenPX);
        System.out.println("El Hilo " + id + " proceso la imagen " + imagenPX + " en el núcleo " + (threadId % NumNucleos));
    }

    public static void main(String[] args) {
        inicializarImagenes();
        ejecutarPool();
    }

    private static void inicializarImagenes() {
        Runtime runtime = Runtime.getRuntime();
        NumNucleos = runtime.availableProcessors();
        inicio = System.nanoTime();
        Tiempo = System.nanoTime() - inicio;

        Imagenes = new int[30][3800][2100];
        for (int k = 0; k < 30; k++) {
            for (int i = 0; i < 3800; i++) {
                for (int j = 0; j < 2100; j++) {
                    Imagenes[k][i][j] = (int) (Math.random() * 255);
                }
            }
        }
    }

    private static void ejecutarPool() {
        Monitor monitor = new Monitor(Imagenes);
        ExecutorService executor = Executors.newFixedThreadPool(NumNucleos / 2);

        for (int i = 1; i <= 30; i++) { // NUESTRO BUCLE EMPEZARA EN LA IMAGEN 1 Y TERMINARA EN LA 3O, ASI NOS EVITAMOS QUE VAYA DE 0 A 29
            executor.execute(new Pool(monitor, i, i - 1));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("");

        System.out.println("\033[4mEn tiempo: " + (Tiempo / 1000000) + " milisegundos\033[0m");
        System.out.println("");

        System.out.println("\033[4mNumero total de nucleos de mi ordenador: " + NumNucleos + "\033[0m");
        System.out.println("");

        System.out.println("\033[4mEl número de núcleos utilizados: " + NumNucleos / 2 + "\033[0m");
    }
}
