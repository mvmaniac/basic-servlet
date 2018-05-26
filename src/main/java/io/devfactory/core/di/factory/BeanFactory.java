package io.devfactory.core.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.devfactory.core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory implements BeanDefinitionRegistry {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        logger.debug("register bean : {}", clazz);
        beanDefinitions.put(clazz, beanDefinition);
    }

    public void initialize() {

        for (Class<?> clazz : getBeanClasses()) {

            if (beans.get(clazz) == null) {
                logger.debug("instantiated Class : {}", clazz);
                getBean(clazz);
            }
        }
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitions.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {

        Object bean = beans.get(clazz);

        if (bean != null) {
            return (T) bean;
        }

        Class<?> concreteClass = findConcreteClass(clazz);

        BeanDefinition beanDefinition = beanDefinitions.get(concreteClass);

        bean = inject(beanDefinition);
        beans.put(concreteClass, bean);

        return (T) bean;
    }

    private Class<?> findConcreteClass(Class<?> clazz) {

        Set<Class<?>> beanClasses = getBeanClasses();

        Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanClasses);

        if (!beanClasses.contains(concreteClazz)) {
            throw new IllegalStateException(clazz + "는 Bean이 아니다.");
        }

        return concreteClazz;
    }

    private Object inject(BeanDefinition beanDefinition) {

        if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_NO) {
            return BeanFactoryUtils.instantiate(beanDefinition.getBeanClass());

        } else if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_FIELD) {
            return injectFields(beanDefinition);

        } else {
            return injectConstructor(beanDefinition);
        }
    }

    private Object injectConstructor(BeanDefinition beanDefinition) {

        Constructor<?> constructor = beanDefinition.getInjectConstructor();

        List<Object> args = Lists.newArrayList();

        for (Class<?> clazz : constructor.getParameterTypes()) {
            args.add(getBean(clazz));
        }

        return BeanFactoryUtils.instantiateClass(constructor, args.toArray());
    }

    private Object injectFields(BeanDefinition beanDefinition) {

        Object bean = BeanFactoryUtils.instantiate(beanDefinition.getBeanClass());

        Set<Field> injectFields = beanDefinition.getInjectFields();

        for (Field field : injectFields) {
            injectField(bean, field);
        }

        return bean;
    }

    private void injectField(Object bean, Field field) {

        logger.debug("Inject Bean : {}, Field : {}", bean, field);

        try {
            field.setAccessible(true);
            field.set(bean, getBean(field.getType()));

        } catch (IllegalAccessException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    public void clear() {
        beanDefinitions.clear();
        beans.clear();
    }
}
