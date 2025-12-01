package com.SoyaBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionDB {
    
    @Autowired
    private DataSource dataSource;
    
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            if (connection == null) {
                throw new SQLException("Failed to establish database connection: connection is null");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            throw new SQLException("Unable to connect to database. Please check your database configuration.", e);
        }
    }
}
