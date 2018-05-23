package io.devfactory.core.nmvc;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.annotation.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {

        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

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

        return handlerExecutions.get(new HandlerKey(requestUri, rm));
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
