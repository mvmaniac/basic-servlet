package io.devfactory.core.di.factory.example;

import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcUserRepository implements UserRepository {

    @Inject
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }
}
