package io.devfactory.next.service;

import io.devfactory.next.CannotDeleteException;
import io.devfactory.next.dao.JdbcAnswerDao;
import io.devfactory.next.dao.JdbcQuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Question;
import io.devfactory.next.model.User;

import java.util.List;

public class QnaService {

    private JdbcQuestionDao questionDao;
    private JdbcAnswerDao answerDao;

    public QnaService(JdbcQuestionDao questionDao, JdbcAnswerDao answerDao) {
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

        if (!question.isSameUserName(user)) {
            throw new CannotDeleteException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);

        if (answers.isEmpty()) {
            questionDao.delete(questionId);
            return;
        }

        String writer = question.getWriter();
        boolean canDelete = true;

        for (Answer answer : answers) {
            if (!writer.equals(answer.getWriter())) {
                canDelete = false;
                break;
            }
        }

        if (!canDelete) {
            throw new CannotDeleteException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
        }

        questionDao.delete(questionId);
    }
}
