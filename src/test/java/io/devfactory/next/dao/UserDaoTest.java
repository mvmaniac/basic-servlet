package io.devfactory.next.dao;

import io.devfactory.core.di.factory.AnnotationConfigApplicationContext;
import io.devfactory.next.config.MyConfiguration;
import io.devfactory.next.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserDaoTest {

    private UserDao userDao;

    @Before
    public void setup() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);
        userDao = ac.getBean(UserDao.class);
    }

    @Test
    public void crud() {

        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);
    }

    @Test
    public void findAll() {

        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }
}