package com.feng.learn.future;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-11-12
 */
@Slf4j
class FutureCasesTest {

    private Object apply(Object o) {
        log.info("code was run in thread: {} -> {}", Thread.currentThread().getName(), o);
        return o;
    }

    private Consumer<Object> printThreadAndArgConsumer = this::apply;
    private Consumer<Object> exception = (o) -> {
        throw new IllegalStateException();
    };
    private Runnable printThreadAndArgRunnable = () -> apply(null);

    final Executor pool1 = Executors.newCachedThreadPool(r -> new Thread(r, "pool1"));
    final Executor pool2 = Executors.newCachedThreadPool(r -> new Thread(r, "pool2"));
    final Executor pool3 = Executors.newCachedThreadPool(r -> new Thread(r, "pool3"));


    /**
     * <pre>
     *     测试 f.then* 默认在什么线程执行
     *     1. f.thenAccept / f.thenRun / f.thenApply 和 f.complete 使用同一个线程
     *     1. f.then*Async 使用指定的线程执行
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenComplete_then() {
        CompletableFuture<String> f = new CompletableFuture<>();
        // f.thenAccept / f.thenRun / f.thenApply 和 f.complete 使用同一个线程
        // f.then*Async 使用指定的线程执行
        f.thenApply(this::apply)
            .thenAccept(this::apply)
            .thenRun(() -> apply(null))
            .thenApplyAsync(this::apply, pool1)
            .thenAcceptAsync(this::apply, pool1)
            .thenRunAsync(() -> apply(null), pool2)
        ;
        f.complete("Hello, CompletableFuture");
        then(f.get()).isEqualTo("Hello, CompletableFuture");
    }

    /**
     * <pre>
     *     测试 f.then* 默认在什么线程执行
     *     1. f.thenAccept / f.thenRun / f.thenApply 和 f.complete 使用同一个线程
     *     1. f.then*Async 使用指定的线程执行
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenCompleteUsingAThread_then() {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.thenApply(this::apply)
            .thenAcceptAsync(this::apply, pool1)
            .thenRunAsync(() -> apply(null), pool2)
        ;
        new Thread(() -> f.complete("Hello, CompletableFuture"), "zhang.zzf").start();
        then(f.get()).isEqualTo("Hello, CompletableFuture");
        // 关注点:
        // complete 后再添加 callback
        // callback 一定是在当前线程执行
        f.thenApply(this::apply);
    }

    /**
     * <pre>
     *     测试 f.exceptionally
     *     1. f.exceptionally 返回一个新的 CompletionStage
     *     1 f.exceptionally().whenComplete() 中的 result 是 exceptionally 中返回的 null
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenCompleteFailed_then() {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.thenApply(this::apply)
            .thenAcceptAsync(this::apply, pool1)
            .thenRunAsync(() -> apply(null), pool2)
            .exceptionally(t -> {
                apply(t);
                return null;
            })
            .whenComplete((result, throwable) -> apply(throwable))
        ;
        f.completeExceptionally(new IllegalStateException());
        then(f.isCompletedExceptionally()).isEqualTo(true);
    }

    /**
     * <pre>
     *     测试 f.handle
     *     1. f.exceptionally 返回一个新的 CompletionStage
     *     1 f.exceptionally().whenComplete() 中的 result 是 exceptionally 中返回的 null
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenHandle_then() {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.thenApply((step) -> {// 不会被执行
                String curStep = step + "->f1";
                log.info("thenApply -> step: {}, curStep: {}", step, curStep);
                return curStep;
            })
            .thenApplyAsync((step) -> {// 不会被执行
                String curStep = step + "->f11";
                log.info("thenApplyAsync -> step: {}, curStep: {}", step, curStep);
                return curStep;
            }, pool1)
            // 把 Exception 转换为默认值， 自动降级模式
            .exceptionally(throwable -> {
                log.info("exceptionally -> step: {}, curStep: {}", null, "exception");
                return "exception";
            })
            .whenComplete((result, throwable) -> {
                log.info("f.exceptionally.whenComplete -> step: {}, curStep: {}", result, throwable);
            })
        ;
        f.whenComplete((o, throwable) -> {
            log.info("f.whenComplete -> step: {}, curStep: {}", o, throwable);
        });
        f.completeExceptionally(new IllegalStateException());
        then(f.isCompletedExceptionally()).isEqualTo(true);
    }

    /**
     * 测试 Stream 与 CompletableFuture 的组合使用
     * <pre>
     *             Integer sum = Stream.of(1, 2, 3)
     *             .map(id -> supplyAsync(() -> sleep.apply(id), pool1))
     *             .map(CompletableFuture::join)
     *             .reduce(0, Integer::sum);
     * </pre>
     * 核心点，若 stream 异步并行执行，最大相应时间小于 6s，若为串行执行，最大执行时间大于 6s 测试结论：算法为串行化执行
     */
    @Test
    void givenStream_whenWithCompletableFuture_then() {
        Function<Integer, Integer> sleep = (Integer id) -> {
            try {
                Thread.sleep(id * 1000);
            } catch (InterruptedException e) {
                // ignore
            }
            return id;
        };
        long start = System.currentTimeMillis();
        Integer sum = Stream.of(1, 2, 3)
            .map(id -> supplyAsync(() -> sleep.apply(id), pool1))
            .map(CompletableFuture::join)
            .reduce(0, Integer::sum);
        then(sum).isEqualTo(6);
        // 核心点，若 stream 异步并行执行，最大相应时间小于 6s，若为串行执行，最大执行时间大于 6s
        // 测试结论：算法为串行化执行
        then(System.currentTimeMillis() - start).isGreaterThanOrEqualTo(6000);
    }

