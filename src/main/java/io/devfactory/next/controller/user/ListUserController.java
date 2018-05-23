package io.devfactory.next.controller.user;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController extends AbstractController {

    private UserDao userDao = UserDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        return jspView("/user/list.jsp").addObject("users", userDao.findAll());
    }
}
