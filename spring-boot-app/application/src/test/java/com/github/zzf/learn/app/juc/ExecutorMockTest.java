package com.github.zzf.learn.app.juc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import java.util.concurrent.Executor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-05-25
 */
public class ExecutorMockTest {

    @Mock
    Executor executor;

    @Test
    void givenExecutorMock_whenExecute_thenMocked() {
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(executor).execute(any());
    }
}
