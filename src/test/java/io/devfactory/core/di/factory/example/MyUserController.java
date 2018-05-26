package io.devfactory.core.di.factory.example;

import io.devfactory.core.annotation.Controller;
import io.devfactory.core.annotation.Inject;

@Controller
public class MyUserController {

    private MyUserService userService;

    @Inject
    public void setUserService(MyUserService userService) {
        this.userService = userService;
    }

    public MyUserService getUserService() {
        return userService;
    }
}
