package com.feng.learn.thread.Volatile;

import java.io.Closeable;
import lombok.Data;

@Data
public class VolatileNotStatusFlag2 implements Closeable {

    public boolean shutdown = false;

    @Override
    public void close() {
        shutdown = true;
    }

    public void doWork() {
        while (!shutdown) {
            // do stuff
        }
        System.out.println(Thread.currentThread().getName() + " shutdown now...");
    }

}
