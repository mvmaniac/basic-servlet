package io.devfactory.next.controller;

import io.devfactory.core.mvc.Controller;
import io.devfactory.next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserDao userDao = new UserDao();
        req.setAttribute("users", userDao.findAll());
        return "home.jsp";
    }
}
