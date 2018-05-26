package io.devfactory.next.config;

import io.devfactory.core.annotation.Bean;
import io.devfactory.core.annotation.ComponentScan;
import io.devfactory.core.annotation.Configuration;
import io.devfactory.core.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan({ "io.devfactory.next", "io.devfactory.core" })
public class MyConfiguration {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/basic_servlet?useSSL=false";
    private static final String DB_USERNAME = "basic";
    private static final String DB_PW = "basic";

    @Bean
    public DataSource dataSource() {

        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);

        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}