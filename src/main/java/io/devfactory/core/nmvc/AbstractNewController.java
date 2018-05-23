package io.devfactory.core.nmvc;

import io.devfactory.core.mvc.JsonView;
import io.devfactory.core.mvc.JspView;
import io.devfactory.core.mvc.ModelAndView;

public abstract class AbstractNewController {

    protected ModelAndView jspView(String forwardUrl) {
        return new ModelAndView(new JspView(forwardUrl));
    }

    protected ModelAndView jsonView() {
        return new ModelAndView(new JsonView());
    }
}
