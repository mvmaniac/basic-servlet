package io.devfactory.core.web.mvc;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.annotation.RequestMethod;
import io.devfactory.core.di.factory.ApplicationContext;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackages;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackages = basePackage;
    }

    public void initialize() {

        ApplicationContext ac = new ApplicationContext(basePackages);
        Map<Class<?>, Object> controllers = getControllers(ac);

        Set<Method> methods = getRequestMappingMethods(controllers.keySet());

        for (Method method : methods) {

            RequestMapping rm = method.getAnnotation(RequestMapping.class);

            logger.debug("register handlerExecution : url is {}, method is {}", rm.value(), rm.method());

            handlerExecutions.put(createHandlerKey(rm), new HandlerExecution(controllers.get(method.getDeclaringClass()), method));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {

        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        logger.debug("requestUri : {}, requestMethod : {}", requestUri, rm);

        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private Map<Class<?>, Object> getControllers(ApplicationContext ac) {

        Map<Class<?>, Object> controllers = Maps.newHashMap();

        for (Class<?> clazz : ac.getBeanClasses()) {

            Annotation annotation = clazz.getAnnotation(Controller.class);

            if (annotation != null) {
                controllers.put(clazz, ac.getBean(clazz));
            }
        }

        return controllers;
    }

    @SuppressWarnings("unchecked")
    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {

        Set<Method> requestMappingMethods = Sets.newHashSet();

        for (Class<?> clazz : controllers) {
            requestMappingMethods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }

        return requestMappingMethods;
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }
}
