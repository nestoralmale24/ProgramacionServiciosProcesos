package MonitorHilosPoolThread;

class Monitor {
 
    private int[][][] Imagenes; // DECLARAMOS LA IMAGEN (30)
   
    public Monitor(int[][][] Imagenes) {
        this.Imagenes = Imagenes;
    }
 
    public synchronized void sumarPixeles(int imagenPX) {
            int[][] Imagen = Imagenes[imagenPX];
            for (int i = 0; i < Imagen.length; i++) {
                for (int j = 0; j < Imagen[i].length; j++) {
                    int ValorPixel = Imagen[i][j];
                    int ValorNuevo = (int) (ValorPixel * 1.1); // AUMENTAMOS EL 10% EL VALOR
                    Imagen[i][j] = Math.min(255, ValorNuevo); // FIJAMOS EL LIMITE EL 255
                }
            }
    }
}