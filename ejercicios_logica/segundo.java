package ejercicios_logica;
public class segundo extends Thread{
    private int id;
    public segundo(int id){
        this.id = id;
    }

    public void run(){
        System.out.println("Soy el hilo:" + id);
    }

    public static void main(String[] args) {
        segundo[] array = new segundo[5];

        for(int i = 0; i < array.length; i++){
            array[i] = new segundo(i+1);
            array[i].start();
        }

        try {
            for(int i = 0; i <array.length; i++){
                array[i].join();
            }
        } catch (Exception e) {
            
        }
        System.out.println("Soy el hilo principal");
    }
}
