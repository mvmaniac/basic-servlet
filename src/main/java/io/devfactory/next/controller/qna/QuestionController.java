package io.devfactory.next.controller.qna;

import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.annotation.RequestMethod;
import io.devfactory.core.web.view.ModelAndView;
import io.devfactory.core.web.mvc.AbstractNewController;
import io.devfactory.next.CannotDeleteException;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.AnswerDao;
import io.devfactory.next.dao.QuestionDao;
import io.devfactory.next.model.Answer;
import io.devfactory.next.model.Question;
import io.devfactory.next.model.User;
import io.devfactory.next.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class QuestionController extends AbstractNewController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private QnaService qnaService;
    private QuestionDao questionDao;
    private AnswerDao answerDao;

    @Inject
    public QuestionController(QnaService qnaService, QuestionDao questionDao, AnswerDao answerDao) {

        this.qnaService = qnaService;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @RequestMapping("/qna/show")
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long questionId = Long.parseLong(request.getParameter("questionId"));

        Question question = questionDao.findById(questionId);
        List<Answer> answers = answerDao.findAllByQuestionId(questionId);

        ModelAndView mav = jspView("/qna/show.jsp");

        mav.addObject("question", question);
        mav.addObject("answers", answers);

        return mav;
    }

    @RequestMapping("/qna/form")
    public ModelAndView createForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        return jspView("/qna/form.jsp");
    }

    @RequestMapping(value = "/qna/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        User user = UserSessionUtils.getUserFromSession(request.getSession());

        if (user == null) {
            return jspView("redirect:/users/loginForm");
        }

        Question question = new Question(user.getName(), request.getParameter("title"), request.getParameter("contents"));
        questionDao.insert(question);

        return jspView("redirect:/");
    }

    @RequestMapping("/qna/updateForm")
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));

        Question question = questionDao.findById(questionId);

        if (!question.isSameUserName(UserSessionUtils.getUserFromSession(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
        }

        return jspView("/qna/update.jsp").addObject("question", question);
    }

    @RequestMapping(value = "/qna/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));

        Question question = questionDao.findById(questionId);

        if (!question.isSameUserName(UserSessionUtils.getUserFromSession(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
        }

        Question newQuestion = new Question(question.getWriter(), request.getParameter("title"), request.getParameter("contents"));

        question.update(newQuestion);
        questionDao.update(question);

        return jspView("redirect:/");
    }

    @RequestMapping(value = "/qna/delete", method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));

        try {
            qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFromSession(request.getSession()));
            return jspView("redirect:/");

        } catch (CannotDeleteException e) {
            return jspView("show.jsp").addObject("question", qnaService.findById(questionId))
                    .addObject("answers", qnaService.findAllByQuestionId(questionId))
                    .addObject("errorMessage", e.getMessage());
        }
    }
}
