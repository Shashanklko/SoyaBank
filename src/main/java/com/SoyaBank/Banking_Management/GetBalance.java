package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;

@Service
public class GetBalance {

    @Autowired
    private ConnectionDB connectionDB;

   // just shows balance
    public void getBalance(int accNo) {
        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT acc_no, Holder_Name, balance FROM customer WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, accNo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nAccount Number: " + rs.getInt("acc_no"));
                        System.out.println("Name: " + rs.getString("Holder_Name"));
                        System.out.println("Balance: " + rs.getInt("balance"));
                    } else {
                        System.out.println("Account not found!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while retrieving balance: " + e.getMessage());
            System.out.println("Failed to retrieve balance. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to retrieve balance. Please try again.");
        }
    }
}
