package io.devfactory.core.di.factory;

import com.google.common.collect.Maps;
import io.devfactory.core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private List<Injector> injectors;

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {

        this.preInstanticateBeans = preInstanticateBeans;
        injectors = Arrays.asList(new FieldInjector(this), new SetterInjector(this), new ConstructorInjector(this));
    }

    public Set<Class<?>> getPreInstanticateBeans() {
        return preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {

        for (Class<?> clazz : preInstanticateBeans) {

            if (beans.get(clazz) == null) {
                logger.debug("instantiated Class : {}", clazz);
                inject(clazz);
            }
        }
    }

    public void registerBean(Class<?> clazz, Object bean) {
        beans.put(clazz, bean);
    }

    public void clear() {
        preInstanticateBeans.clear();
        beans.clear();
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

    private void inject(Class<?> clazz) {
        for (Injector injector : injectors) {
            injector.inject(clazz);
        }
    }
}
