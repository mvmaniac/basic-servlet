package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowQuestionController extends AbstractController {

    private QuestionDao questionDao;
    private AnswerDao answerDao;

    public ShowQuestionController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long questionId = Long.parseLong(request.getParameter("questionId"));

        Question question = questionDao.findById(questionId);
        List<Answer> answers = answerDao.findAllByQuestionId(questionId);

        ModelAndView mav = jspView("/qna/show.jsp");

        mav.addObject("question", question);
        mav.addObject("answers", answers);

        return mav;
    }
}
