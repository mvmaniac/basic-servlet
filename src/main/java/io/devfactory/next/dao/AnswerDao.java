package io.devfactory.next.dao;

import io.devfactory.core.jdbc.JdbcTemplate;
import io.devfactory.core.jdbc.KeyHolder;
import io.devfactory.core.jdbc.PreparedStatementCreator;
import io.devfactory.core.jdbc.RowMapper;
import io.devfactory.next.model.Answer;

import java.sql.*;
import java.util.List;

public class AnswerDao {

    public Answer insert(Answer answer) {

        String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement pstmt = con.prepareStatement(sql);

                pstmt.setString(1, answer.getWriter());
                pstmt.setString(2, answer.getContents());
                pstmt.setTimestamp(3, new Timestamp(answer.getTimeFromCreateDate()));
                pstmt.setLong(4, answer.getQuestionId());

                return pstmt;
            }
        };

        KeyHolder keyHolder = new KeyHolder();

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(psc, keyHolder);

        return findById(keyHolder.getId());
    }

    public Answer findById(long answerId) {

        String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE answerId = ?";

        RowMapper<Answer> rm = new RowMapper<Answer>() {
            @Override
            public Answer mapRow(ResultSet rs) throws SQLException {
                return new Answer(rs.getLong("answerId"), rs.getString("writer"), rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getLong("questionId"));
            }
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.queryForObject(sql, rm, answerId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {

        String sql = "SELECT answerId, writer, contents, createdDate FROM ANSWERS WHERE questionId = ? ORDER BY answerId DESC";

        RowMapper<Answer> rm = new RowMapper<Answer>() {
            @Override
            public Answer mapRow(ResultSet rs) throws SQLException {
                return new Answer(rs.getLong("answerId"), rs.getString("writer"), rs.getString("contents"), rs.getTimestamp("createdDate"), questionId);
            }
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(sql, rm, questionId);
    }

    public void delete(Long answerId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "DELETE FROM ANSWERS WHERE answerId = ?";
        jdbcTemplate.update(sql, answerId);
    }
}
