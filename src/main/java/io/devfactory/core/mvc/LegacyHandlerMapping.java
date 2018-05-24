package io.devfactory.core.mvc;

import io.devfactory.core.nmvc.DispatcherServlet;
import io.devfactory.core.nmvc.HandlerMapping;
import io.devfactory.next.controller.qna.*;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.JdbcAnswerDao;
import io.devfactory.next.dao.JdbcQuestionDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {

        QuestionDao questionDao = new JdbcQuestionDao();
        AnswerDao answerDao = new JdbcAnswerDao();
        QnaService qnaService = new QnaService(questionDao, answerDao);

        mappings.put("/qna/show", new ShowQuestionController());
        mappings.put("/qna/form", new CreateFormQuestionController());
        mappings.put("/qna/create", new CreateQuestionController());
        mappings.put("/qna/updateForm", new UpdateFormQuestionController());
        mappings.put("/qna/update", new UpdateQuestionController());
        mappings.put("/qna/delete", new DeleteQuestionController());
        mappings.put("/api/qna/deleteQuestion", new ApiDeleteQuestionController());
        mappings.put("/api/qna/list", new ApiListQuestionController());
        mappings.put("/api/qna/addAnswer", new AddAnswerController());
        mappings.put("/api/qna/deleteAnswer", new DeleteAnswerController());

        logger.info("Initialized Request Mapping!");
    }

    public Controller findController(String url) {
        return mappings.get(url);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return mappings.get(request.getRequestURI());
    }
}
