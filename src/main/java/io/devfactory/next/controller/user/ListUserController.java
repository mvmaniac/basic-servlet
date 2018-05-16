package io.devfactory.next.controller.user;

import io.devfactory.core.mvc.Controller;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }

        UserDao userDao = new UserDao();
        req.setAttribute("users", userDao.findAll());

        return "/user/list.jsp";
    }
}
