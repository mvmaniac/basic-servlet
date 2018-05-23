package io.devfactory.core.mvc;

import com.google.common.collect.Lists;
import io.devfactory.core.nmvc.AnnotationHandlerMapping;
import io.devfactory.core.nmvc.HandlerExecution;
import io.devfactory.core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> mappings = Lists.newArrayList();

    @Override
    public void init() {

        LegacyHandlerMapping lhm = new LegacyHandlerMapping();
        lhm.initMapping();

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping();
        ahm.initialize();

        mappings.add(lhm);
        mappings.add(ahm);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        Object handler = getHandler(request);

        if (handler == null) {
            throw new IllegalArgumentException("존재하지 않는 URL입니다.");
        }

        try {
            ModelAndView mav = execute(handler, request, response);

            View view = mav.getView();
            view.render(mav.getModel(), request, response);

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {

        for (HandlerMapping handlerMapping : mappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    private ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (handler instanceof Controller) {
            return ((Controller)handler).execute(request, response);
        } else {
            return ((HandlerExecution)handler).handle(request, response);
        }
    }
}
