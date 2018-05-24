package io.devfactory.next.dao;

import io.devfactory.next.model.Answer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnswerDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(AnswerDaoTest.class);

    @Test
    public void addAnswer() throws Exception {

        long questionId = 1L;
        Answer expected = new Answer("javajigi", "answer contents", questionId);

        AnswerDao dut = new JdbcAnswerDao();
        Answer answer = dut.insert(expected);

        logger.debug("Answer : {}", answer);
    }
}
