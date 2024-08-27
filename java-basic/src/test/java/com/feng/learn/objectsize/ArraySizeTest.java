package com.feng.learn.objectsize;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * 测量数组对象头的大小
 * <p>测试环境jdk1.8</p>
 * <p>测试1: new 100W个 只有一个8个长度的int[]，然后观察heap</p>
 * <p>测试2：new 100W个 只有一个8个长度的long[]，然后观察heap</p>
 * <p>得出结论: 每个数组对象头是20byte + 4byte内存填充 + 实际占用内存</p>
 */
public class ArraySizeTest {

    /**
     * 分析heap100W = 56000000byte。
     * <p>一个数组对象占用56byte
     * <p>对象头和对齐填充56-32=24byte</p>
     * <p>对象头 20bytes = 8(类引用) + 8(头) + 4(数组大小)</p>
     * <p>对象8字节对齐填充 = 4byte</p>
     */
    @Test
    public void testIntArraySize() throws InterruptedException {
        int size = 1000000;
        List<int[]> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new int[8]); //拥有8个int的数组，每个int占用4个byte字节，共32byte
        }
        Thread.currentThread().join();
    }

    /**
     * 分析heap100W = 88000000byte。
     * <p>一个数组对象占用88byte
     * <p>对象头和对齐填充88-64=24byte</p>
     * <p>对象头 20bytes = 8(类引用) + 8(头) + 4(数组大小)</p>
     * <p>对象8字节对齐填充 = 4byte</p>
     */
    @Test
    public void testLongArraySize() throws InterruptedException {
        int size = 1000000;
        List<long[]> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tmp.add(new long[8]); //拥有8个long的数组，每个long占用8个byte字节，共64byte
        }
        Thread.currentThread().join();
    }

}
