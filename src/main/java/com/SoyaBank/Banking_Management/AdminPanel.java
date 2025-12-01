package com.SoyaBank.Banking_Management;

import com.SoyaBank.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;

@Service
public class AdminPanel {

    @Autowired
    private ConnectionDB connectionDB;
    Scanner sc = new Scanner(System.in);

    public void adminMenu() {

        while (true) {
            System.out.println("\n============== Admin Panel ==============");
            System.out.println("1) View All Customers");
            System.out.println("2) Search Customer by Account No");
            System.out.println("3) Delete Customer");
            System.out.println("4) Update Customer Name");
            System.out.println("5) Reset Customer Password");
            System.out.println("6) View Total Bank Balance");
            System.out.println("7) View Transaction History");
            System.out.println("8) Logout");
            System.out.print("Enter Choice: ");

            int ch = safeIntInput();

            switch (ch) {

                case 1:
                    viewAllCustomers();
                    break;

                case 2:
                    searchCustomer();
                    break;

                case 3:
                    deleteCustomer();
                    break;

                case 4:
                    updateCustomerName();
                    break;

                case 5:
                    resetPassword();
                    break;

                case 6:
                    viewTotalBankBalance();
                    break;

                case 7:
                    viewTransactionHistory();
                    break;

                case 8:
                    System.out.println("Admin logged out.");
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // Safe Integer Input Handling
    private int safeIntInput() {
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input! Enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public void viewAllCustomers() {
        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT acc_no, Holder_Name, balance FROM customer ORDER BY acc_no ASC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("\n------ Customer List ------");

                    boolean hasCustomers = false;
                    while (rs.next()) {
                        hasCustomers = true;
                        System.out.println("A/C: " + rs.getInt("acc_no") +
                                " | Name: " + rs.getString("Holder_Name") +
                                " | Balance: " + rs.getInt("balance"));
                    }
                    
                    if (!hasCustomers) {
                        System.out.println("No customers found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while retrieving customers: " + e.getMessage());
            System.out.println("Failed to retrieve customer list. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to retrieve customer list. Please try again.");
        }
    }

    public void searchCustomer() {
        System.out.print("Enter Account Number: ");
        int ac = safeIntInput();

        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT * FROM customer WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, ac);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nAccount Found:");
                        System.out.println("Name: " + rs.getString("Holder_Name"));
                        System.out.println("Balance: " + rs.getInt("balance"));
                    } else {
                        System.out.println("No customer found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while searching customer: " + e.getMessage());
            System.out.println("Failed to search customer. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to search customer. Please try again.");
        }
    }

    public void deleteCustomer() {
        System.out.print("Enter Account No to Delete: ");
        int ac = safeIntInput();

        try (Connection con = connectionDB.getConnection()) {
            String sql = "DELETE FROM customer WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, ac);

                int rowsDeleted = ps.executeUpdate();
                if (rowsDeleted == 1) {
                    System.out.println("Customer Deleted Successfully!");
                } else {
                    System.out.println("Account Not Found.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while deleting customer: " + e.getMessage());
            System.out.println("Failed to delete customer. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to delete customer. Please try again.");
        }
    }

    public void updateCustomerName() {
        System.out.print("Enter Account No: ");
        int ac = safeIntInput();
        sc.nextLine(); // clear buffer

        System.out.print("Enter New Name: ");
        String newName = sc.nextLine().trim();

        if (newName.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        try (Connection con = connectionDB.getConnection()) {
            String sql = "UPDATE customer SET Holder_Name = ? WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, newName);
                ps.setInt(2, ac);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated == 1) {
                    System.out.println("Name Updated Successfully!");
                } else {
                    System.out.println("Account Not Found!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while updating name: " + e.getMessage());
            System.out.println("Failed to update name. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to update name. Please try again.");
        }
    }

    public void resetPassword() {
        System.out.print("Enter Account No: ");
        int ac = safeIntInput();
        System.out.print("Enter New Password: ");
        int newPass = safeIntInput();

        try (Connection con = connectionDB.getConnection()) {
            String sql = "UPDATE customer SET pass_code = ? WHERE acc_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, newPass);
                ps.setInt(2, ac);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated == 1) {
                    System.out.println("Password Reset Successfully!");
                } else {
                    System.out.println("Account Not Found!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while resetting password: " + e.getMessage());
            System.out.println("Failed to reset password. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to reset password. Please try again.");
        }
    }

    public void viewTotalBankBalance() {
        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT SUM(balance) AS total FROM customer";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long total = rs.getLong("total");
                        System.out.println("\nTotal Balance in Bank: ₹" + (rs.wasNull() ? 0 : total));
                    } else {
                        System.out.println("\nNo customers found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while retrieving total balance: " + e.getMessage());
            System.out.println("Failed to retrieve total balance. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to retrieve total balance. Please try again.");
        }
    }
    public void viewTransactionHistory() {
        try (Connection con = connectionDB.getConnection()) {
            String sql = "SELECT * FROM transactions ORDER BY date_time DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("\n===== Transaction History =====");

                    boolean hasTransactions = false;
                    while (rs.next()) {
                        hasTransactions = true;
                        Integer receiverAcc = rs.getObject("receiver_acc", Integer.class);
                        System.out.println("ID: " + rs.getInt("id") +
                                " | A/C: " + rs.getInt("acc_no") +
                                " | Type: " + rs.getString("type") +
                                " | Amount: ₹" + rs.getInt("amount") +
                                " | Receiver: " + (receiverAcc != null ? receiverAcc : "N/A") +
                                " | Time: " + rs.getTimestamp("date_time"));
                    }

                    if (!hasTransactions) {
                        System.out.println("No transactions found.");
                    }
                    System.out.println("===============================");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while retrieving transaction history: " + e.getMessage());
            System.out.println("Failed to retrieve transaction history. Please try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.out.println("Failed to retrieve transaction history. Please try again.");
        }
    }
}
