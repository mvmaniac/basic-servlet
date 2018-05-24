package io.devfactory.next.dao;

import io.devfactory.core.jdbc.JdbcTemplate;
import io.devfactory.core.jdbc.KeyHolder;
import io.devfactory.core.jdbc.PreparedStatementCreator;
import io.devfactory.core.jdbc.RowMapper;
import io.devfactory.next.model.Question;

import java.sql.*;
import java.util.List;

public class JdbcQuestionDao implements QuestionDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcQuestionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Question insert(Question question) {

        String sql = "INSERT INTO questions (writer, title, contents, createdDate) VALUES (?, ?, ?, ?)";

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, question.getWriter());
                pstmt.setString(2, question.getTitle());
                pstmt.setString(3, question.getContents());
                pstmt.setTimestamp(4, new Timestamp(question.getTimeFromCreateDate()));
                return pstmt;
            }
        };

        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        return findById(keyHolder.getId());
    }

    public void update(Question question) {

        String sql = "UPDATE questions SET title = ?, contents = ? WHERE questionId = ?";
        jdbcTemplate.update(sql, question.getTitle(), question.getContents(), question.getQuestionId());
    }

    public void delete(long questionId) {

        String sql = "DELETE FROM questions WHERE questionId = ?";
        jdbcTemplate.update(sql, questionId);
    }

    public void updateCountOfAnswer(long questionId) {

        String sql = "UPDATE questions set countOfAnswer = countOfAnswer + 1 WHERE questionId = ?";
        jdbcTemplate.update(sql, questionId);
    }

    public List<Question> findAll() {

        String sql = "SELECT questionId, writer, title, createdDate, countOfAnswer FROM questions ORDER BY questionId DESC";

        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"), null,
                        rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));
            }
        };

        return jdbcTemplate.query(sql, rm);
    }

    public Question findById(long questionId) {

        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM questions WHERE questionId = ?";

        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"),
                        rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));
            }
        };

        return jdbcTemplate.queryForObject(sql, rm, questionId);
    }
}
