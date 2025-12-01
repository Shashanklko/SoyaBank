package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class CreateAccount {

    @Autowired
    private ConnectionDB connectionDB;

    // Create Account
    public boolean createAccount(String name, int passCode, int initialBalance) {

        if (name == null || name.trim().isEmpty() || passCode == 0 || initialBalance < 0) {
            System.out.println("All fields are required and must be valid!");
            return false;
        }

        try (Connection con = connectionDB.getConnection()) {
            String sql = "INSERT INTO customer(Holder_Name, balance, pass_code) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                ps.setInt(2, initialBalance);
                ps.setInt(3, passCode);

                int rows = ps.executeUpdate();
                if (rows == 1) {
                    System.out.println("Account created successfully!");
                    return true;
                } else {
                    System.out.println("Failed to create account. Please try again.");
                    return false;
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists! Please try another one.");
            return false;
        } catch (SQLException e) {
            System.err.println("Database error while creating account: " + e.getMessage());
            System.out.println("Account creation failed. Please try again later.");
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Account creation failed. Please try again.");
            return false;
        }
    }
}
