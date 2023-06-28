package com.feng.learn.infrastructure.gray;

import com.feng.learn.infrastructure.gray.annotation.DegradeSwitch;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/12
 */
@Service
@Slf4j
public class ServiceUseDegradeSwitch {

  @DegradeSwitch(degradeTo = "methodADegrade")
  void methodA(int i, String str, Object[] array) {
    log.info("methodA");
  }


  private void methodADegrade(int i, String str, Object[] array) {
    log.info("methodADegrade");
  }

  @DegradeSwitch(degradeTo = "methodBDegrade")
  protected Map<String, List<Integer>> methodB() {
    log.info("methodB");
    return Collections.emptyMap();
  }

  private Map<String, List<Integer>> methodBDegrade() {
    log.info("methodBDegrade");
    return Collections.emptyMap();
  }


}
