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

import static org.junit.Assert.assertNotNull;

public class BeanFactoryTest {

    private Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @Before
    public void setup() {

        beanFactory = new BeanFactory();

        ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner(beanFactory);
        scanner.doScan("core.di.factory.example");

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

    @After
    public void tearDown() {
        beanFactory.clear();
    }
}
