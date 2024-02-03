package ejercicios_logica;
//CASO DE INDETERMINISMO TODOS ACCEDEN A LO MISMO Y CHOCA
public class tercero extends Thread {
    public static int contador = 0;

    public void run() {
        for (int i = 0; i < 1000; i++) {
            contador++;
            System.out.println(contador);
        }
    }

    public static void main(String[] args) {
        tercero[] array = new tercero[1000];

        for (int i = 0; i < array.length; i++) {
            array[i] = new tercero();
            array[i].start();
        }

        try {
            for (int i = 0; i < array.length; i++) {
                array[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(contador);
    }
}
