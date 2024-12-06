package com.feng.learn.pattern.observe.java;

import java.util.Observable;
import lombok.Data;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/15
 */
@Data
public class ObservableOne extends Observable {

    private int value;

    public void setValue(int newValue) {
        this.value = newValue;
        setChanged();
        notifyObservers(newValue);
    }

}
