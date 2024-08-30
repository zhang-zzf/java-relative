package com.feng.learn.thread.split;

/**
 * 乐观锁/cas算法
 *
 * @author zhangzhanfeng
 * @date Dec 10, 2017
 */
public class LocationV3 {

    private XY xy_; // 有this对象加锁保护

    public LocationV3(double x, double y) {
        xy_ = new XY(x, y);
    }

    public synchronized XY xy() {
        return xy_;
    }

    /**
     * 必须加锁保证原子操作
     *
     * @Author zhangzhanfeng
     */
    private synchronized boolean compareAndSet(XY oldValue, XY newValue) {
        boolean success = oldValue == xy_;
        if (success) {
            xy_ = newValue;
        }
        return success;
    }

    /**
     * 减小锁的粒度，把创建对象放到了锁的外面。
     *
     * @Author zhangzhanfeng
     */
    public void moveBy(double dx, double dy) {
        while (!Thread.interrupted()) {
            XY oldValue = xy(); // 这里有锁保护
            XY newValue = new XY(oldValue.x() + dx, oldValue.y() + dx);    // 这里无锁
            if (compareAndSet(oldValue, newValue)) { // 这里有锁保护
                break;
            }
            Thread.yield();
        }
    }
}
