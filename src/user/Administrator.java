package user;

import java.util.Scanner;
import java.util.UUID;
import java.sql.SQLException;
import java.util.List;
import entities.Offering;
import system.OfferingDAO;

public class Administrator extends User {

    public Administrator(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getRole() {
        return "Administrator";
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. Create new offering");
            System.out.println("2. View all offerings");
            System.out.println("3. Delete offering");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createNewOffering(scanner);
                    break;
                case "2":
                    viewAllOfferings();
                    break;
                case "3":
                    deleteOffering(scanner);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void createNewOffering(Scanner scanner) {
        System.out.println("Enter offering details:");

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Lesson Type: ");
        String lessonType = scanner.nextLine();

        System.out.print("Is Private? (yes/no): ");
        boolean isPrivate = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("Start Time (HH:MM): ");
        String startTime = scanner.nextLine();

        System.out.print("End Time (HH:MM): ");
        String endTime = scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        Offering offering = new Offering(location, lessonType, isPrivate, startTime, endTime, date);

        try {
            OfferingDAO.saveOffering(offering);
            System.out.println("Offering created with ID: " + offering.getId());
        } catch (SQLException e) {
            System.err.println("Failed to save offering: " + e.getMessage());
        }
    }

    private void viewAllOfferings() {
        try {
            List<Offering> offerings = OfferingDAO.getAllOfferings();
            System.out.println("\nAll Offerings:");
            for (Offering offering : offerings) {
                System.out.println(offering);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve offerings: " + e.getMessage());
        }
    }

    private void deleteOffering(Scanner scanner) {
        System.out.print("Enter Offering ID to delete: ");
        String offeringId = scanner.nextLine();
        try {
            UUID id = UUID.fromString(offeringId);
            boolean success = OfferingDAO.deleteOffering(id);
            if (success) {
                System.out.println("Offering deleted.");
            } else {
                System.out.println("Offering not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        } catch (SQLException e) {
            System.err.println("Failed to delete offering: " + e.getMessage());
        }
    }
}
