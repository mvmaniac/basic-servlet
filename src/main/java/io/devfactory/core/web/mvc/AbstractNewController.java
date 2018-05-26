package io.devfactory.core.web.mvc;

import io.devfactory.core.web.view.JsonView;
import io.devfactory.core.web.view.JspView;
import io.devfactory.core.web.view.ModelAndView;

public abstract class AbstractNewController {

    protected ModelAndView jspView(String forwardUrl) {
        return new ModelAndView(new JspView(forwardUrl));
    }

    protected ModelAndView jsonView() {
        return new ModelAndView(new JsonView());
    }
}
