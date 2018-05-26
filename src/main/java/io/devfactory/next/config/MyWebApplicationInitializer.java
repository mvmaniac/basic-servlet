package io.devfactory.next.config;

import io.devfactory.core.di.factory.AnnotationConfigApplicationContext;
import io.devfactory.core.di.factory.ApplicationContext;
import io.devfactory.core.web.WebApplicationInitializer;
import io.devfactory.core.web.mvc.AnnotationHandlerMapping;
import io.devfactory.core.web.mvc.DispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(MyWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        ApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(ac);
        ahm.initialize();

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(ahm));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        logger.info("Start MyWebApplication Initializer");
    }
}
