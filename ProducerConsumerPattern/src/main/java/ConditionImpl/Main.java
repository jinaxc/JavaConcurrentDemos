package ConditionImpl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jinaxCai
 * implemented with lock and condition
 * one consumer and one producer with multiple volumes
 * consumer and producer cant work at the same time because they are on the same lock
 * note: before using some Condition, the lock of the condition must be got first
 * note: the use of lock must be first getLock by lock.lock and use a try-finally to release the lock
 */
public class Main {
    final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final int volume;
    private int current;

    public static void main(String[] args) {
        new Main(5).run();
    }

    public Main(int volume) {
        this.volume = volume;
        current = 0;
    }

    public void run(){
        new Thread(new Consumer()).start();
        new Thread(new Producer()).start();
    }

    class Consumer implements Runnable{
        public void run() {
            while(true){
                lock.lock();
                try{
                    while(current <= 0){
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consumer is running");
                    current--;
                    System.out.println("Consumer finished " +
                            "currentCount is " + current);
                    notFull.signal();
                }
                finally {
                    lock.unlock();
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
                lock.lock();
                try{
                    while(current>=volume){
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Producer is running");
                    current++;
                    System.out.println("Producer finished " +
                            "currentCount is " + current);
                    notEmpty.signal();
                }finally {
                    lock.unlock();
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
