package ejercicios_logica;
public class primero extends Thread{

    private int id;
    public primero(int id){
        this.id = id;
    }

    public void run(){
        System.out.println("Soy el hilo:" + id);
    }

    public static void main(String[] args){
        primero Hilo1 = new primero(1);
        primero Hilo2 = new primero(2);
        primero Hilo3 = new primero(3);

        Hilo1.start();
        Hilo2.start();
        Hilo3.start();

        System.out.println("Soy el hilo principal");
    }
}
