public class matriz_inicio_final implements Runnable {
    int inicio;
    int fin;
    int id;

    matriz_inicio_final(int inicio, int fin, int id) {
        this.inicio = inicio;
        this.fin = fin;
        this.id = id;
    }

    public void run() {
        //MOSTRAMOS EL INICIO Y EL FIN DE LA ITERACION DE CADA HILO
        System.out.println("Hilo " + id + ": Iteración inicial " + inicio + ", Iteración final " + fin);

        for (int i = inicio; i < fin; i++) {
            for (int j = 0; j < 2100; j++) {
                if (i < 3800) {
                    int valorPixel = Imagen[i][j];

                    if (valorPixel > 255 / 2) {
                        // RESTAMOS 10 
                        Imagen[i][j] = Math.max(255 / 2, valorPixel - 10);
                    } else {
                        // SUMAMOS 10
                        Imagen[i][j] = Math.min(255 / 2, valorPixel + 10);
                    }
                }
            }
        }
    }

    static int[][] Imagen;
    static int NumNucleos;
    static int iteracionesTotales;
    public static double Tinicio, Tiempo;

    public static void main(String[] args) {
        Tinicio = System.nanoTime();

        //SACAMOS EL NUMERO TOTAL DE ITERACIONES
        iteracionesTotales = 3800 * 2100;
        System.out.println("Iteraciones totales: " + iteracionesTotales);

        


        //MOSTRAMOS NUCLEOS DE MI ORDENADOR
        Runtime runtime = Runtime.getRuntime();
        NumNucleos = runtime.availableProcessors();
        System.out.println("Número de núcleos: " + NumNucleos);        
        System.out.println("");       
        System.out.println("");
        System.out.println("\033[4mRESULTADO ITERACIONES POR HILO\033[0m"); 
        
        Imagen = new int[3800][2100];

        for (int i = 0; i < 3800; i++) {
            for (int j = 0; j < 2100; j++) {
                Imagen[i][j] = (int) (Math.random() * 255);
            }
        }

        //HACEMOS LA REPARTICION DE ITERACIONES
        int iteracionesPorHilo = 3800 / NumNucleos;

        Thread[] hilos = new Thread[NumNucleos];
        int inicio = 0;
        for (int i = 1; i <= NumNucleos - 1; i++) {
            int fin = inicio + iteracionesPorHilo;
            hilos[i - 1] = new Thread(new matriz_inicio_final(inicio, fin, i));
            hilos[i - 1].start();
            inicio = fin;
        }

        //ASIGNAMOS ULTIMA ITERACION AL HILO 16
        int finUltimoHilo = 3800;
        hilos[NumNucleos - 1] = new Thread(new matriz_inicio_final(inicio, finUltimoHilo, 16));
        hilos[NumNucleos - 1].start();

        for (int i = 0; i < NumNucleos; i++) {
            try {
                hilos[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //MOSTRAMOS TIEMPO EL MILISEGUNDOS
        System.out.println("");
        System.out.println("\033[4mRESULTADO TIEMPO TOTAL DE PROCESAMIENTO\033[0m");
        Tiempo = System.nanoTime() - Tinicio;
        System.out.println("En tiempo:" + (Tiempo / 1000000) + " milisegundos");
    }
}
