package io.devfactory.core.nmvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}