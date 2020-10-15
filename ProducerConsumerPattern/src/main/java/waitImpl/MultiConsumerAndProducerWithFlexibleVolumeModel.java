package waitImpl;

/**
 * @author jinaxCai
 * implemented with wait
 * multiple consumers and producers and multiple volumes
 * may implemented with only one lock for consumers and producers(when produce or consume a good, notify all consumers and producers)
 * note : wait would release the lock in hand, after got notified, the thread will reget the lock!(thus currentLock is not necessary, but if use different locks for c and p, then currentLock is needed when updating current)
 */
public class MultiConsumerAndProducerWithFlexibleVolumeModel {
    final Object o1,o2;//o1 is lock for consumer;o2 is lock for producer
    int volume;
    int current;
    final Object currentLock = new Object();
    private int cCount,pCount;

    public static void main(String[] args) {
        new MultiConsumerAndProducerWithFlexibleVolumeModel(10,5,5);
    }

    public MultiConsumerAndProducerWithFlexibleVolumeModel(int volume,int cCount,int pCount) {
        this.o1 = new Object();
        this.o2 = new Object();
        current = 0;
        this.volume = volume;
        this.cCount = cCount;
        this.pCount = pCount;

        for(int i = 0;i < cCount;i++){
            new Thread(new Consumer(),"C" + i).start();
        }
        for(int i = 0;i < pCount;i++){
            new Thread(new Producer(),"P" + i).start();
        }
    }

    class Consumer implements Runnable{

        public void run() {
            while(true){
                synchronized (o1){
                    while(current <= 0){//must be while here
                        try {
                            o1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consumer" + Thread.currentThread().getName() + " is running");
                    synchronized (currentLock){
                        current--;
                        System.out.println("Consumer" + Thread.currentThread().getName() + " finished " +
                                           "currentCount is " + current);
                    }
                }
//                synchronized (currentLock){
//                    if(current <= 0){
//                        continue;
//                    }
//                }

//                System.out.println("Consumer" + Thread.currentThread().getName() + " is running");
//                synchronized (currentLock){
//                    current--;
//                    System.out.println("Consumer" + Thread.currentThread().getName() + " finished" +
//                                       "currentCount is " + current);
//                }
                synchronized (o2){
                    o2.notifyAll();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Producer implements Runnable{

        public void run() {
            while(true){
                synchronized (o2){
                    while(current >= volume){
                        try {
                            o2.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Producer" + Thread.currentThread().getName() + " is running");
                    synchronized (currentLock){
                        current++;
                        System.out.println("Producer" + Thread.currentThread().getName() + " finished " +
                                           "currentCount is " + current);
                    }
                }
//                synchronized (currentLock){
//                    if(current  >= volume){
//                        continue;
//                    }
//                }
//                System.out.println("Producer" + Thread.currentThread().getName() + " is running");
//                synchronized (currentLock){
//                    current++;
//                    System.out.println("Producer" + Thread.currentThread().getName() + " finished" +
//                                       "currentCount is " + current);
//                }
                synchronized (o1){
                    o1.notifyAll();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
