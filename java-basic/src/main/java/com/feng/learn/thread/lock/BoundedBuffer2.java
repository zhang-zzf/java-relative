package com.feng.learn.thread.lock;

import com.feng.learn.annotation.ThreadSafe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe(authors = {"zhanfeng.zhang"})
public class BoundedBuffer2<E> {

    private E[] items;
    private int head, tail;

    private Lock lock = new ReentrantLock();
    Condition notEmpty = lock.newCondition();
    Condition notFull = lock.newCondition();

    public BoundedBuffer2(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        items = (E[]) new Object[capacity];
    }

    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            while (isFull()) { // 这里必须用while，不能用if
                notFull.await();
            }
            doAddToTail(e);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        E ret;
        lock.lock();
        try {
            while (isEmpty()) { // 这里必须用while，不能用if
                notEmpty.await();
            }
            ret = doDeleteFromHead();
            notFull.signal();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    private E doDeleteFromHead() {
        E ret = items[head];
        items[head] = null;
        head = (head + 1) % items.length;
        return ret;
    }

    private void doAddToTail(E e) {
        items[tail] = e;
        tail = (tail + 1) % items.length;
    }

    private boolean isEmpty() {
        return head == tail && items[head] == null;
    }

    private boolean isFull() {
        return head == tail && items[head] != null;
    }

    public int size() {
        int size = tail - head;
        if (size == 0 && items[head] != null) {
            size = items.length;
        }
        return size;
    }

}
