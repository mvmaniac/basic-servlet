package io.devfactory.next.controller.user;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.controller.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutController extends AbstractController {

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return jspView("redirect:/");
    }
}
