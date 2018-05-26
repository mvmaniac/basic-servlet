package io.devfactory.next.controller;

import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.web.view.ModelAndView;
import io.devfactory.core.web.mvc.AbstractNewController;
import io.devfactory.next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController extends AbstractNewController {

    private QuestionDao questionDao;

    @Inject
    public HomeController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @RequestMapping("/")
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
}
