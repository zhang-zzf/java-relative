package com.feng.learn.objectsize;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 */
public class ObjectSizeTest {

    /**
     * 分析heap100 W 对象 = 1600 W byte。
     * <p>一个对象占用16byte
     * <p>对象头和对齐填充 = 16byte</p>
     * <p>对象头 16bytes = 8(类引用) + 8(头)</p>
     * <p>对象8字节对齐填充 = 0byte</p>
     */
    @Test
    public void testObject() throws InterruptedException {
        int size = 1000000;
        List<Object> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new Object()); //Object 对象内无任何实例属性，所以只有对象头和用于对齐的内存
        }
        Thread.currentThread().join();
    }

    /**
     * 分析heap100 W 对象 = 2000 W byte。
     * <p>一个对象占用20byte
     * <p>对象头和对齐填充 = 16 (20-4)</p>
     * <p>对象头 16bytes = 8(类引用) + 8(头)</p>
     * <p>对象8字节对齐填充 = 0byte</p>
     */
    @Test
    public void testIntegerSize() throws InterruptedException {
        int size = 1000000;
        List<Object> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new Integer(i)); //Integer 对象内只有一个int实例属性，占用4byte
        }
        Thread.currentThread().join();
    }


    /**
     * 分析heap100 W 对象 = 1700 W byte。
     * <p>一个对象占用17byte
     * <p>对象头和对齐填充 = 16 (17-1)</p>
     * <p>对象头 16bytes = 8(类引用) + 8(头)</p>
     * <p>对象8字节对齐填充 = 0byte</p>
     */
    @Test
    public void testByteSize() throws InterruptedException {
        int size = 1000000;
        List<Object> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new Byte((byte) i)); //Byte 对象内只有一个byte实例属性，占用1byte
        }
        Thread.currentThread().join();
    }


    /**
     * 分析heap100 W 对象 = 2400 W byte。
     * <p>一个对象占用24byte
     * <p>对象头和对齐填充 = 16 (24-8)</p>
     * <p>对象头 16bytes = 8(类引用) + 8(头)</p>
     * <p>对象8字节对齐填充 = 0byte</p>
     */
    @Test
    public void testLongSize() throws InterruptedException {
        int size = 1000000;
        List<Object> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new Long(i)); //Long 对象内只有一个long实例属性，占用8byte
        }
        Thread.currentThread().join();
    }


    /**
     * 分析heap100 W 对象 = 2800 W byte。
     * <p>一个对象占用28byte
     * <p>对象头和对齐填充 = 16 (28-12)</p>
     * <p>对象头 16bytes = 8(类引用) + 8(头)</p>
     * <p>对象8字节对齐填充 = 0byte</p>
     */
    @Test
    public void testStringSize() throws InterruptedException {
        int size = 1000000;
        List<Object> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(""); //String 对象内有int/char[]实例属性，占用12byte
        }
        Thread.currentThread().join();
    }

    /**
     * 分析heap100 W 对象 = 4000 W byte。
     * <p>一个对象占用40byte
     * <p>对象头和对齐填充 = 16 (40-24)</p>
     * <p>对象头 16bytes = 8(类引用) + 8(头)</p>
     * <p>对象8字节对齐填充 = 0byte</p>
     */
    @Test
    public void testEntrySize() throws InterruptedException {
        int size = 1000000;
        List<Object> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new Entry()); //Entry 24byte
        }
        Thread.currentThread().join();
    }


    // 4 + 2 + 8 + 8 + 2 = 24byte
    static class Entry {

        int i;
        short s;
        long l;
        Object o;
        char c;
    }

}
