package io.devfactory.core.di.factory;

import com.google.common.collect.Sets;
import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Repository;
import io.devfactory.core.annotation.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {

    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> scan() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {

        Set<Class<?>> preInstantiatedBeans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            preInstantiatedBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return preInstantiatedBeans;
    }
}
