package io.devfactory.next.dao;

import io.devfactory.next.model.Answer;

import java.util.List;

public interface AnswerDao {

    Answer insert(Answer answer);

    void delete(Long answerId);

    Answer findById(long answerId);

    List<Answer> findAllByQuestionId(long questionId);
}