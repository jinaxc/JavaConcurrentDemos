package SemaphoreImpl;

import java.util.concurrent.Semaphore;

/**
 * @author : chara
 * implemented with BlockingQueue
 * one consumer and one producer
 * note : Semaphore is like the number of licenses, if only one or zero, then it is like a mutex
 */
public class SingleConsumerAndSingleProducerModel {
    private final Semaphore notFull = new Semaphore(10);
    private final Semaphore notEmpty = new Semaphore(0);
    private final Semaphore currentLock = new Semaphore(1);
    private final int volume;
    private int current;

    public SingleConsumerAndSingleProducerModel(int volume) {
        this.volume = volume;
        current = 0;
    }
    public void run(){
        new Thread(new Consumer()).start();
        new Thread(new Producer()).start();
    }

    public static void main(String[] args) {
        new SingleConsumerAndSingleProducerModel(4).run();
    }

    class Consumer implements Runnable{
        public void run() {
            while(true){
                try {
                    System.out.println("Consumer is running");
                    while(current <= 0){
                        notEmpty.acquire();
                    }
                    currentLock.acquire();
                    current--;
                    System.out.println("Consumer finished" + " current Count is " + current);
                    currentLock.release();
                    notFull.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(3000);//only for testing
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
                    while(current >= volume){
                        notFull.acquire();
                    }
                    currentLock.acquire();
                    current++;
                    System.out.println("Producer finished" + " current Count is " + current);
                    currentLock.release();
                    notEmpty.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);//only for testing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
