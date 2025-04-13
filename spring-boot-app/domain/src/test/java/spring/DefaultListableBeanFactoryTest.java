package spring;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-04-13
 */
@ExtendWith(MockitoExtension.class)
public class DefaultListableBeanFactoryTest {

    @Test
    void givenDLBF_whenRegistryBeanDefinition_thenSuccess() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        // 创建一个BeanDefinition
        RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
        String beanName = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";
        // 注册到DLBF
        dlbf.registerBeanDefinition(beanName, def);
        BeanDefinition bd = dlbf.getBeanDefinition(beanName);
        then(bd).isNotNull();
    }

    @Test
    void givenDLBF_whenCreateBean_thenSuccess() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
        String beanName = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";
        dlbf.registerBeanDefinition(beanName, def);
        BeanDefinition bd = dlbf.getBeanDefinition(beanName);
        then(bd).isNotNull();
        // first getBean: create new instance
        Object bean = dlbf.getBean(beanName);
        then(bean).isNotNull();
        // second getBean: return the created instance
        then(dlbf.getBean(beanName)).isSameAs(bean);
    }


}
