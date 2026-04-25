package main;

import dao.*;
import model.*;
import service.BloodMatchingService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static DonorDAO donorDAO = new DonorDAO();
    static RecipientDAO recipientDAO = new RecipientDAO();
    static BloodInventoryDAO inventoryDAO = new BloodInventoryDAO();
    static TransactionLogDAO logDAO = new TransactionLogDAO();
    static BloodMatchingService matchingService = new BloodMatchingService();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== Blood Bank & Organ Donor System =====");
            System.out.println("1. Register Donor");
            System.out.println("2. View All Donors");
            System.out.println("3. Register Recipient");
            System.out.println("4. View All Recipients");
            System.out.println("5. View Blood Inventory");
            System.out.println("6. Add Blood Units to Inventory");
            System.out.println("7. Find Donors by Blood Group");
            System.out.println("8. Perform Blood Transfusion");
            System.out.println("9. View Transaction Logs");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> registerDonor();
                    case 2 -> viewDonors();
                    case 3 -> registerRecipient();
                    case 4 -> viewRecipients();
                    case 5 -> viewInventory();
                    case 6 -> addBloodUnits();
                    case 7 -> findDonorsByBloodGroup();
                    case 8 -> performTransfusion();
                    case 9 -> viewLogs();
                    case 0 -> System.out.println("Exiting system. Goodbye!");
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    static void registerDonor() throws SQLException {
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Age: "); int age = sc.nextInt(); sc.nextLine();
        System.out.print("Blood Group (e.g. A+): "); String bg = sc.nextLine();
        System.out.print("Contact: "); String contact = sc.nextLine();
        System.out.print("City: "); String city = sc.nextLine();
        System.out.print("Is Organ Donor? (true/false): "); boolean organ = sc.nextBoolean(); sc.nextLine();

        Donor d = new Donor(name, age, bg, contact, city, organ);
        System.out.println(donorDAO.addDonor(d) ? "Donor registered!" : "Registration failed.");
    }

    static void viewDonors() throws SQLException {
        List<Donor> donors = donorDAO.getAllDonors();
        if (donors.isEmpty()) System.out.println("No donors found.");
        else donors.forEach(System.out::println);
    }

    static void registerRecipient() throws SQLException {
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Age: "); int age = sc.nextInt(); sc.nextLine();
        System.out.print("Blood Group: "); String bg = sc.nextLine();
        System.out.print("Contact: "); String contact = sc.nextLine();
        System.out.print("Hospital ID: "); int hid = sc.nextInt(); sc.nextLine();
        System.out.print("Organ Needed (or 'none'): "); String organ = sc.nextLine();
        System.out.print("Urgency (LOW/MEDIUM/HIGH/CRITICAL): "); String urgency = sc.nextLine();

        Recipient r = new Recipient(name, age, bg, contact, hid, organ, urgency);
        System.out.println(recipientDAO.addRecipient(r) ? "Recipient registered!" : "Failed.");
    }

    static void viewRecipients() throws SQLException {
        List<Recipient> list = recipientDAO.getAllRecipients();
        if (list.isEmpty()) System.out.println("No recipients found.");
        else list.forEach(System.out::println);
    }

    static void viewInventory() throws SQLException {
        inventoryDAO.getAllInventory().forEach(i ->
                System.out.printf("Blood Group: %-4s | Units: %d%n",
                        i.getBloodGroup(), i.getUnitsAvailable()));
    }

    static void addBloodUnits() throws SQLException {
        System.out.print("Blood Group: "); String bg = sc.nextLine();
        System.out.print("Units to add: "); int units = sc.nextInt(); sc.nextLine();
        System.out.println(inventoryDAO.addUnits(bg, units) ? "Units added." : "Failed.");
    }

    static void findDonorsByBloodGroup() throws SQLException {
        System.out.print("Enter blood group: "); String bg = sc.nextLine();
        List<Donor> list = matchingService.findMatchingDonors(bg);
        if (list.isEmpty()) System.out.println("No donors found for " + bg);
        else list.forEach(System.out::println);
    }

    static void performTransfusion() throws SQLException {
        System.out.print("Donor ID: "); int did = sc.nextInt();
        System.out.print("Recipient ID: "); int rid = sc.nextInt();
        System.out.print("Hospital ID: "); int hid = sc.nextInt(); sc.nextLine();
        System.out.print("Blood Group: "); String bg = sc.nextLine();
        System.out.print("Units: "); int units = sc.nextInt(); sc.nextLine();
        matchingService.donateBlood(did, bg, units, rid, hid);
    }

    static void viewLogs() throws SQLException {
        List<TransactionLog> logs = logDAO.getAllLogs();
        if (logs.isEmpty()) System.out.println("No transactions found.");
        else logs.forEach(l ->
                System.out.printf("[%d] %s | Donor:%d → Recipient:%d | %s | Units:%d%n",
                        l.getLogId(), l.getTransactionType(), l.getDonorId(),
                        l.getRecipientId(), l.getBloodGroup(), l.getUnits()));
    }
}