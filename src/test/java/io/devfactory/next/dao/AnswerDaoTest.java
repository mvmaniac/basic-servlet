package io.devfactory.next.dao;

import io.devfactory.core.di.factory.AnnotationConfigApplicationContext;
import io.devfactory.next.config.MyConfiguration;
import io.devfactory.next.model.Answer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnswerDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(AnswerDaoTest.class);

    private AnswerDao answerDao;

    @Before
    public void setup() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);
        answerDao = ac.getBean(AnswerDao.class);
    }

    @Test
    public void addAnswer() throws Exception {

        long questionId = 1L;

        Answer expected = new Answer("javajigi", "answer contents", questionId);
        Answer answer = answerDao.insert(expected);

        logger.debug("Answer : {}", answer);
    }
}
