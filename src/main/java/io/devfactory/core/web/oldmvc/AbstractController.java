package io.devfactory.core.web.oldmvc;

import io.devfactory.core.web.view.JsonView;
import io.devfactory.core.web.view.JspView;
import io.devfactory.core.web.view.ModelAndView;

public abstract class AbstractController implements Controller {

    protected ModelAndView jspView(String forwardUrl) {
        return new ModelAndView(new JspView(forwardUrl));
    }

    protected ModelAndView jsonView() {
        return new ModelAndView(new JsonView());
    }
}
