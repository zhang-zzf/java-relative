package com.github.learn.lock.annotation;

import java.lang.annotation.*;

/**
 * The class to which this annotation is applied is thread-safe. This means that
 * no sequences of accesses (reads and writes to public fields, calls to public
 * methods) may put the object into an invalid state, regardless of the
 * interleaving of those actions by the runtime, and without requiring any
 * additional synchronization or coordination on the part of the caller.
 *
 * @author zhanfeng.zhang
 * @date 2021/08/28
 * @see NotThreadSafe
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafe {

}