    /**
     * 测试 Stream 与 CompletableFuture 的组合使用
     * <pre>
     *             List<Integer> ids = Stream.of(1, 2, 3)
     *             .map(id -> supplyAsync(() -> sleep.apply(id), pool1))
     *             .reduce(completedFuture(empty()), (cf1, cf2) -> cf1.thenCombine(cf2, Stream::concat))
     *             .join()
     *             .collect(toList());
     * </pre>
     * 核心点，若 stream 异步并行执行，最大相应时间小于 6s，若为串行执行，最大执行时间大于 6s 测试结论：算法为并行化执行
     */
    @Test
    void givenStream_whenWithCompletableFuture2_then() {
        Function<Integer, Stream<Integer>> sleep = (Integer id) -> {
            try {
                Thread.sleep(id * 1000);
            } catch (InterruptedException e) {
                // ignore
            }
            return Stream.of(id);
        };
        long start = System.currentTimeMillis();
        List<Integer> ids = Stream.of(1, 2, 3)
            .map(id -> supplyAsync(() -> sleep.apply(id), pool1))
            .reduce(completedFuture(empty()), (cf1, cf2) -> cf1.thenCombine(cf2, Stream::concat))
            .join()
            .collect(toList());
        then(ids).contains(1, 2, 3);
        // 核心点，若 stream 异步并行执行，最大相应时间小于 6s，若为串行执行，最大执行时间大于 6s
        // 测试结论：算法为并行化执行
        then(System.currentTimeMillis() - start).isLessThan(4000);
    }


