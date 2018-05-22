package io.devfactory.next.dao;

import io.devfactory.next.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserDaoTest {

    @Test
    public void crud() {

        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = UserDao.getInstance();
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

        UserDao userDao = UserDao.getInstance();
        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }
}