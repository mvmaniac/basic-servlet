package io.devfactory.core.web.oldmvc;

import io.devfactory.core.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController extends AbstractController {

    private String forwardUrl;

    public ForwardController(String forwardUrl) {

        this.forwardUrl = forwardUrl;

        if (forwardUrl == null) {
            throw new NullPointerException("forwardUrl is null. 이동할 URL을 입력하세요.");
        }
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView(forwardUrl);
    }
}
