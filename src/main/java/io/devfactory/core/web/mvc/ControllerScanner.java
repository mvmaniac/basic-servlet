package io.devfactory.core.web.mvc;

import com.google.common.collect.Maps;
import io.devfactory.core.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {

        Set<Class<?>> preInitiatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(preInitiatedControllers);
    }

    private Map<Class<?>,Object> instantiateControllers(Set<Class<?>> preInitiatedControllers) {

        Map<Class<?>, Object> controllers = Maps.newHashMap();

        try {
            for (Class<?> clazz : preInitiatedControllers) {
                controllers.put(clazz, clazz.newInstance());
            }

        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }

        return controllers;
    }
}
