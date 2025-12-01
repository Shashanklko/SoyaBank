package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;

@Service
public class LoginAccount {

    @Autowired
    private ConnectionDB connectionDB;
    
    @Autowired
    private UpdateBalance updateBalance;
    
    @Autowired
    private TransferMoney transferMoney;
    
    @Autowired
    private GetBalance getBalance;
    
    @Autowired
    private ViewAccountDetails viewAccountDetails;

    public boolean loginAccount(String name, int passCode, Scanner sc) {

        if (name == null || name.trim().isEmpty() || passCode == 0) {
            System.out.println("All fields are required!");
            return false;
        }

        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT * FROM customer WHERE Holder_Name = ? AND pass_code = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                ps.setInt(2, passCode);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int accNo = rs.getInt("acc_no");

                        while (true) {
                            System.out.println("\n==============================");
                            System.out.println(" Welcome, " + rs.getString("Holder_Name"));
                            System.out.println("==============================");
                            System.out.println("1) Deposit Money");
                            System.out.println("2) Withdraw Money");
                            System.out.println("3) Transfer Money");
                            System.out.println("4) View Balance");
                            System.out.println("5) View Account Details");
                            System.out.println("6) Logout");
                            System.out.print("Enter Choice: ");
                            int ch = sc.nextInt();
                            sc.nextLine(); // consume newline

                            switch (ch) {
                                case 1:
                                    System.out.print("Enter Deposit Amount: ");
                                    int depAmount = sc.nextInt();
                                    sc.nextLine();
                                    if (updateBalance.deposit(accNo, depAmount)) {
                                        System.out.println("Amount Deposited Successfully!");
                                    } else {
                                        System.out.println("Deposit failed!");
                                    }
                                    break;

                                case 2:
                                    System.out.print("Enter Withdrawal Amount: ");
                                    int wAmount = sc.nextInt();
                                    sc.nextLine();
                                    if (updateBalance.withdraw(accNo, wAmount)) {
                                        System.out.println("Amount Withdrawn Successfully!");
                                    } else {
                                        System.out.println("Withdrawal failed!");
                                    }
                                    break;

                                case 3:
                                    System.out.print("Enter Receiver A/c No: ");
                                    int receiverAc = sc.nextInt();
                                    sc.nextLine();
                                    System.out.print("Enter Amount: ");
                                    int amt = sc.nextInt();
                                    sc.nextLine();

                                    if (transferMoney.transferMoney(accNo, receiverAc, amt, sc)) {
                                        System.out.println("Transaction Successful!");
                                    } else {
                                        System.out.println("Transaction Failed!");
                                    }
                                    break;

                                case 4:
                                    getBalance.getBalance(accNo);
                                    break;

                                case 5:
                                    viewAccountDetails.viewDetails(accNo, sc);
                                    break;

                                case 6:
                                    System.out.println("Logging out…");
                                    return true;

                                default:
                                    System.out.println("Invalid choice! Try again.");
                            }
                        }
                    } else {
                        System.out.println("Invalid username or password!");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            System.out.println("Login failed. Please try again later.");
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            System.out.println("Login failed. Please try again.");
            return false;
        }
    }
}
