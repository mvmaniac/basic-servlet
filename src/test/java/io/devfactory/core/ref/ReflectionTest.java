package io.devfactory.core.ref;

import io.devfactory.next.model.Question;
import io.devfactory.next.model.User;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        printFields(clazz.getFields());
        printFields(clazz.getDeclaredFields());
        printConstructors(clazz.getConstructors());
        printConstructors(clazz.getDeclaredConstructors());
        printMethods(clazz.getMethods());
        printMethods(clazz.getDeclaredMethods());
    }
    
    @Test
    public void newInstanceWithConstructorArgs() throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<User> clazz = User.class;

        String stringName = String.class.toString();
        String type;

        for (Constructor constructor : clazz.getConstructors()) {

            int count = constructor.getParameterCount();

            Object[] objects = new Object[count];

            for (int i = 0; i < count; i++) {

                type = constructor.getParameters()[i].getType().toString();

                if (type.equals(stringName)) {
                    objects[i] = "o"+i;
                }
            }

            Object obj = constructor.newInstance(objects);
            logger.debug(obj.toString());
        }
    }
    
    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
    }

    private void printFields(Field[] fields) {
        for (Field field : fields) {
            logger.debug("Field {} {}", field.getType(), field.getName());
        }
    }

    private void printConstructors(Constructor[] constructors) {
        for (Constructor constructor : constructors) {
            logger.debug("Constructor {}", constructor.getName());
        }
    }

    private void printMethods(Method[] methods) {
        for (Method method : methods) {
            logger.debug("Method {}", method.getName());
        }
    }
}
