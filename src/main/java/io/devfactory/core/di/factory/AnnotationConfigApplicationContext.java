package io.devfactory.core.di.factory;

import com.google.common.collect.Lists;
import io.devfactory.core.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationConfigApplicationContext.class);

    private BeanFactory beanFactory;

    public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {

        Object[] basePackages = findBasePackages(annotatedClasses);

        beanFactory = new BeanFactory();

        AnnotatedBeanDefinitionReader abdr = new AnnotatedBeanDefinitionReader(beanFactory);
        abdr.register(annotatedClasses);

        if (basePackages.length > 0) {
            ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner(beanFactory);
            scanner.doScan(basePackages);
        }

        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    private Object[] findBasePackages(Class<?>[] annotatedClasses) {

        List<Object> basePackages = Lists.newArrayList();

        for (Class<?> annotatedClass : annotatedClasses) {

            ComponentScan componentScan = annotatedClass.getAnnotation(ComponentScan.class);

            if (componentScan == null) {
                continue;
            }

            for (String basePackage : componentScan.value()) {
                logger.info("Component Scan basePackage : {}", basePackage);
            }

            basePackages.addAll(Arrays.asList(componentScan.value()));
        }
        return basePackages.toArray();
    }
}
