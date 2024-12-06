package com.feng.learn.gc;

import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-09-23
 */
@Slf4j
public class MemoryLeak {

    public static void main(String[] args) throws InterruptedException {
        List<int[]> data = new LinkedList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            data.add(new int[1024]);
            Thread.sleep(10);
            int[] ints = new int[1024];
        }
        log.info("data.size => {}", data.size());
    }
}
