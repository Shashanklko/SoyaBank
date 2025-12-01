package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class UpdateBalance {

    @Autowired
    private ConnectionDB connectionDB;

    // Deposit
    public boolean deposit(int accNo, int amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than 0!");
            return false;
        }

        try (Connection con = connectionDB.getConnection()) {
            String sql = "UPDATE customer SET balance = balance + ? WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, amount);
                ps.setInt(2, accNo);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated == 1) {
                    // Track transaction
                    TransactionHistory.addHistory(con, accNo, "deposit", amount, null);
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

    // Withdraw
    public boolean withdraw(int accNo, int amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be greater than 0!");
            return false;
        }

        try (Connection con = connectionDB.getConnection()) {
            // Check balance
            String check = "SELECT balance FROM customer WHERE acc_no = ?";
            try (PreparedStatement ps1 = con.prepareStatement(check)) {
                ps1.setInt(1, accNo);
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        int currentBalance = rs.getInt("balance");
                        if (currentBalance >= amount) {
                            String sql = "UPDATE customer SET balance = balance - ? WHERE acc_no = ?";
                            try (PreparedStatement ps2 = con.prepareStatement(sql)) {
                                ps2.setInt(1, amount);
                                ps2.setInt(2, accNo);

                                if (ps2.executeUpdate() == 1) {
                                    // Track transaction
                                    TransactionHistory.addHistory(con, accNo, "withdraw", amount, null);
                                    return true;
                                } else {
                                    System.out.println("Withdrawal failed. Please try again.");
                                    return false;
                                }
                            }
                        } else {
                            System.out.println("Insufficient Balance! Current balance: " + currentBalance);
                            return false;
                        }
                    } else {
                        System.out.println("Account not found!");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during withdrawal: " + e.getMessage());
            System.out.println("Withdrawal failed. Please try again.");
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error during withdrawal: " + e.getMessage());
            System.out.println("Withdrawal failed. Please try again.");
            return false;
        }
    }
}
