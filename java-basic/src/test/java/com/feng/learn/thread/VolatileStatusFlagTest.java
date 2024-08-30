package com.feng.learn.thread;

import com.feng.learn.basic.thread.Volatile.VolatileNotStatusFlag;
import com.feng.learn.basic.thread.Volatile.VolatileNotStatusFlag2;
import com.feng.learn.basic.thread.Volatile.VolatileStatusFlag;
import org.junit.Test;

public class VolatileStatusFlagTest {


    /**
     * work-thread will never stop. work-thread 看不到shutdown-thread 对 `shutdown`
     * 变量做的改变.
     */
    @Test
    public void testNoneVolatile() throws InterruptedException {
        VolatileNotStatusFlag statusFlag = new VolatileNotStatusFlag();
        new Thread(() -> statusFlag.doWork(), "work-thread").start();
        Thread.sleep(1000); // 等待线程启动完成
        new Thread(() -> statusFlag.close(), "shutdown-thread").start();
        Thread.sleep(30000);
    }

    /**
     * work-thread will stop just after shutdown-thread shutdown the flag.
     */
    @Test
    public void testVolatile() throws InterruptedException {
        VolatileStatusFlag statusFlag = new VolatileStatusFlag();

        new Thread(() -> statusFlag.doWork(), "work-thread").start();
        Thread.sleep(1000); // 等待线程启动完成
        new Thread(() -> statusFlag.close(), "shutdown-thread").start();
        Thread.sleep(30000);
    }

    /**
     * work-thread-1 will never see the change to `shutdown` field. work-thread-2
     * will never see the change to `shutdown` field. work-thread-3 will see the
     * change to `shutdown` field. why? work-thread-4 will see the change to
     * `shutdown` field. why?
     */
    @Test
    public void testNoneVolatile2() throws InterruptedException {
        VolatileNotStatusFlag2 statusFlag = new VolatileNotStatusFlag2();

        new Thread(() -> {
            while (!statusFlag.isShutdown()) {
                // do stuff
            }
            System.out.println(Thread.currentThread().getName() + " shutdown ...");
        }, "work-thread-1").start();
        new Thread(statusFlag::doWork, "work-thread-2").start();

        new Thread(() -> {
            while (!statusFlag.shutdown) {
                System.out.println(statusFlag.shutdown);
            }
            System.out.println(Thread.currentThread().getName() + " shutdown ...");
        }, "work-thread-3").start();

        new Thread(() -> {
            while (!statusFlag.isShutdown()) {
                System.out.println(statusFlag.isShutdown());
            }
            System.out.println(Thread.currentThread().getName() + " shutdown ...");
        }, "work-thread-4").start();

        Thread.sleep(1000); // 等待线程启动完毕

        statusFlag.close();

        Thread.sleep(30000);
    }

}