package io.devfactory.core.mvc;

import io.devfactory.core.nmvc.DispatcherServlet;
import io.devfactory.core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
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
