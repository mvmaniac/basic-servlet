package io.devfactory.next.controller.qna;

import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.annotation.RequestMethod;
import io.devfactory.core.jdbc.DataAccessException;
import io.devfactory.core.web.view.ModelAndView;
import io.devfactory.core.web.mvc.AbstractNewController;
import io.devfactory.next.CannotDeleteException;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Result;
import io.devfactory.next.model.User;
import io.devfactory.next.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiQuestionController extends AbstractNewController {

    private static final Logger logger = LoggerFactory.getLogger(ApiQuestionController.class);

    private QnaService qnaService;
    private QuestionDao questionDao;
    private AnswerDao answerDao;

    @Inject
    public ApiQuestionController(QnaService qnaService, QuestionDao questionDao, AnswerDao answerDao) {

        this.qnaService = qnaService;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @RequestMapping(value = "/api/qna/deleteQuestion", method = RequestMethod.POST)
    public ModelAndView deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jsonView().addObject("result", Result.fail("Login is required"));
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));

        try {
            qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFromSession(request.getSession()));
            return jsonView().addObject("result", Result.ok());

        } catch (CannotDeleteException e) {
            return jsonView().addObject("result", Result.fail(e.getMessage()));
        }
    }

    @RequestMapping("/api/qna/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jsonView().addObject("questions", questionDao.findAll());
    }

    @RequestMapping(value = "/api/qna/addAnswer", method = RequestMethod.POST)
    public ModelAndView addAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {

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

    @RequestMapping(value = "/api/qna/deleteAnswer", method = RequestMethod.POST)
    public ModelAndView deleteAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long answerId = Long.parseLong(request.getParameter("answerId"));

        ModelAndView mav = jsonView();

        try {
            answerDao.delete(answerId);
            mav.addObject("result", Result.ok());
        } catch (DataAccessException e) {
            mav.addObject("result", Result.fail(e.getMessage()));
        }

        return mav;
    }
}
