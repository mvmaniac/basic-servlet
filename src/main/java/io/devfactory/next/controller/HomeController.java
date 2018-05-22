package io.devfactory.next.controller;

import io.devfactory.core.mvc.*;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends AbstractController {

    private QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
}
