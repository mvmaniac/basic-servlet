package io.devfactory.core.di.factory.example;

import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.RequestMapping;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.core.nmvc.AbstractNewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QnaController extends AbstractNewController {
    
    private MyQnaService qnaService;

    @Inject
    public QnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }

    @RequestMapping("/questions")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("/qna/list.jsp");
    }
}
