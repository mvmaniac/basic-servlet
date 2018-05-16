package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.Controller;
import io.devfactory.core.mvc.JsonView;
import io.devfactory.core.mvc.View;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController implements Controller {

    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        Long answerId = Long.parseLong(req.getParameter("answerId"));

        AnswerDao answerDao = new AnswerDao();
        answerDao.delete(answerId);

        req.setAttribute("result", Result.ok());
        return new JsonView();
    }
}
