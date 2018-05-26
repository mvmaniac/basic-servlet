package io.devfactory.core.di.factory;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public abstract class AbstractInjector implements Injector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInjector.class);

    private BeanFactory beanFactory;

    public AbstractInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(Class<?> clazz) {

        instantiateClass(clazz);

        Set<?> injectedBeans = getInjectedBeans(clazz);

        for (Object injectedBean : injectedBeans) {
            Class<?> beanClass = getBeanClass(injectedBean);
            inject(injectedBean, instantiateClass(beanClass), beanFactory);
        }
    }

    abstract Set<?> getInjectedBeans(Class<?> clazz);

    abstract Class<?> getBeanClass(Object injectedBean);

    abstract void inject(Object injectedBean, Object bean, BeanFactory beanFactory);

    private Object instantiateClass(Class<?> clazz) {

        Class<?> concreteClass = findBeanClass(clazz, beanFactory.getPreInstanticateBeans());

        Object bean = beanFactory.getBean(concreteClass);

        if (bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

        if (injectedConstructor == null) {

            if (concreteClass.isInterface()) {
                throw new IllegalStateException(clazz +"는 클래스가 아니다.");

            } else {
                try {
                    bean = concreteClass.newInstance();
                    beanFactory.registerBean(concreteClass, bean);

                    return bean;

                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("{} class newInstance error : ", concreteClass, e);
                    return null;
                }
            }
        }

        logger.debug("Constructor : {}", injectedConstructor);

        bean = instantiateConstructor(injectedConstructor);
        beanFactory.registerBean(concreteClass, bean);

        return bean;
    }

    private Object instantiateConstructor(Constructor<?> constructor) {

        Class<?>[] pTypes = constructor.getParameterTypes();

        List<Object> args = Lists.newArrayList();

        for (Class<?> clazz : pTypes) {

            Class<?> concreteClazz = findBeanClass(clazz, beanFactory.getPreInstanticateBeans());

            Object bean = beanFactory.getBean(concreteClazz);

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

    private Class<?> findBeanClass(Class<?> clazz, Set<Class<?>> preInstanticateBeans) {

        Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);

        if (!preInstanticateBeans.contains(concreteClazz)) {
            throw new IllegalStateException(clazz + "는 Bean이 아니다.");
        }

        return concreteClazz;
    }
}
