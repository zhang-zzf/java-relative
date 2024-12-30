package com.github.learn.springframework.value;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-30
 */
@Slf4j
@Getter
@Service
public class ConfigService {

    @Value("aConstantStr") // 直接注入字符串好像没有什么用途
    String aConstantStr;
    //
    String aConstantStr2 = "aConstantStr2";

    /**
     * <pre>
     *      从 Environment 中获取 `value.from.config` 配置
     *      1. 无 `:` 默认值时，配置项必须存在，否则启动报错
     *      1. 有 `:` 默认值时，配置不存在使用默认值
     *          1. `${config:}` 默认值为空字符串。
     * </pre>
     */
    // @Value("${value.from.config}")
    @Value("${value.from.config:}")
    String valueFromConfig;
    @Value("${value.from.config2:a b c}")
    String valueFromConfig2;

    /**
     * 数组
     */
    @Value("${value.from.config3:a,b,c d}")
    String[] valueFromConfigArr;

    /**
     * 列表
     */
    @Value("${value.from.config4:a,b,c d}")
    List<String> valueFromConfigArr2;

    /**
     * 列表 使用 spEL 解析
     */
    @Value("#{'${value.from.config4:a,b,c d}'.split(',')}")
    List<String> valueFromConfigArr21;

    /**
     * 列表 使用 spEL 解析，可动态编码
     * <p>按 行 切分</p>
     */
    @Value("#{'${value.from.config4:a\nb\nc d}'.split('\\n')}")
    List<String> valueFromConfigArr22;

    /**
     * 列表  -> 1, null, null
     */
    @Value("${value.from.config5:1,,}")
    List<Long> valueFromConfigArr3;

    /**
     * Set  -> 1, 2
     */
    @Value("${value.from.config5:1,1,2}")
    Set<Long> valueFromConfigArr4;

    @Value("${value.from.config5:[{\"name\":\"zhang.zzf\",\"age\":5,\"adult\":false}]}")
    String jsonString;

    @Value("classpath*:i18n/*.properties")
    // 这里必须使用数组接受，List 会接受失败
    Resource[] exceptionBundleList;

    @Value("#{${map.from.config:{'5':5}}}")
    Map<Long, Long> mapValue;

}
