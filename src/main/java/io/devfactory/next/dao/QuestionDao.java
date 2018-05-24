package io.devfactory.next.dao;

import io.devfactory.next.model.Question;

import java.util.List;

public interface QuestionDao {

    Question insert(Question question);

    void update(Question question);

    void updateCountOfAnswer(long questionId);

    void delete(long questionId);

    List<Question> findAll();

    Question findById(long questionId);
}
