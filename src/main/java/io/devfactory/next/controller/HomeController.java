package io.devfactory.next.controller;

import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.core.nmvc.AbstractNewController;
import io.devfactory.next.dao.JdbcQuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController extends AbstractNewController {

    private JdbcQuestionDao questionDao;

    public HomeController(JdbcQuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @RequestMapping("/")
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
}
