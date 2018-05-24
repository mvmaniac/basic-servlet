package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.*;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.JdbcAnswerDao;
import io.devfactory.next.dao.JdbcQuestionDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Result;
import io.devfactory.next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAnswerController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AddAnswerController.class);

    private QuestionDao questionDao;
    private AnswerDao answerDao;

    public AddAnswerController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jsonView().addObject("result", Result.fail("Login is required"));
        }

        User user = UserSessionUtils.getUserFromSession(request.getSession());

        Answer answer = new Answer(user.getName(), request.getParameter("contents"), Long.parseLong(request.getParameter("questionId")));

        logger.debug("answer : {}", answer);

        Answer savedAnswer = answerDao.insert(answer);
        questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());

        return jsonView().addObject("answer", savedAnswer).addObject("result", Result.ok());
    }
}
