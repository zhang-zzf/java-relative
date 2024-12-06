package com.feng.learn.infrastructure.gray;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/12
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    SwitchAop.class,
    ServiceUseDegradeSwitch.class,
})
public class DegradeSwitchTest {

    @Autowired
    ServiceUseDegradeSwitch serviceUseDegradeSwitch;

    /**
     * 打断点， 看流程
     */
    @Test
    public void testDegradeSwitch() {
        serviceUseDegradeSwitch.methodA(0, "", null);
        Map<String, List<Integer>> stringListMap = serviceUseDegradeSwitch.methodB();
    }

}
