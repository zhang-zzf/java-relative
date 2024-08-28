package com.feng.learn.resourceloader;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>资源在项目根目录下：tmp/cache.txt</p>
 * <p>直接使用ClassLoader的静态方法或实例方法来加载资源时，是不能以'/'开头的。直接只用 'tmp/cache.txt' 作为资源名字
 * <p>使用java.lang.Class#getResource(java.lang.String)加载资源时，
 * 可以使用'/tmp/cache.txt'表示从项目根目录下加载资源；使用'./JustForTest.class'表示以类的目录作为相对目录来加载资源
 * </p>
 */
@Slf4j
public class ResourceLoaderTest {

    /**
     * java.lang.ClassLoader#getResource(java.lang.String)
     * java.lang.ClassLoader#getResources(java.lang.String)
     * <p>getResource 和 getResources 区别：</p>
     * <p>getResource: 双亲委托父加载器加载，父加载器加载成功返回；加载失败自己加载，只会有一个结果</p>
     * <p>getResources: 同时返回双亲委托父加载器加载结果和自己加载的结果</p>
     */
    @Test
    public void testLoadResourceUseClassLoadInstanceMethod() throws IOException {
        // classLoader默认为sun.misc.Launcher.AppClassLoader
        ClassLoader classLoader = this.getClass().getClassLoader();

        URL resource = classLoader.getResource("");
        log.info("root path: {}", resource);

        InputStream resourceAsStream = classLoader.getResourceAsStream("tmp/cache.txt");
        URL cacheTxt = classLoader.getResource("tmp/cache.txt");
        log.info("root path inputStream: {}", cacheTxt);
        BDDAssertions.then(cacheTxt).isNotNull();

        URL cacheNotExists = classLoader.getResource("tmp/cache2.txt");
        log.info("root path inputStream: {}", cacheNotExists);
        BDDAssertions.then(cacheNotExists).isNull();

        Enumeration<URL> resources = classLoader.getResources("");
        log.info("root paths: {}", resource);
    }


    @Test
    public void testLoadResourceUseClassLoaderStaticMethod() {

        // 1. 默认使用sun.misc.Launcher.AppClassLoader 类加载器来加载资源 = classLoader.getResource(java.lang.String)
        // 2. 若不存在AppClassLoader加载器，借用JVM Bootstrap 来加载资源
        URL systemResource = ClassLoader.getSystemResource("");
        log.info("root path: {}", systemResource);

        // tmp 资源需要存在，否者方法返回null
        URL tmp = ClassLoader.getSystemResource("tmp/cache.txt");
        log.info("/tmp under root path: {}", tmp);

        // wrong usage
        // URL systemResource1 = ClassLoader.getSystemResource("/");
        log.info("");
    }

    @Test
    public void testLoadResourceUseClassMethod() {
        Class<? extends ResourceLoaderTest> aClass = this.getClass();
        // 使用绝对路径（项目根目录）加载资源
        URL resource = aClass.getResource("/tmp/cache.txt");
        log.info("tmp/cache.txt under root path: {}", resource);

        // 使用相对路径（当前类所在的目录）加载资源
        URL relativeFile = aClass.getResource("./JustForTest.class");
        log.info("JustForTest.class under the class same dir: {}", relativeFile);
    }

    @Test
    public void testFindCurDirClasses() {
        URL resource = this.getClass().getResource(".");
        log.info("{} is in dir {}", this.getClass().getName(), resource);
        List<String> classes = findClasses(new File(resource.getFile()), true);
        log.info("classes: {}", classes);
    }

    private List<String> findClasses(File file, boolean subPackage) {
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>(files.length);
        for (File f : files) {
            if (f.isDirectory()) {
                if (subPackage) {
                    list.addAll(findClasses(f, subPackage));
                }
            } else {
                list.add(f.getPath());
            }
        }
        return list;
    }
}


