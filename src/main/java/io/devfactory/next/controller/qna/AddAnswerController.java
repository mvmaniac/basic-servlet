package io.devfactory.next.controller.qna;

import io.devfactory.core.mvc.Controller;
import io.devfactory.core.mvc.JsonView;
import io.devfactory.core.mvc.View;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.model.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAnswerController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(AddAnswerController.class);

    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        Answer answer = new Answer(req.getParameter("writer"), req.getParameter("contents"), Long.parseLong(req.getParameter("questionId")));

        logger.debug("answer : {}", answer);

        AnswerDao answerDao = new AnswerDao();
        Answer saveAnswer = answerDao.insert(answer);

        req.setAttribute("answer", saveAnswer);
        return new JsonView();
    }
}