    /**
     * <pre>
     *     测试 f.thenApply / f.thenAccept / f.thenRun
     *     f.thenApply 是压栈操作，后进先出
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0, curStep: f0->f3
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0, curStep: f0->f2
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0->f2, curStep: f0->f2->f21
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0, curStep: f0->f1
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenMultiplyApply_then() {
        // given
        String stepF0 = "f0";
        CompletableFuture<String> f0 = new CompletableFuture<>();
        CompletableFuture<String> f1 = f0.thenApply((step) -> {
            String curStep = step + "->f1";
            log.info("step: {}, curStep: {}", step, curStep);
            return curStep;
        });
        CompletableFuture<String> f2 = f0.thenApply((step) -> {
            String curStep = step + "->f2";
            log.info("step: {}, curStep: {}", step, curStep);
            return curStep;
        });
        CompletableFuture<String> f21 = f2.thenApply((step) -> {
            String curStep = step + "->f21";
            log.info("step: {}, curStep: {}", step, curStep);
            return curStep;
        });
        CompletableFuture<String> f3 = f0.thenApply((step) -> {
            String curStep = step + "->f3";
            log.info("step: {}, curStep: {}", step, curStep);
            return curStep;
        });
        // when
        f0.complete(stepF0);
        // then
        then(f0.get()).isEqualTo(stepF0);
        then(f1.get()).isEqualTo(stepF0 + "->f1");
        then(f21.get()).isEqualTo(stepF0 + "->f2" + "->f21");
    }

    /**
     * <pre>
     *     测试 f.thenApply / f.thenAccept / f.thenRun
     *     f.thenApply 是压栈操作，后进先出
     *     f1 / f2 / f3 是3个链路，f1/f3 的异常不影响 f2
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0, curStep: f0->f3
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0, curStep: f0->f2
     *     [main] INFO  c.feng.learn.future.FutureCasesTest - step: f0->f2, curStep: f0->f2->f21
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenException_then() {
        // given
        String stepF0 = "f0";
        CompletableFuture<String> f0 = new CompletableFuture<>();
        CompletableFuture<String> f1 = f0.thenApply((step) -> {
            String curStep = step + "->f1";
            log.info("step: {}, curStep: {}", step, curStep);
            throw new IllegalArgumentException();
            // return curStep;
        });
        CompletableFuture<String> f2 = f0.thenApply((step) -> {
            String curStep = step + "->f2";
            log.info("step: {}, curStep: {}", step, curStep);
            return curStep;
        });
        CompletableFuture<String> f21 = f2.thenApply((step) -> {
            String curStep = step + "->f21";
            log.info("step: {}, curStep: {}", step, curStep);
            return curStep;
        });
        CompletableFuture<String> f3 = f0.thenApply((step) -> {
            String curStep = step + "->f3";
            log.info("step: {}, curStep: {}", step, curStep);
            throw new IllegalArgumentException();
            // return curStep;
        });
        // when
        f0.complete(stepF0);
        // then
        then(f0.get()).isEqualTo(stepF0);
        then(f21.get()).isEqualTo(stepF0 + "->f2" + "->f21");
        then(catchThrowable(f1::get)).isNotNull()
            .isInstanceOf(ExecutionException.class);
    }

    /**
     * <pre>
     *      async get idList
     *      1. async get nameList by idList
     *      2. async get addressList by idList
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenAsync2_then() {
        long startTime = System.currentTimeMillis();
        CompletableFuture<List<Long>> getIdStage = CompletableFuture.supplyAsync(() -> {
            trySleep(1000);
            List<Long> idList = new ArrayList<>();
            log.info("getIdStage response -> {}", idList);
            return idList;
        }, pool1);
        CompletableFuture<List<String>> nameStage = getIdStage.thenApplyAsync((idList) -> {
            log.info("thenApplyAsync.queryNameByIdList request -> {}", idList);
            trySleep(1000);
            return (List<String>) new ArrayList<String>();
        }, pool2);
        CompletableFuture<List<String>> addressStage = getIdStage.thenApplyAsync((idList) -> {
            log.info("thenApplyAsync.queryAddressByIdList request -> {}", idList);
            trySleep(1000);
            return (List<String>) new ArrayList<String>();
        }, pool3);
        //
        getIdStage.thenApply((idList) -> {
            log.info("thenApply.queryNameByIdList request -> {}", idList);
            trySleep(1000);
            return (List<String>) new ArrayList<String>();
        });
        //
        List<String> nameList = nameStage.get();
        List<String> addressList = addressStage.get();
        then(System.currentTimeMillis() - startTime).isGreaterThan(3000);
    }

    /**
     * <pre>
     *      async get idList
     *      1. async get nameList by idList
     *      2. async get addressList by idList
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCompletableFuture_whenAsync_then() {
        long startTime = System.currentTimeMillis();
        CompletableFuture<List<Long>> getIdStage = CompletableFuture.supplyAsync(() -> {
            trySleep(1000);
            List<Long> idList = new ArrayList<>();
            idList.add(1L);
            idList.add(2L);
            log.info("getIdStage response -> {}", idList);
            return idList;
        }, pool1);
        CompletableFuture<List<String>> nameStage = getIdStage.thenApplyAsync((idList) -> {
            log.info("queryNameByIdList request -> {}", idList);
            trySleep(1000);
            List<String> nameList = new ArrayList<>();
            nameList.add("n1");
            nameList.add("n2");
            log.info("queryNameByIdList response -> {}", nameList);
            return nameList;
        }, pool2);
        CompletableFuture<List<String>> addressStage = getIdStage.thenApplyAsync((idList) -> {
            log.info("queryAddressByIdList request -> {}", idList);
            trySleep(1000);
            List<String> addressList = new ArrayList<>();
            addressList.add("a1");
            addressList.add("a2");
            log.info("queryAddressByIdList request -> {}", idList);
            return addressList;
        }, pool3);
        //
        List<String> nameList = nameStage.get();
        List<String> addressList = addressStage.get();
        then(System.currentTimeMillis() - startTime).isLessThan(3000);
    }

    private static void trySleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
        }
    }

}