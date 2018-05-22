package io.devfactory.next.dao;

import io.devfactory.core.jdbc.JdbcTemplate;
import io.devfactory.core.jdbc.RowMapper;
import io.devfactory.next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private static UserDao userDao = new UserDao();

    private JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    private UserDao() {}

    public static UserDao getInstance() {
        return userDao;
    }

    public void insert(User user) {

        String sql = "INSERT INTO users VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {

        jdbcTemplate.update("UPDATE users SET password = ?, name = ?, email = ? WHERE userId = ?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {

        String sql = "SELECT userId, password, name, email FROM users";

        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        };

        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userId) {

        String sql = "SELECT userId, password, name, email FROM users WHERE userid = ?";

        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        };

        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }
}
