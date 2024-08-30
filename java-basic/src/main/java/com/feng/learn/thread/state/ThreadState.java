package com.feng.learn.thread.state;

import java.io.IOException;

public class ThreadState {

    public static void main(String... args) {

        // RUNNABLE
        new Thread(() -> {
            try {
                printThread("wait for read from System.in..");
                int r = System.in.read();
                printThread("read a byte from System.in: " + r);
            } catch (IOException e) {
                printThread(e.toString());
            }
        }, "RUNNABLE").start();

        // TIMED_WAITING
        Object blockedLock = new Object();
        new Thread(() -> {
            synchronized (blockedLock) {
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, "Sleep with lock held").start();
        // BLOCKED
        // entry set
        new Thread(() -> {
            printThread("try to acquire lock");
            synchronized (blockedLock) {
                printThread("lock acquired");
            }
            printThread("lock released");
        }, "BLOCKED").start();

        // WAITING
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    // deal with it
                }
            }
        }, "WAITING").start();

        // TIMED_WAITING
        new Thread(() -> {
            synchronized (lock) {
                try {
                    // 等待60s
                    lock.wait(1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "TIMED_WAITING").start();
    }

    static void printThread(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
