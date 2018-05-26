package io.devfactory.next.dao;

import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.Repository;
import io.devfactory.core.jdbc.JdbcTemplate;
import io.devfactory.core.jdbc.KeyHolder;
import io.devfactory.core.jdbc.PreparedStatementCreator;
import io.devfactory.core.jdbc.RowMapper;
import io.devfactory.next.model.Answer;

import java.sql.*;
import java.util.List;

@Repository
public class JdbcAnswerDao implements AnswerDao {

    private JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcAnswerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Answer insert(Answer answer) {

        String sql = "INSERT INTO answers (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, answer.getWriter());
                pstmt.setString(2, answer.getContents());
                pstmt.setTimestamp(3, new Timestamp(answer.getTimeFromCreateDate()));
                pstmt.setLong(4, answer.getQuestionId());

                return pstmt;
            }
        };

        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        return findById(keyHolder.getId());
    }

    public void delete(Long answerId) {

        String sql = "DELETE FROM answers WHERE answerId = ?";
        jdbcTemplate.update(sql, answerId);
    }

    public Answer findById(long answerId) {

        String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM answers WHERE answerId = ?";

        RowMapper<Answer> rm = new RowMapper<Answer>() {
            @Override
            public Answer mapRow(ResultSet rs) throws SQLException {
                return new Answer(rs.getLong("answerId"), rs.getString("writer"), rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getLong("questionId"));
            }
        };

        return jdbcTemplate.queryForObject(sql, rm, answerId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {

        String sql = "SELECT answerId, writer, contents, createdDate FROM answers WHERE questionId = ? ORDER BY answerId DESC";

        RowMapper<Answer> rm = new RowMapper<Answer>() {
            @Override
            public Answer mapRow(ResultSet rs) throws SQLException {
                return new Answer(rs.getLong("answerId"), rs.getString("writer"), rs.getString("contents"), rs.getTimestamp("createdDate"), questionId);
            }
        };

        return jdbcTemplate.query(sql, rm, questionId);
    }
}
