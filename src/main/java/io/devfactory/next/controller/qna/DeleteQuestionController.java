package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DeleteQuestionController extends AbstractController {

    private QuestionDao questionDao = QuestionDao.getInstance();
    private AnswerDao answerDao = AnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(req.getParameter("questionId"));


        Question question = questionDao.findById(questionId);

        if (question == null) {
            jspView("redirect:/users/loginForm");
        }

        if (!question.isSameUserName(UserSessionUtils.getUserFromSession(req.getSession()))) {
            jspView("redirect:/users/loginForm");
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);

        if (answers.isEmpty()) {
            questionDao.delete(questionId);
            return jspView("redirect:/users/loginForm");
        }

        boolean canDelete = true;

        for (Answer answer : answers) {
            String writer = question.getWriter();
            if (!writer.equals(answer.getWriter())) {
                canDelete = false;
                break;
            }
        }

        if (!canDelete) {
            //throw new CannotDeleteException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
            return jspView("redirect:/users/loginForm");
        }

        questionDao.delete(questionId);
        return jspView("redirect:/");
    }
}
