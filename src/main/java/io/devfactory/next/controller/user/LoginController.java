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

    private UserDao userDao = new UserDao();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        User user = userDao.findByUserId(userId);

        if (user == null) {
            return jspView("/user/login.jsp").addObject("loginFailed", true);
        }

        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return jspView("redirect:/");

        } else {
            req.setAttribute("loginFailed", true);
            return jspView("/user/login.jsp").addObject("loginFailed", true);
        }
    }
}
