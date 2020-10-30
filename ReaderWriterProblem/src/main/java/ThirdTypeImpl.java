import java.util.concurrent.Semaphore;

/**
 * @author : chara
 */
public class ThirdTypeImpl {
    private final Semaphore mutex = new Semaphore(1);//only for protect the change of readCount;
    private final Semaphore wrMutex = new Semaphore(1);
    private final Semaphore queueMutex = new Semaphore(1);
    private int readCount = 0;
    private String data = "hello";

    public static void main(String[] args) {
        new ThirdTypeImpl().run();
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
                    queueMutex.acquire();
                    mutex.acquire();
                    readCount++;
                    if(readCount == 1){
                        wrMutex.acquire();
                    }
                    queueMutex.release();
                    mutex.release();
                    System.out.println("read from reader " + Thread.currentThread().getName() + " ->" + data);

                    mutex.acquire();
                    readCount--;
                    if(readCount == 0){
                        wrMutex.release();
                    }
                    mutex.release();
//                    Thread.sleep(2000);
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
                    queueMutex.acquire();
                    wrMutex.acquire();
                    queueMutex.release();
                    data = "hello from writer " + Thread.currentThread().getName();
                    System.out.println("write from writer " + Thread.currentThread().getName() + " ->" + data);
                    wrMutex.release();
//                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
