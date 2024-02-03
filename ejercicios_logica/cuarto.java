package ejercicios_logica;
import java.util.Random;

public class cuarto extends Thread {
    private static int extension;
    private static int[] array;
    private int start, end;

    public cuarto(int ini, int fin) {
        this.start = ini;
        this.end = fin;
    }

    public void run() {
        for (int i = this.start; i < this.end; i++) {
            array[i] = 10;
        }
    }

    public static void main(String[] args) {
        Random rand = new Random(System.nanoTime());

        // Asignar un valor a la variable extension
        extension = rand.nextInt(1000000) + 1;

        // Asegurarse de que la extensión sea positiva
        extension = Math.abs(extension);

        // Inicializar el array con la extensión aleatoria
        array = new int[extension];

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(10);
        }

        cuarto hilo1 = new cuarto(0, extension / 2);
        cuarto hilo2 = new cuarto(extension / 2, extension);

        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
    }
}
