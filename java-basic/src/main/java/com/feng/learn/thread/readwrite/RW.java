package com.feng.learn.thread.readwrite;

public abstract class RW {

    private int waitingReader;
    private int activeReader;

    private int waitingWriter;
    private int activeWriter;

    public void read() throws InterruptedException {
        preRead();
        try {
            doRead();
        } finally {
            postRead();
        }
    }

    public void write() throws InterruptedException {
        preWrite();
        try {
            doWrite();
        } finally {
            postWrite();
        }
    }

    protected synchronized void preRead() throws InterruptedException {
        waitingReader++;
        while (!canRead()) {
            wait();
        }
        waitingReader--;
        activeReader++;
    }

    protected synchronized void postRead() {
        activeReader--;
        notifyAll();
    }

    protected abstract void doRead();

    protected synchronized void preWrite() throws InterruptedException {
        waitingWriter++;
        while (!canWrite()) {
            wait();
        }
        waitingWriter--;
        activeWriter++;
    }

    protected abstract void doWrite();

    protected synchronized void postWrite() {
        activeWriter--;
        notifyAll();
    }

    private boolean canRead() {
        return waitingWriter == 0 && activeWriter == 0;
    }

    private boolean canWrite() {
        return activeReader == 0 && activeWriter == 0;
    }
}
