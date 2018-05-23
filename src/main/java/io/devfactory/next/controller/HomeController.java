package io.devfactory.next.controller;

import io.devfactory.core.mvc.*;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends AbstractController {

    private QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
}
