package com.hopkins.carsapi;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DatabaseConnectionProvider {
    private static DataSource ds;
    
    public static synchronized void initDataSource(Config config) {
        MysqlDataSource mysqlDs = new MysqlDataSource();
        mysqlDs.setServerName(config.getDbServer());
        mysqlDs.setPort(config.getDbPort());
        mysqlDs.setDatabaseName(config.getDbDatabase());
        mysqlDs.setUser(config.getDbUsername());
        mysqlDs.setPassword(config.getDbPassword());
        ds = mysqlDs;
    }
    
    public static Connection connect() throws SQLException {
        return ds.getConnection();
    }   
}
