package io.devfactory.next.service;

import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.Service;
import io.devfactory.next.CannotDeleteException;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Question;
import io.devfactory.next.model.User;

import java.util.List;

@Service
public class QnaService {

    private QuestionDao questionDao;
    private AnswerDao answerDao;

    @Inject
    public QnaService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public Question findById(long questionId) {
        return questionDao.findById(questionId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {
        return answerDao.findAllByQuestionId(questionId);
    }

    public void deleteQuestion(long questionId, User user) throws CannotDeleteException {

        Question question = questionDao.findById(questionId);

        if (question == null) {
            throw new CannotDeleteException("존재하지 않는 질문입니다.");
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);

        if (question.canDelete(user, answers)) {
            questionDao.delete(questionId);
        }
    }
}
