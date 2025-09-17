package com.github.zzf.dd.repo.mysql.iot_card.mapper;

import static org.assertj.core.api.Java6BDDAssertions.then;

import com.github.zzf.dd.repo.mysql.iot_card.entity.Events;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-09-11
 *  * JVM Asia/Shanghai
 *  * serverTimeZone Asia/Shanghai
 *  * MySQL Asia/Shanghai
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class EventsMapperTest {

    @Autowired
    EventsMapper eventsMapper;

    @Test
    public void givenMySQL_whenInsertAndSelect_then() {
        Events e1 = new Events();
        Date now = new Date();
        e1.setDsLocalTime(now);
        e1.setTz("Asia/Shanghai");
        e1.setDsUtcTime(now);
        e1.setTsUtcTime(now);
        int row = eventsMapper.insert(e1);
        Events events = eventsMapper.selectLatest();
        then(events).isNotNull();
    }
}