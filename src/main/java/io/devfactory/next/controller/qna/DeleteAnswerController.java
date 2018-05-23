package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController extends AbstractController {

    private AnswerDao answerDao = AnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long answerId = Long.parseLong(request.getParameter("answerId"));

        answerDao.delete(answerId);
        return jsonView().addObject("result", Result.ok());
    }
}
