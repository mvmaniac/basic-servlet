package io.devfactory.core.web.mvc;

import io.devfactory.core.web.view.ModelAndView;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;

public class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;
    private MockHttpServletResponse response;

    @Before
    public void setup() {

        handlerMapping = new AnnotationHandlerMapping("io.devfactory.core.web.nmvc");
        handlerMapping.initialize();

        response = new MockHttpServletResponse();
    }

    @Test
    public void list() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView mav = execution.handle(request, response);
        mav.getView().render(mav.getModel(), request, response);

        assertEquals("/users/list.jsp", response.getForwardedUrl());
    }

    @Test
    public void show() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView mav = execution.handle(request, response);
        mav.getView().render(mav.getModel(), request, response);

        assertEquals("/users/show.jsp", response.getForwardedUrl());
    }

    @Test
    public void create() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView mav = execution.handle(request, response);
        mav.getView().render(mav.getModel(), request, response);

        assertEquals("/users", response.getRedirectedUrl());
    }
}