package com.SoyaBank.Banking_Management;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class AdminLogin {

    private final String ADMIN_USER = "admin";
    private final String ADMIN_PASS = "1234";

    public boolean adminLogin() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n====== Admin Login ======");
        System.out.print("Enter Admin Username: ");
        String user = sc.nextLine();

        System.out.print("Enter Admin Password: ");
        String pass = sc.nextLine();

        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            System.out.println("Admin Login Successful!");
            return true;
        } else {
            System.out.println("Invalid Admin Credentials!");
            return false;
        }
    }
}
