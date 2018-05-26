package io.devfactory.core.di.factory;

import io.devfactory.core.di.factory.example.MyQnaService;
import io.devfactory.core.di.factory.example.MyUserController;
import io.devfactory.core.di.factory.example.MyUserService;
import io.devfactory.core.di.factory.example.QnaController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class BeanFactoryTest {

    private Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {

        BeanScanner scanner = new BeanScanner("io.devfactory.core.di.factory.example");

        Set<Class<?>> preInstanticateClazz = scanner.scan();

        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    public void constructorDI() throws Exception {

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();

        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void fieldDI() throws Exception {

        MyUserService userService = beanFactory.getBean(MyUserService.class);

        assertNotNull(userService);
        assertNotNull(userService.getUserRepository());
    }

    @Test
    public void setterDI() throws Exception {

        MyUserController userController = beanFactory.getBean(MyUserController.class);

        assertNotNull(userController);
        assertNotNull(userController.getUserService());
    }

    @Test
    public void getControllers() throws Exception {

        Map<Class<?>, Object> controllers = beanFactory.getControllers();
        Set<Class<?>> keys = controllers.keySet();

        for (Class<?> clazz : keys) {
            logger.debug("Bean : {}", clazz);
        }
    }

    @After
    public void tearDown() {
        beanFactory.clear();
    }
}
