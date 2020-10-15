package BlockingQueueImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author : chara
 */
public class MultipleProducerMultipleConsumerModel {
    private final BlockingQueue<Object> objects = new LinkedBlockingDeque<>(5);
    private final Object takeAndSizeLock = new Object();

    public static void main(String[] args) {
        new MultipleProducerMultipleConsumerModel().run();
    }

    public void run(){
        for(int i = 0;i < 5;i++){
            new Thread(new Consumer(),"c" + i).start();
        }
        for(int i = 0;i < 5;i++){
            new Thread(new Producer(),"p" + i).start();
        }
    }

    class Consumer implements Runnable{
        public void run() {
            while(true){
                System.out.println("Consumer " + Thread.currentThread().getName() + "  is running");
                synchronized (takeAndSizeLock){
                    while(objects.poll() == null){
                        System.out.println("Consumer " + Thread.currentThread().getName() + " waiting for an object");
                        try {
                            takeAndSizeLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consumer " + Thread.currentThread().getName() +" finished");
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
                    System.out.println("Producer " + Thread.currentThread().getName() + " is running");
                    synchronized (takeAndSizeLock){
                        while(!objects.offer(new Object())){
                            System.out.println("Producer " + Thread.currentThread().getName() + " waiting for the list to be not full");
                            takeAndSizeLock.wait();
                        }
                        System.out.println("Producer " + Thread.currentThread().getName() + " finished");
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
