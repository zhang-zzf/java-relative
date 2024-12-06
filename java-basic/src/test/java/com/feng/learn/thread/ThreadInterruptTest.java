package com.feng.learn.thread;

import org.junit.Test;

/**
 * 提交线程执行的循环任务必须要响应线程中断，否则线程是不能被正常终止的。
 * <p>LoopTask 中的 Thread.sleep() 抛出的 InterruptedException 必须要根据业务逻辑正常响应</p>
 * <p>LoopTask2 中在合适位置检测线程中断信号</p>
 *
 * @see LoopTask
 * @see LoopTask2
 */
public class ThreadInterruptTest {

    /**
     * a-test-thread线程 不会停止
     */
    @Test
    public void testThreadInterruptContrast() throws InterruptedException {
        Runnable task = new LoopTask();
        Thread thread = new Thread(task, "a-test-thread");
        thread.start();
        //        Thread.sleep(1000); // 等待线程启动
        thread.interrupt(); // 中断
        thread.join();
    }

    /**
     * a-test-thread 线程在被中断后，会正常终止
     */
    @Test
    public void testThreadInterrupt() throws InterruptedException {
        Runnable task = new LoopTask();
        Thread thread = new Thread(task, "a-test-thread");
        thread.start();
        Thread.sleep(1000); // 等待线程启动完成
        thread.interrupt(); // 中断
        thread.join();
    }


}

class LoopTask implements Runnable {

    @Override
    public void run() {
        String thread = Thread.currentThread().getName();
        //      while (true) {
        while (!Thread.interrupted()) {
            System.out.println(thread + " is running...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                break; // 线程被中断后，跳出循环
            }
        }
        System.out.println(thread + " was interrupted.");
    }
}

class LoopTask2 implements Runnable {

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }
            // do some business staff...

            if (Thread.interrupted()) {
                break;
            }
            // do some other business staff..
        }
    }
}
