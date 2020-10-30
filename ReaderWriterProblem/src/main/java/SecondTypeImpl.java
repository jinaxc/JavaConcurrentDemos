import java.util.concurrent.Semaphore;

/**
 * @author : chara
 */
public class SecondTypeImpl {
    private final Semaphore mutex = new Semaphore(1);//only to protect the change of readCount;
    private final Semaphore wMutex = new Semaphore(1);//only to protect the change of writeCount;
    private final Semaphore wrMutex = new Semaphore(1);
    private final Semaphore resourceLock = new Semaphore(1);
    private int writeCount = 0;
    private int readCount = 0;
    private String data = "hello";

    public static void main(String[] args) {
        new SecondTypeImpl().run();
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
                    wrMutex.acquire();
                    mutex.acquire();
                    readCount++;
                    if(readCount == 1){
                        resourceLock.acquire();
                    }
                    mutex.release();
                    System.out.println("read from reader " + Thread.currentThread().getName() + " ->" + data);
                    wrMutex.release();

                    mutex.acquire();
                    readCount--;
                    if(readCount == 0){
                        resourceLock.release();
                    }
                    mutex.release();
                    Thread.sleep(100);
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
                    wMutex.acquire();
                    // protect writeCount
                    writeCount++;
                    if(writeCount == 1){
                        wrMutex.acquire();
                    }
                    wMutex.release();

                    //make sure that only one writer write every time
                    resourceLock.acquire();
                    data = "hello from writer " + Thread.currentThread().getName();
                    System.out.println("write from writer " + Thread.currentThread().getName() + " ->" + data);
                    resourceLock.release();

                    wMutex.acquire();
                    // protect writeCount
                    writeCount--;
                    if(writeCount == 0){
                        wrMutex.release();
                    }
                    wMutex.release();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
