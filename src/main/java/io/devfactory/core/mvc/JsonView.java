package io.devfactory.core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType("application/json;charset=utf-8");

        ObjectMapper mapper = new ObjectMapper();

        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(model));
    }
}
