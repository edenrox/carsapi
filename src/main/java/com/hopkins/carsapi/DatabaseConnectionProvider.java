package com.hopkins.carsapi;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DatabaseConnectionProvider {
    private static DataSource ds;
    
    private static synchronized void initDataSource() {
        if (ds == null) {
            String database = System.getProperty("carsapi.db.database");
            String user = System.getProperty("carsapi.db.user");
            String pass = System.getProperty("carsapi.db.pass");

            MysqlDataSource mysqlDs = new MysqlDataSource();
            mysqlDs.setServerName("localhost");
            mysqlDs.setPort(3306);
            mysqlDs.setDatabaseName(database);
            mysqlDs.setUser(user);
            mysqlDs.setPassword(pass);
            ds = mysqlDs;
        }
    }
    
    public static Connection connect() throws SQLException {
        initDataSource();
        return ds.getConnection();
    }   
}
