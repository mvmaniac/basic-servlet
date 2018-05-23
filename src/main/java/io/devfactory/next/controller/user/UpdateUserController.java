package io.devfactory.next.controller.user;

import io.devfactory.core.mvc.AbstractController;
import io.devfactory.core.mvc.ModelAndView;
import io.devfactory.next.controller.UserSessionUtils;
import io.devfactory.next.dao.UserDao;
import io.devfactory.next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUserController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserController.class);

    private UserDao userDao = UserDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = userDao.findByUserId(request.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));

        logger.debug("Update User : {}", updateUser);

        user.update(updateUser);
        userDao.update(user);

        return jspView("redirect:/");
    }
}
