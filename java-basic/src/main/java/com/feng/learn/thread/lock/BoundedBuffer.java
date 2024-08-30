package com.feng.learn.thread.lock;

import com.feng.learn.annotation.ThreadSafe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe(authors = {"zhanfeng.zhang"})
public class BoundedBuffer<E> {

    private final E[] items;
    private int head, tail;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    public BoundedBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        items = (E[]) new Object[capacity];
    }

    public final void put(E e) throws InterruptedException {
        lock.lock();
        try {
            if (isEmpty()) {
                notEmpty.signal();
            }
            while (isFull()) { // 这里必须用while，不能用if
                notFull.await();
            }
            doAddToTail(e);
        } finally {
            lock.unlock();
        }
    }

    public final E take() throws InterruptedException {
        lock.lock();
        try {
            if (isFull()) {
                notFull.signal();
            }
            while (isEmpty()) { // 这里必须用while，不能用if
                notEmpty.await();
            }
            return doDeleteFromHead();
        } finally {
            lock.unlock();
        }

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
        lock.lock();
        try {
            int size = tail - head;
            if (size == 0 && items[head] != null) {
                size = items.length;
            }
            return size;
        } finally {
            lock.unlock();
        }
    }

}
