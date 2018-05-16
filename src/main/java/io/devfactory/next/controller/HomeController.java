package io.devfactory.next.controller;

import io.devfactory.core.mvc.Controller;
import io.devfactory.core.mvc.JspView;
import io.devfactory.core.mvc.View;
import io.devfactory.next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {

    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        UserDao userDao = new UserDao();
        req.setAttribute("users", userDao.findAll());

        return new JspView("home.jsp");
    }
}
