package io.devfactory.core.di.factory;

import com.google.common.collect.Sets;
import io.devfactory.core.annotation.Inject;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Optional;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;
import static org.reflections.ReflectionUtils.getAllConstructors;
import static org.reflections.ReflectionUtils.withAnnotation;

public class BeanFactoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryUtils.class);

    @SuppressWarnings({ "unchecked" })
    public static Set<Method> getInjectedMethods(Class<?> clazz) {
        return getAllMethods(clazz, withAnnotation(Inject.class), withReturnType(void.class));
    }

    @SuppressWarnings({ "unchecked" })
    public static Set<Method> getBeanMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        return getAllMethods(clazz, withAnnotation(annotation));
    }

    @SuppressWarnings({ "unchecked" })
    public static Set<Field> getInjectedFields(Class<?> clazz) {
        return getAllFields(clazz, withAnnotation(Inject.class));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Set<Constructor> getInjectedConstructors(Class<?> clazz) {
        return getAllConstructors(clazz, withAnnotation(Inject.class));
    }

    public static Optional<Object> invokeMethod(Method method, Object bean, Object[] args) {
        try {
            return Optional.ofNullable(method.invoke(bean, args));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> T instantiate(Class<T> clazz) {

        if (clazz.isInterface()) {
            throw new IllegalStateException(clazz +"는 클래스가 아니다.");

        } else {
            try {
                return clazz.newInstance();

            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("{} class newInstance error : ", clazz, e);
                return null;
            }
        }
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) {

        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }

        try {
            return ctor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("{} constructor newInstance error : ", ctor, e);
            return null;
        }
    }

    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환
     * 
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {

        Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject.class));

        if (injectedConstructors.isEmpty()) {
            return null;
        }

        return injectedConstructors.iterator().next();
    }

    /**
     * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스,
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
     * 
     * @param injectedClazz
     * @param preInstanticateBeans
     * @return
     */
    public static Optional<Class<?>> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstanticateBeans) {

        if (!injectedClazz.isInterface()) {
            return Optional.of(injectedClazz);
        }

        for (Class<?> clazz : preInstanticateBeans) {

            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());

            if (interfaces.contains(injectedClazz)) {
                return Optional.of(clazz);
            }
        }

        return Optional.empty();
    }
}
