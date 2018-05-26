package io.devfactory.core.web.mvc;

import io.devfactory.core.di.factory.AnnotationConfigApplicationContext;
import io.devfactory.core.web.view.ModelAndView;
import io.devfactory.next.config.MyConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;

public class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @Before
    public void setup() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);

        handlerMapping = new AnnotationHandlerMapping(ac);
        handlerMapping.initialize();
    }

    @Test
    public void getHandler() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @Test
    public void list() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView mav = execution.handle(request, response);
        mav.getView().render(mav.getModel(), request, response);

        assertEquals("/users/list.jsp", response.getForwardedUrl());
    }

    @Test
    public void show() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView mav = execution.handle(request, response);
        mav.getView().render(mav.getModel(), request, response);

        assertEquals("/users/show.jsp", response.getForwardedUrl());
    }

    @Test
    public void create() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView mav = execution.handle(request, response);
        mav.getView().render(mav.getModel(), request, response);

        assertEquals("/users", response.getRedirectedUrl());
    }
}