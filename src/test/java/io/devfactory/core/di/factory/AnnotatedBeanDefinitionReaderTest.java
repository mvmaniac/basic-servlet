package io.devfactory.core.di.factory;

import io.devfactory.core.di.factory.example.ExampleConfig;
import io.devfactory.core.di.factory.example.IntegrationConfig;
import io.devfactory.core.di.factory.example.JdbcUserRepository;
import io.devfactory.core.di.factory.example.MyJdbcTemplate;
import org.junit.Test;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

public class AnnotatedBeanDefinitionReaderTest {

    @Test
    public void register_simple() {

        BeanFactory beanFactory = new BeanFactory();

        AnnotatedBeanDefinitionReader abdr = new AnnotatedBeanDefinitionReader(beanFactory);
        abdr.register(ExampleConfig.class);

        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_ClasspathBeanDefinitionScanner_통합() {

        BeanFactory beanFactory = new BeanFactory();

        AnnotatedBeanDefinitionReader abdr = new AnnotatedBeanDefinitionReader(beanFactory);
        abdr.register(IntegrationConfig.class);

        ClasspathBeanDefinitionScanner cbds = new ClasspathBeanDefinitionScanner(beanFactory);
        cbds.doScan("io.devfactory.core.di.factory.example");

        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }
}
