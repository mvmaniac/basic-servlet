package io.devfactory.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcTemplate() {}

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, Object... parameters) {
        update(sql, createPreparedStatementSetter(parameters));
    }

    public void update(String sql, PreparedStatementSetter pss) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.setValues(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(PreparedStatementCreator psc, KeyHolder holder) {

        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = psc.createPreparedStatement(conn);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                holder.setId(rs.getLong(1));
            }

            rs.close();

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return query(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {

        ResultSet rs = null;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.setValues(pstmt);
            rs = pstmt.executeQuery();

            List<T> result = new ArrayList<>();

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;

        } catch (SQLException e) {
            throw new DataAccessException();

        } finally {
            try {
                if (rs != null) { rs.close(); }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForObject(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {

        List<T> result = query(sql, rowMapper, pss);

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object[] parameters) {

        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (int i = 0, length = parameters.length; i < length; i++) {
                    pstmt.setObject(i + 1, parameters[i]);
                }
            }
        };
    }
}
