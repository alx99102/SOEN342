package user;

import java.util.Scanner;
import java.util.UUID;
import java.sql.SQLException;

import entities.Availability;
import entities.Offering;
import system.InstructorAvailabilityDAO;
import system.OfferingDAO;

public class Instructor extends User {
    private String phoneNumber;
    private String specialization;

    private String[] cities;

    public Instructor(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nInstructor Menu:");
            System.out.println("1. View available offerings");
            System.out.println("2. Select offering");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAvailableOfferings();
                    break;
                case "2":
                    selectOffering(scanner);
                    break;
                case "3":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void viewAvailableOfferings() {
        try {
            Offering[] offerings = OfferingDAO.getOfferingsForInstructor(this.cities);
            System.out.println("\nAvailable Offerings:");
            for (Offering offering : offerings) {
                System.out.println(offering);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve offerings: " + e.getMessage());
        }
    }

    private void selectOffering(Scanner scanner) {
        System.out.print("Enter Offering ID to select: ");
        String offeringId = scanner.nextLine();
        try {
            UUID id = UUID.fromString(offeringId);
            Offering offering = OfferingDAO.getOfferingById(id);
            Availability[] availabilities = InstructorAvailabilityDAO.getForInstructor(this.id);

            if (offering != null && !offering.getStatus().equals("Taken")) {
                // Constraint: “The city associated with an offering must be one the city’s that the instructor has
                // indicated in their availabilities.”
                boolean isAvailable = false;
                for (Availability availability : availabilities) {
                    if (availability.getCity().equals(offering.getLocation())) {
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable) {
                    System.out.println("Instructor not available in this city.");
                    return;
                }
                offering.setStatus("Taken");
                OfferingDAO.updateOffering(offering);
                System.out.println("Offering selected.");
            } else {
                System.out.println("Offering not found or already taken.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        } catch (SQLException e) {
            System.err.println("Failed to update offering: " + e.getMessage());
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setCities(String[] cities) {
        this.cities = cities;
    }

    public String[] getCities() {
        return cities;
    }
}
