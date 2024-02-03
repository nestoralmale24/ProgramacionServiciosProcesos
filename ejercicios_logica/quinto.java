package ejercicios_logica;
import java.util.concurrent.atomic.AtomicInteger;

public class quinto implements Runnable {

    public static double Tinicio, Tiempo;

    static int numNucleos = 0;
    static int Rango = 1; // Inicializado con un valor arbitrario
    public static AtomicInteger cont = new AtomicInteger(0); // Utilizando AtomicInteger

    public static int iteracionPorNucleo;

    public static Object Lock = new Object();

    public void run() {
        for (int i = 0; i < iteracionPorNucleo; i++) {
            cont.incrementAndGet(); // Incrementando de manera atómica
        }
    }

    quinto(int inicio, int fin) {
    }

    public static void main(String[] args) {

        Runtime runtime = Runtime.getRuntime();
        numNucleos = runtime.availableProcessors();

        int iteracionTotal = (int) (Math.random() * 1000000) + 1;
        iteracionPorNucleo = iteracionTotal / numNucleos;

        Thread[] hilos = new Thread[numNucleos];

        Tinicio = System.nanoTime();

        for (int i = 0; i < numNucleos; i++) {
            hilos[i] = new Thread(new quinto(i * iteracionPorNucleo, (i + 1) * iteracionPorNucleo));
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

        System.out.println("Numero de nucleos" + "=" + " " + numNucleos);
        System.out.println("valor: " + cont.get());
        System.out.println("En tiempo:" + (Tiempo / 1000000) + " milisegundos");
        System.out.println("Iteracion por nucleo" + " " + "=" + iteracionPorNucleo);
        System.out.println(iteracionTotal + " - " + cont.get());
        System.out.println(iteracionTotal - cont.get());
    }
}
