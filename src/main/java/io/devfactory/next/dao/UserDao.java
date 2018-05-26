package io.devfactory.next.dao;

import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.Repository;
import io.devfactory.core.jdbc.JdbcTemplate;
import io.devfactory.core.jdbc.RowMapper;
import io.devfactory.next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Inject
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
