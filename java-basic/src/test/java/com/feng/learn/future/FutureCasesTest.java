package com.feng.learn.future;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

  Executor pool1 = Executors.newCachedThreadPool(r -> new Thread(r, "pool1"));
  Executor pool2 = Executors.newCachedThreadPool(r -> new Thread(r, "pool2"));


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
    CompletableFuture<String> f = new CompletableFuture();
    // f.thenAccept / f.thenRun / f.thenApply 和 f.complete 使用同一个线程
    // f.then*Async 使用指定的线程执行
    f.thenApply(o -> apply(o))
        .thenAccept(o -> apply(o))
        .thenRun(() -> apply(null))
        .thenApplyAsync(o -> apply(o), pool1)
        .thenAcceptAsync(o -> apply(o), pool1)
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
    CompletableFuture<String> f = new CompletableFuture();
    f.thenApply(o -> apply(o))
        .thenAcceptAsync(o -> apply(o), pool1)
        .thenRunAsync(() -> apply(null), pool2)
    ;
    new Thread(() -> {
      f.complete("Hello, CompletableFuture");
    }, "zhang.zzf").start();
    then(f.get()).isEqualTo("Hello, CompletableFuture");
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
    CompletableFuture<String> f = new CompletableFuture();
    f.thenApply(o -> apply(o))
        .thenAcceptAsync(o -> apply(o), pool1)
        .thenRunAsync(() -> apply(null), pool2)
        .exceptionally(t -> {
          apply(t);
          return null;
        })
        .whenComplete((result, throwable) -> {
          apply(throwable);
        })
    ;
    f.completeExceptionally(new IllegalStateException());
    then(f.isCompletedExceptionally()).isEqualTo(true);
  }

}