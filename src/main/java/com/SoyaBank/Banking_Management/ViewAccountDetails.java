package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;

@Service
public class ViewAccountDetails {

    @Autowired
    private ConnectionDB connectionDB;

    // Display account details and optionally reset password
    public void viewDetails(int accNo, Scanner sc) {
        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT acc_no, Holder_Name, balance FROM customer WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, accNo);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("\n=========== Account Details ===========");

                    if (rs.next()) {
                        System.out.println("Account Number : " + rs.getInt("acc_no"));
                        System.out.println("Holder Name    : " + rs.getString("Holder_Name"));
                        System.out.println("Balance        : " + rs.getInt("balance"));
                    } else {
                        System.out.println("Account not found!");
                        return;
                    }

                    System.out.println("======================================");

                    // Offer password reset option
                    System.out.print("\nDo you want to reset your password? (y/n): ");
                    String ch = sc.nextLine();

                    if (ch.equalsIgnoreCase("y")) {
                        resetPassword(accNo, sc);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while retrieving account details: " + e.getMessage());
            System.out.println("Failed to retrieve account details. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to retrieve account details. Please try again.");
        }
    }

    // Reset password securely
    public void resetPassword(int accNo, Scanner sc) {
        System.out.print("Enter new password: ");
        int newPass = sc.nextInt();
        sc.nextLine(); // consume leftover newline

        try (Connection con = connectionDB.getConnection()) {
            String sql = "UPDATE customer SET pass_code = ? WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, newPass);
                ps.setInt(2, accNo);

                int rows = ps.executeUpdate();
                if (rows == 1) {
                    System.out.println("Password updated successfully!");
                } else {
                    System.out.println("Account not found. Password update failed.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while updating password: " + e.getMessage());
            System.out.println("Password update failed. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Password update failed. Please try again.");
        }
    }
}
