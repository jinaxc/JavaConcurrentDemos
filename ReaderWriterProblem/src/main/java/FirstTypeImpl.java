

import java.util.concurrent.Semaphore;

/**
 * @author : chara
 * the implement for readers-preference(reader wont wait if another reader is reading; write and read is incompatible)
 */
public class FirstTypeImpl {
    private final Semaphore mutex = new Semaphore(1);//only for protect the change of readCount;
    private final Semaphore wrMutex = new Semaphore(1);
    private int readCount = 0;
    private String data = "hello";

    public static void main(String[] args) {
        new FirstTypeImpl().run();
    }

    public void run(){
        for(int i = 0;i < 4;i++){
            new Thread(new Reader(),"" + i).start();
        }
        for(int i = 0;i < 4;i++){
            new Thread(new Writer(),"" + i).start();
        }
    }

    class Reader implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    mutex.acquire();
                    readCount++;
                    if(readCount == 1){
                        wrMutex.acquire();
                    }
                    mutex.release();

                    System.out.println("read from reader " + Thread.currentThread().getName() + " ->" + data);

                    mutex.acquire();
                    readCount--;
                    if(readCount == 0){
                        wrMutex.release();
                    }
                    mutex.release();
                    Thread.sleep(2000);//to see that writer will starve, set sleep time to 0
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Writer implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    wrMutex.acquire();
                    data = "hello from writer " + Thread.currentThread().getName();
                    System.out.println("write from writer " + Thread.currentThread().getName() + " ->" + data);
                    wrMutex.release();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
