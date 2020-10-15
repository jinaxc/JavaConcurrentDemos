package BlockingQueueImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author : chara
 * implemented with BlockingQueue
 * one consumer and one producer(easy to expand with blockingQueue)
 * note : the lock{takeAndSizeLock} is not necessary, here is just for testing.
 *        If do not need this, then to operation with the Queue just use take and put is OK
 */
public class SingleProducerSingleConsumerModel {
    private final BlockingQueue<Object> objects = new LinkedBlockingDeque<>(5);
    private final Object takeAndSizeLock = new Object();

    public static void main(String[] args) {
        new SingleProducerSingleConsumerModel().run();
    }

    public void run(){
        new Thread(new Consumer()).start();
        new Thread(new Producer()).start();
    }

    class Consumer implements Runnable{

        public void run() {
            while(true){
                System.out.println("Consumer is running");
                synchronized (takeAndSizeLock){
                    while(objects.poll() == null){
                        System.out.println("Consumer waiting for an object");
                        try {
                            takeAndSizeLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consumer finished");
                    System.out.println("current Count is " + objects.size());
                    takeAndSizeLock.notifyAll();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Producer implements Runnable{

        public void run() {
            while(true){
                try {
                    System.out.println("Producer is running");
                    synchronized (takeAndSizeLock){
                        while(!objects.offer(new Object())){
                            System.out.println("Producer waiting for the list to be not full");
                            takeAndSizeLock.wait();
                        }
                        System.out.println("Producer finished");
                        System.out.println("current Count is " + objects.size());
                        takeAndSizeLock.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
