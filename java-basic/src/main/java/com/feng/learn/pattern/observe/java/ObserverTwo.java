package com.feng.learn.pattern.observe.java;

import java.util.Observable;
import java.util.Observer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/15
 */
@Slf4j
public class ObserverTwo implements Observer {

  final ObservableOne observableOne;

  public ObserverTwo(ObservableOne observableOne) {
    this.observableOne = observableOne;
    observableOne.addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    ObservableOne observableOne = (ObservableOne) o;
    int valueAfterUpdate = observableOne.getValue();
    log.info("ObserverTwo receive notify from {}: {}", o, valueAfterUpdate);
  }
}
