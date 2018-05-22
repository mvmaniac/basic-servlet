package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.CannotDeleteException;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.model.Result;
import io.devfactory.next.service.QnaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiDeleteQuestionController extends AbstractController {

    private QnaService qnaService = QnaService.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jsonView().addObject("result", Result.fail("Login is required"));
        }

        long questionId = Long.parseLong(req.getParameter("questionId"));

        try {
            qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFromSession(req.getSession()));
            return jsonView().addObject("result", Result.ok());

        } catch (CannotDeleteException e) {
            return jsonView().addObject("result", Result.fail(e.getMessage()));
        }
    }
}