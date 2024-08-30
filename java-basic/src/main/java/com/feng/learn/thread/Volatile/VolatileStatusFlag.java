package com.feng.learn.thread.Volatile;

import java.io.Closeable;

public class VolatileStatusFlag implements Closeable {

    private volatile boolean shutdown = false;

    @Override
    public void close() {
        shutdown = true;
    }

    public void doWork() {
        while (!shutdown) {
            // do stuff
        }
        System.out.println("shutdown now...");
    }


    public static void main(String[] args) throws InterruptedException {
        VolatileStatusFlag statusFlag = new VolatileStatusFlag();

        new Thread(statusFlag::doWork, "work-thread").start();
        Thread.sleep(1000);

        new Thread(statusFlag::close, "close-thread").start();

        Thread.sleep(10000);
    }
}
