package io.devfactory.next.dao;

import io.devfactory.core.jdbc.ConnectionManager;
import io.devfactory.next.model.Answer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class AnswerDaoTest {

    private static final Logger loggger = LoggerFactory.getLogger(AnswerDaoTest.class);

    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void addAnswer() throws Exception {

        long questionId = 1L;

        Answer expected = new Answer("javajigi", "answer contents", questionId);

        AnswerDao dut = AnswerDao.getInstance();
        Answer answer = dut.insert(expected);

        loggger.debug("Answer : {}", answer);
    }
}
