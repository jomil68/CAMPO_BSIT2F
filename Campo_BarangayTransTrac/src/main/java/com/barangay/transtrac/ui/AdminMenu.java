package com.barangay.transtrac.ui;

import com.barangay.transtrac.dao.UserDao;
import com.barangay.transtrac.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show(User current) {
        while (true) {
            System.out.println("\n===== ADMINISTRATOR MENU =====");
            System.out.println("Logged in as: " + current.getFullName());
            System.out.println("1. Review pending accounts");
            System.out.println("2. View all users");
            System.out.println("3. Resident management   (coming in next deliverable)");
            System.out.println("4. Transaction types     (coming in next deliverable)");
            System.out.println("5. Audit logs            (coming in next deliverable)");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> reviewPending();
                case "2" -> viewAll();
                case "3", "4", "5" -> System.out.println("Coming soon.");
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void reviewPending() {
        List<User> pending = userDao.findByStatus("PENDING");
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
            return;
        }
        for (User u : pending) {
            System.out.printf("ID %d | %s | %s | %s%n",
                    u.getUserId(), u.getUsername(), u.getFullName(), u.getRole().getLabel());
        }
        System.out.print("Enter user ID to review (blank to cancel): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;
        try {
            int id = Integer.parseInt(input);
            System.out.print("Approve (A) or Reject (R)? ");
            String decision = scanner.nextLine().trim().toUpperCase();
            if (decision.equals("A")) {
                System.out.println(userDao.updateStatus(id, "APPROVED") ? "Account approved." : "User not found.");
            } else if (decision.equals("R")) {
                System.out.println(userDao.updateStatus(id, "REJECTED") ? "Account rejected." : "User not found.");
            } else {
                System.out.println("Cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    private void viewAll() {
        System.out.printf("%-4s %-15s %-25s %-14s %-10s%n", "ID", "Username", "Full Name", "Role", "Status");
        for (User u : userDao.findAll()) {
            System.out.printf("%-4d %-15s %-25s %-14s %-10s%n",
                    u.getUserId(), u.getUsername(), u.getFullName(), u.getRole().name(), u.getStatus());
        }
    }
}
