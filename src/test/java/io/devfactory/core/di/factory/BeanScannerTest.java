package io.devfactory.core.di.factory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BeanScannerTest {

    private Logger logger = LoggerFactory.getLogger(BeanScannerTest.class);

    @Test
    public void scan() throws Exception {

        BeanScanner scanner = new BeanScanner("io.devfactory.core.di.factory.example", "io.devfactory.next.controller");
        Set<Class<?>> beans = scanner.scan();

        for (Class<?> clazz : beans) {
            logger.debug("Bean : {}", clazz);
        }
    }
}
