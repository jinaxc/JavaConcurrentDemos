package joinImpl;

/**
 * @author jinaxCai
 * use join
 * make sure that C runs before B runs before A
 */
public class Main {
    static Thread t1,t2,t3;

    public static void main(String[] args) {
        t1 = new Thread(new ThreadA());
        t2 = new Thread(new ThreadB());
        t3 = new Thread(new ThreadC());
        t1.start();
        t2.start();
        t3.start();
    }
    static class ThreadA implements Runnable{

        public void run() {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("A is running");
        }
    }

    static class ThreadB implements Runnable{

        public void run() {
            try {
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B is running");
        }
    }

    static class ThreadC implements Runnable{
        public void run() {
            System.out.println("C is running");
        }
    }
}


