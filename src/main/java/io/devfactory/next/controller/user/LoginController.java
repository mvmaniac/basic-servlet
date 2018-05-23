package io.devfactory.next.controller.user;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.UserDao;
import io.devfactory.next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController extends AbstractController {

    private UserDao userDao = UserDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = userDao.findByUserId(userId);

        if (user == null) {
            return jspView("/user/login.jsp").addObject("loginFailed", true);
        }

        if (user.matchPassword(password)) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return jspView("redirect:/");

        } else {
            request.setAttribute("loginFailed", true);
            return jspView("/user/login.jsp").addObject("loginFailed", true);
        }
    }
}
