package com.SoyaBank;

import com.SoyaBank.Banking_Management.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Bank implements CommandLineRunner {

    @Autowired
    private CreateAccount createAccount;

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private AdminLogin adminLogin;

    @Autowired
    private AdminPanel adminPanel;

    public static void main(String[] args) {
        SpringApplication.run(Bank.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        String name;
        int initialBalance;
        int pass_code;
        int ch;

        while (true) {
            System.out.println("\n===============================");
            System.out.println(" Welcome to SoyaBank ");
            System.out.println("===============================");
            System.out.println("1) Create Account");
            System.out.println("2) Login Account");
            System.out.println("3) Admin Login");
            System.out.println("4) Exit");

            try {
                System.out.print("\nEnter Choice: ");
                ch = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (ch) {

                    // -------------------- CREATE ACCOUNT --------------------
                    case 1: {
                        System.out.print("Enter Unique Username: ");
                        name = sc.nextLine();

                        System.out.print("Enter Initial Balance: ");
                        initialBalance = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Password: ");
                        pass_code = sc.nextInt();
                        sc.nextLine();

                        if (createAccount.createAccount(name, pass_code, initialBalance)) {
                            System.out.println("You can now login from the main menu.");
                        }
                        break;
                    }

                    // -------------------- LOGIN ACCOUNT ---------------------
                    case 2: {
                        System.out.print("Enter Username: ");
                        name = sc.nextLine();

                        System.out.print("Enter Password: ");
                        pass_code = sc.nextInt();
                        sc.nextLine();

                        if (!loginAccount.loginAccount(name, pass_code, sc)) {
                            System.out.println("Login failed. Try again.");
                        }
                        break;
                    }
                    case 3:
                        if (adminLogin.adminLogin()) {
                            adminPanel.adminMenu();
                        }
                        break;

                    // -------------------- EXIT PROGRAM ----------------------
                    case 4: {
                        System.out.println("Thank you for using SoyaBank! Goodbye.");
                        sc.close();
                        System.exit(0);
                        break;
                    }

                    // -------------------- INVALID INPUT ---------------------
                    default:
                        System.out.println("Invalid input! Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Please enter a valid input!");
                sc.nextLine(); // Clear bad input
            }
        }
    }
}
