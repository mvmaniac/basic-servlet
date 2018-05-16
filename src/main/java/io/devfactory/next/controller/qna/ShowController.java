package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.Controller;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        Long questionId = Long.parseLong(req.getParameter("questionId"));

        QuestionDao questionDao = new QuestionDao();
        AnswerDao answerDao = new AnswerDao();

        req.setAttribute("question", questionDao.findById(questionId));
        req.setAttribute("answers", answerDao.findAllByQuestionId(questionId));

        return "/qna/show.jsp";
    }
}
