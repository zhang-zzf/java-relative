package com.feng.learn.pattern.decorator;

import com.feng.learn.pattern.decorator.beverage.Beverage;
import com.feng.learn.pattern.decorator.beverage.Decaf;
import com.feng.learn.pattern.decorator.condiment.Milk;
import com.feng.learn.pattern.decorator.condiment.Mocha;
import com.feng.learn.pattern.decorator.condiment.Whip;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DecoratorTest {

    /**
     * 透明装饰模式.
     * <p>客户端完全针对抽象类编程.</p>
     */
    @Test
    public void test() {
        Beverage b = new Decaf();
        b = new Milk(b); // 加牛奶
        b = new Mocha(new Mocha(b)); // 双倍mocha
        b = new Whip(b);
        log.info("Beverage: {}, details: {}", b.cost(), b.getDescription(),
            b.cost());
    }

    @Test
    public void testSemiTransparentDecorator() throws FileNotFoundException {
        URL cacheFile = ClassLoader.getSystemResource("tmp/cache.txt");
        OutputStream os = new FileOutputStream(cacheFile.getFile());
        os = new BufferedOutputStream(os); // 透明装饰者模式

        // 半透明装饰者模式
        PrintStream ps = new PrintStream(os);
        ps.println(10); // 使用了Decorator 中新增的方法（OutputStream中无此方法)
    }

}
