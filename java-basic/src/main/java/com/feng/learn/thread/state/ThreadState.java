package com.feng.learn.thread.state;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadState {

    public static void main(String... args) {

        /**
         * "RUNNABLE" #17 prio=5 os_prio=31 cpu=0.75ms elapsed=12.35s tid=0x00007fc62f024800 nid=0x6703 runnable  [0x000070000f39e000]
         *    java.lang.Thread.State: RUNNABLE
         * 	at java.io.FileInputStream.readBytes(java.base@17.0.4/Native Method)
         * 	at java.io.FileInputStream.read(java.base@17.0.4/FileInputStream.java:276)
         * 	at java.io.BufferedInputStream.fill(java.base@17.0.4/BufferedInputStream.java:244)
         * 	at java.io.BufferedInputStream.read(java.base@17.0.4/BufferedInputStream.java:263)
         * 	- locked <0x000000043f808e08> (a java.io.BufferedInputStream)
         * 	at com.feng.learn.thread.state.ThreadState.lambda$main$0(ThreadState.java:15)
         * 	at com.feng.learn.thread.state.ThreadState$$Lambda$14/0x0000000800c00a08.run(Unknown Source)
         * 	at java.lang.Thread.run(java.base@17.0.4/Thread.java:833)
         */
        // RUNNABLE
        // 等待 in 输入的过程中，线程一直都是 RUNNABLE 状态
        Thread runnableThread = new Thread(() -> {
            try {
                printThread("wait for read from System.in..");
                int r = System.in.read();
                printThread("read a byte from System.in: " + r);
            } catch (IOException e) {
                printThread(e.toString());
            }
        }, "RUNNABLE");
        runnableThread.start();

        /**
         * "Sleep with lock held" #18 prio=5 os_prio=31 cpu=0.24ms elapsed=12.35s tid=0x00007fc5ef02aa00 nid=0x6903 waiting on condition  [0x000070000f4a1000]
         *    java.lang.Thread.State: TIMED_WAITING (sleeping)
         * 	at java.lang.Thread.sleep(java.base@17.0.4/Native Method)
         * 	at com.feng.learn.thread.state.ThreadState.lambda$main$1(ThreadState.java:29)
         * 	- locked <0x000000043fc3d770> (a java.lang.Object)
         * 	at com.feng.learn.thread.state.ThreadState$$Lambda$15/0x0000000800c00c28.run(Unknown Source)
         * 	at java.lang.Thread.run(java.base@17.0.4/Thread.java:833)
         */
        // TIMED_WAITING
        Object blockedLock = new Object();
        new Thread(() -> {
            printThread("try to acquire lock: blockedLock");
            synchronized (blockedLock) {
                printThread("lock acquired: blockedLock");
                try {
                    Thread.sleep(1000 * 60);
                    printThread("Thread.sleep end");
                } catch (InterruptedException e) {
                    //
                }
            }
            printThread("lock released: blockedLock");
        }, "Sleep with lock held").start();

        /**
         * "BLOCKED" #19 prio=5 os_prio=31 cpu=0.52ms elapsed=12.35s tid=0x00007fc63180be00 nid=0x6b03 waiting for monitor entry  [0x000070000f5a4000]
         *    java.lang.Thread.State: BLOCKED (on object monitor)
         * 	at com.feng.learn.thread.state.ThreadState.lambda$main$2(ThreadState.java:42)
         * 	- waiting to lock <0x000000043fc3d770> (a java.lang.Object)
         * 	at com.feng.learn.thread.state.ThreadState$$Lambda$16/0x0000000800c01800.run(Unknown Source)
         * 	at java.lang.Thread.run(java.base@17.0.4/Thread.java:833)
         */
        // BLOCKED
        // entry set
        new Thread(() -> {
            printThread("try to acquire lock: blockedLock");
            synchronized (blockedLock) {
                printThread("lock acquired: blockedLock");
            }
            printThread("lock released: blockedLock");
        }, "BLOCKED").start();

        /**
         * "WAITING" #20 prio=5 os_prio=31 cpu=0.26ms elapsed=12.35s tid=0x00007fc630011400 nid=0x6d03 in Object.wait()  [0x000070000f6a7000]
         *    java.lang.Thread.State: WAITING (on object monitor)
         * 	at java.lang.Object.wait(java.base@17.0.4/Native Method)
         * 	- waiting on <0x000000043fc45dc0> (a java.lang.Object)
         * 	at java.lang.Object.wait(java.base@17.0.4/Object.java:338)
         * 	at com.feng.learn.thread.state.ThreadState.lambda$main$3(ThreadState.java:57)
         * 	- locked <0x000000043fc45dc0> (a java.lang.Object)
         * 	at com.feng.learn.thread.state.ThreadState$$Lambda$17/0x0000000800c01a28.run(Unknown Source)
         * 	at java.lang.Thread.run(java.base@17.0.4/Thread.java:833)
         */
        // WAITING
        Object lock = new Object();
        AtomicBoolean waitResourceReady =  new AtomicBoolean(false);
        new Thread(() -> {
            printThread("try to acquire lock: lock");
            synchronized (lock) {
                printThread("lock acquired: lock");
                while (!waitResourceReady.get()) {
                    try {
                        printThread("wait start: lock");
                        lock.wait();
                        printThread("wait end: lock");
                    } catch (InterruptedException e) {
                        // deal with it
                    }
                }
            }
            printThread("lock released: lock");
        }, "WAITING").start();

        /**
         * "TIMED_WAITING" #21 prio=5 os_prio=31 cpu=0.26ms elapsed=12.34s tid=0x00007fc5ef02b000 nid=0x8003 in Object.wait()  [0x000070000f7aa000]
         *    java.lang.Thread.State: TIMED_WAITING (on object monitor)
         * 	at java.lang.Object.wait(java.base@17.0.4/Native Method)
         * 	- waiting on <0x000000043fc45dc0> (a java.lang.Object)
         * 	at com.feng.learn.thread.state.ThreadState.lambda$main$4(ThreadState.java:76)
         * 	- locked <0x000000043fc45dc0> (a java.lang.Object)
         * 	at com.feng.learn.thread.state.ThreadState$$Lambda$18/0x0000000800c01c50.run(Unknown Source)
         * 	at java.lang.Thread.run(java.base@17.0.4/Thread.java:833)
         */
        // TIMED_WAITING
        new Thread(() -> {
            printThread("try to acquire lock: lock");
            synchronized (lock) {
                printThread("lock acquired: lock");
                while (!waitResourceReady.get()) {
                    try {
                        printThread("wait start: lock");
                        // 等待60s
                        lock.wait(1000 * 25);
                        printThread("wait end: lock");
                    } catch (InterruptedException e) {
                        // deal with it
                    }
                }
            }
            printThread("lock released: lock");
        }, "TIMED_WAITING").start();

        new Thread(() -> {
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                // ignore
            }
            if (waitResourceReady.compareAndSet(false, true)){
                printThread("resource ready");
                printThread("try to acquire lock: lock");
                synchronized (lock) {
                    printThread("lock acquired: lock");
                    lock.notifyAll();
                    printThread("notify: lock");
                }
            }
            runnableThread.interrupt();
        }, "notify-thread").start();
    }

    static void printThread(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
