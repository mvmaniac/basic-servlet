package io.devfactory.core.di.factory;

import com.google.common.collect.Sets;
import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Repository;
import io.devfactory.core.annotation.Service;
import io.devfactory.core.di.factory.example.MyQnaService;
import io.devfactory.core.di.factory.example.QnaController;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class BeanFactoryTest {

    private Reflections reflections;
    private BeanFactory beanFactory;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {

        reflections = new Reflections("io.devfactory.core.di.factory.example");

        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    public void di() throws Exception {

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {

        Set<Class<?>> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return beans;
    }
}
