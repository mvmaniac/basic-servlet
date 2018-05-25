package io.devfactory.core.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.devfactory.core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {

        for (Class<?> clazz : preInstanticateBeans) {

            if (beans.get(clazz) == null) {
                logger.debug("instantiated Class : {}", clazz);
                instantiateClass(clazz);
            }
        }
    }

    public Map<Class<?>, Object> getControllers() {

        Map<Class<?>, Object> controllers = Maps.newHashMap();

        for (Class<?> clazz : preInstanticateBeans) {

            Annotation annotation = clazz.getAnnotation(Controller.class);

            if (annotation != null) {
                controllers.put(clazz, beans.get(clazz));
            }
        }

        return controllers;
    }

    private Object instantiateClass(Class<?> clazz) {

        Object bean = beans.get(clazz);

        if (bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (injectedConstructor == null) {

            if (clazz.isInterface()) {
                throw new IllegalStateException(clazz +"는 클래스가 아니다.");

            } else {
                try {
                    bean = clazz.newInstance();
                    beans.put(clazz, bean);

                    return bean;

                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("{} class newInstance error : ", clazz, e);
                    return null;
                }
            }
        }

        logger.debug("Constructor : {}", injectedConstructor);

        bean = instantiateConstructor(injectedConstructor);
        beans.put(clazz, bean);

        return bean;
    }

    private Object instantiateConstructor(Constructor<?> constructor) {

        Class<?>[] pTypes = constructor.getParameterTypes();

        List<Object> args = Lists.newArrayList();

        for (Class<?> clazz : pTypes) {

            Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);

            if (!preInstanticateBeans.contains(concreteClazz)) {
                throw new IllegalStateException(clazz +"는 Bean이 아니다.");
            }

            Object bean = beans.get(concreteClazz);

            if (bean == null) {
                bean = instantiateClass(concreteClazz);
            }

            args.add(bean);
        }

        if ((!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) && !constructor.isAccessible()) {
            constructor.setAccessible(true);
        }

        try {
            return constructor.newInstance(args.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("{} constructor newInstance error : ", constructor, e);
        }

        return null;
    }
}
