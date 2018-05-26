package io.devfactory.core.web.oldmvc;

import io.devfactory.core.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
