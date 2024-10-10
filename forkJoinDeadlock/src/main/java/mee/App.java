package mee;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.IntStream;





/**
 * Hello world!
 */
public class App {
    Semaphore semaphore;
    public static void main(String[] args) {
        App app = new App();
        app.runTest();
    }

    private void runTest() {
        //forkJoinPoolStream(); 
        parallelStreamSemaphore();
    }

    private void parallelStreamSemaphore() {
        semaphore = new Semaphore(2);

        try {
            IntStream.range(1, 20).parallel().forEach((page) -> {
                System.out.println(Thread.currentThread() + " Number of active thread: " + Thread.activeCount());
                try {
                    //System.out.println("THREAD NUM FROM MAIN : " + page);
                    //System.out.println(Thread.currentThread() + " ACQUIRING SEM " + semaphore.availablePermits());
                    semaphore.acquire();

                    soutSomeShit(page);

                    //System.out.println(Thread.currentThread() + "RELEASING SEM " + semaphore.availablePermits());
                    semaphore.release();
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                //   });
        });
    } catch (Exception e) {
        System.out.println(e);
    }
}

private void forkJoinPoolStream() {
    ForkJoinPool forkJoinPool1 = new ForkJoinPool(10);
    try {
        forkJoinPool1.submit(() -> {
            System.out.println("PARA MAIN: " + forkJoinPool1.getParallelism());
            IntStream.range(1, 200).parallel().forEach((page) -> {
                System.out.println(Thread.currentThread() + " Number of active thread: " + Thread.activeCount());
                try {
                    //System.out.println("THREAD NUM FROM MAIN : " + page);
                    //System.out.println(Thread.currentThread() + " ACQUIRING SEM " + semaphore.availablePermits());

                    soutSomeShit(page);

                    //System.out.println(Thread.currentThread() + "RELEASING SEM " + semaphore.availablePermits());
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        });
    } catch (Exception e) {
        System.out.println(e);
    }
    forkJoinPool1.close();
}

private void soutSomeShit(final int test) throws Exception {
    ForkJoinPool forkJoinPool = new ForkJoinPool(20);
    forkJoinPool.submit(() -> {
        try{
            System.out.println("TEST " + test);
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }).join();
    forkJoinPool.close();
}

private void functionToExecute() throws InterruptedException, ExecutionException{
    ForkJoinPool forkJoinPool1 = new ForkJoinPool(4);
    System.out.println("PARA SUB: " + forkJoinPool1.getParallelism());
    forkJoinPool1.submit(() -> {
        IntStream.range(1, 20).parallel().forEach((value) -> {
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread() + " EXECUTING INNER FUNCITON USING JOIN POOL TOO: " + value);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }).get();
    forkJoinPool1.close();
}
}
