package ConditionImpl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jinaxCai
 * note: before using some Condition, the lock of the condition must be got first
 * note: the use of lock must be first getLock by lock.lock and use a try-finally to release the lock
 */
public class Main {
    final Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private  Condition notEmpty = lock.newCondition();
    private int volume;
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
                    while(current <= volume){
                        try {
                            notEmpty.await();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Consumer is running");
                        notFull.signal();

                    }
                }
                finally {
                    lock.unlock();
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
                    notEmpty.signal();
                }finally {
                    lock.unlock();
                }

            }



        }
    }
}
