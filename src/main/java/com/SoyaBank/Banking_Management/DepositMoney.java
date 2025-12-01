package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class DepositMoney {

    @Autowired
    private ConnectionDB connectionDB;

    public boolean deposit(int accNo, int amount) {

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0");
            return false;
        }

        try (Connection con = connectionDB.getConnection()) {
            String sql = "UPDATE customer SET balance = balance + ? WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, amount);
                ps.setInt(2, accNo);

                int rows = ps.executeUpdate();
                if (rows == 1) {
                    System.out.println("Amount deposited successfully!");
                    return true;
                } else {
                    System.out.println("Account not found or deposit failed!");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during deposit: " + e.getMessage());
            System.out.println("Deposit failed. Please try again.");
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error during deposit: " + e.getMessage());
            System.out.println("Deposit failed. Please try again.");
            return false;
        }
    }
}
