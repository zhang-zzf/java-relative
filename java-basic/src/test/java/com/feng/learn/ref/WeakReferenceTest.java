package com.feng.learn.ref;


import static org.assertj.core.api.BDDAssertions.then;

import java.lang.ref.WeakReference;
import org.junit.jupiter.api.Test;

/**
 * <pre>
 *   结论:
 *   1. 若一个对象只有弱引用（可以有多个弱引用）指向它，在 GC 时会被回收
 *   1. 若一个对象有弱引用和强引用指向它，在 GC 时不会被回收
 * </pre>
 *
 */
public class WeakReferenceTest {

  @Test
  void givenWeak_when_then() throws InterruptedException {
    AObject obj = new AObject();
    WeakReference<AObject> ref = new WeakReference<>(obj);
    then(ref.get()).isSameAs(obj);
    obj = null;
    // obj 弱引用，被回收
    System.gc();
    //
    Thread.sleep(1000);
    then(ref.get()).isNull();
  }

  @Test
  void given2Weak_when_then() throws InterruptedException {
    AObject obj = new AObject();
    WeakReference<AObject> ref = new WeakReference<>(obj);
    WeakReference<AObject> ref2 = new WeakReference<>(obj);
    then(ref.get()).isSameAs(obj);
    then(ref2.get()).isSameAs(obj);
    obj = null;
    // obj 弱引用，被回收
    System.gc();
    //
    Thread.sleep(1000);
    then(ref.get()).isNull();
    then(ref2.get()).isNull();
  }


  @Test
  void givenWeakAndStrong_when_then() throws InterruptedException {
    AObject obj = new AObject();
    WeakReference<AObject> ref = new WeakReference<>(obj);
    then(ref.get()).isSameAs(obj);
    // obj 有强引用和弱引用，不会被回收
    System.gc();
    //
    Thread.sleep(1000);
    then(ref.get()).isSameAs(obj);
  }


}
