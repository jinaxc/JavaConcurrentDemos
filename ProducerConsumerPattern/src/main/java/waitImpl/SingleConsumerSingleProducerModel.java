package waitImpl;

/**
 * @author jinaxCai
 * implemented with wait
 * producer and consumer with only one volumn
 * note : the use of wait must be inside a synchronized block
 */
public class SingleConsumerSingleProducerModel {
    final Object o1,o2;
    boolean cRunning,pRunning;

    public static void main(String[] args) {
        SingleConsumerSingleProducerModel m = new SingleConsumerSingleProducerModel();
        m.run();
    }

    public SingleConsumerSingleProducerModel() {
        o1 = new Object();
        o2 = new Object();
        pRunning = true;
        cRunning = false;
    }

    public void run(){
        new Thread(new Consumer()).start();
        new Thread(new Producer()).start();
    }

    class Consumer implements Runnable{

        public void run() {
            synchronized (o1){
                while(!cRunning){
                    try {
                        o1.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Consumer is running");
            cRunning = false;
            pRunning = true;
            synchronized (o2){
                o2.notify();
            }

        }
    }

    class Producer implements Runnable{

        public void run() {
            synchronized (o2){
                while(!pRunning){
                    try {
                        o2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Producer is running");
            pRunning = false;
            cRunning = true;
            synchronized (o1){
                o1.notify();
            }
        }
    }
}
