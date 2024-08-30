package com.feng.learn.thread.split;

import com.feng.learn.annotation.ThreadSafe;

/**
 * 悲观锁
 *
 * @author zhangzhanfeng
 * @date Dec 10, 2017
 */
@ThreadSafe(authors = {"zhanfeng.zhang"})
public class LocationV2 {

    private XY xy_;

    public LocationV2(double x, double y) {
        xy_ = new XY(x, y);
    }

    public synchronized XY xy() {
        return xy_;
    }

    public synchronized void movedBy(double dx, double dy) {
        XY oldXy = xy();
        xy_ = new XY(oldXy.x() + dx, oldXy.y() + dy);
    }

}
