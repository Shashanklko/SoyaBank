package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;

@Service
public class TransferMoney {

    @Autowired
    private ConnectionDB connectionDB;

    public boolean transferMoney(int sender_ac, int receiver_ac, int amount, Scanner sc) {
        if (receiver_ac == 0 || amount <= 0) {
            System.out.println("All fields are required and must be valid!");
            return false;
        }

        if (sender_ac == receiver_ac) {
            System.out.println("Cannot transfer to the same account!");
            return false;
        }

        Connection con = null;
        try {
            con = connectionDB.getConnection();
            // ---------------------------
            // 1. Ask for confirmation BEFORE transaction
            // ---------------------------
            System.out.print("Confirm transfer of " + amount + " to account " + receiver_ac + " (yes/no): ");
            String confirm = sc.nextLine();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Transaction cancelled.");
                return false;
            }

            // ---------------------------
            // 2. Begin transaction
            // ---------------------------
            con.setAutoCommit(false);

            // ---------------------------
            // 3. Lock both accounts in consistent order
            // ---------------------------
            int firstAcc = Math.min(sender_ac, receiver_ac);
            int secondAcc = Math.max(sender_ac, receiver_ac);

            String lockSQL = "SELECT acc_no, balance, Holder_Name FROM customer WHERE acc_no IN (?, ?) FOR UPDATE";

            int senderBalance = 0;
            String receiverName = "";

            try (PreparedStatement psLock = con.prepareStatement(lockSQL)) {
                psLock.setInt(1, firstAcc);
                psLock.setInt(2, secondAcc);

                try (ResultSet rs = psLock.executeQuery()) {
                    boolean senderFound = false;
                    boolean receiverFound = false;

                    while (rs.next()) {
                        int accNo = rs.getInt("acc_no");
                        int balance = rs.getInt("balance");
                        String holderName = rs.getString("Holder_Name");

                        if (accNo == sender_ac) {
                            senderBalance = balance;
                            senderFound = true;
                        } else if (accNo == receiver_ac) {
                            receiverName = holderName;
                            receiverFound = true;
                        }
                    }

                    if (!senderFound) {
                        System.out.println("Sender account not found!");
                        con.rollback();
                        return false;
                    }
                    if (!receiverFound) {
                        System.out.println("Receiver account not found!");
                        con.rollback();
                        return false;
                    }

                    if (senderBalance < amount) {
                        System.out.println("Insufficient balance!");
                        con.rollback();
                        return false;
                    }
                }
            }

            // ---------------------------
            // 4. Execute transfer (debit + credit)
            // ---------------------------
            String debitSQL = "UPDATE customer SET balance = balance - ? WHERE acc_no = ?";
            String creditSQL = "UPDATE customer SET balance = balance + ? WHERE acc_no = ?";

            try (PreparedStatement psDebit = con.prepareStatement(debitSQL);
                 PreparedStatement psCredit = con.prepareStatement(creditSQL)) {

                psDebit.setInt(1, amount);
                psDebit.setInt(2, sender_ac);
                psDebit.executeUpdate();

                psCredit.setInt(1, amount);
                psCredit.setInt(2, receiver_ac);
                psCredit.executeUpdate();
            }

            // ---------------------------
            // 5. Log transaction history (using same connection/transaction)
            // ---------------------------
            TransactionHistory.addHistory(con, sender_ac, "transfer-out", amount, receiver_ac);
            TransactionHistory.addHistory(con, receiver_ac, "transfer-in", amount, sender_ac);

            // ---------------------------
            // 6. Commit transaction
            // ---------------------------
            con.commit();
            System.out.println("Amount transferred successfully to " + receiverName + "!");
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Transaction failed and has been rolled back.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Database error during transfer: " + e.getMessage());
            System.out.println("Transfer failed. Please try again.");
            return false;
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Transaction failed and has been rolled back.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Unexpected error during transfer: " + e.getMessage());
            System.out.println("Transfer failed. Please try again.");
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
