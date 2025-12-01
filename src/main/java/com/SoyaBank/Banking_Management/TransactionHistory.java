package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class TransactionHistory {

    @Autowired
    private ConnectionDB connectionDB;

    // Static method that accepts a connection (for use within transactions)
    public static void addHistory(Connection connection, int accNo, String type, int amount, Integer receiverAcc) throws SQLException {
        String sql = "INSERT INTO transactions(acc_no, type, amount, receiver_acc) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, accNo);
            ps.setString(2, type);
            ps.setInt(3, amount);

            if (receiverAcc == null)
                ps.setNull(4, Types.INTEGER);
            else
                ps.setInt(4, receiverAcc);

            ps.executeUpdate();
        }
    }

    // Instance method for backward compatibility
    public void addHistory(int accNo, String type, int amount, Integer receiverAcc) {
        try (Connection con = connectionDB.getConnection()) {
            addHistory(con, accNo, type, amount, receiverAcc);
        } catch (SQLException e) {
            System.err.println("Error adding transaction history: " + e.getMessage());
            // Don't throw exception as this is just logging
        }
    }
}
