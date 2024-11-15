import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import system.InstructorAvailabilityDAO;
import user.*;
import java.sql.SQLException;
import system.DatabaseHelper;
import system.UserDAO;

public class Main {
    public static void main(String[] args) {
        User user = null;
        Scanner scanner = new Scanner(System.in);

        try {
            DatabaseHelper.initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\nPlease enter your user ID to sign in, or 'new' to create a new user.");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("new")) {
                System.out.println("Enter the role for your new user: \n" +
                        "'A': Administrator\n" +
                        "'I': Instructor\n" +
                        "'C': Client");
                String roleInput = scanner.nextLine();
                char role = roleInput.charAt(0);

                switch (role) {
                    case 'A':
                        if(Administrator.hasInstance() || UserDAO.adminExists()) {
                            System.out.println("An administrator account already exists.");
                            break;
                        }
                        user = Administrator.getInstance();
                        try {
                            UserDAO.saveUser(user);
                            System.out.println("Administrator account created with ID: " + user.getId());
                        } catch (SQLException e) {
                            System.err.println("Failed to save user: " + e.getMessage());
                        }
                        user.run();
                        break;

                    case 'I':
                        System.out.println("Enter your name:");
                        String instructorName = scanner.nextLine();
                        user = new Instructor(UUID.randomUUID(), instructorName);
                        System.out.println("Enter your phone number:");
                        String phoneNumber = scanner.nextLine();
                        ((Instructor)user).setPhoneNumber(phoneNumber);
                        System.out.println("Enter your specialization:");
                        String specialization = scanner.nextLine();
                        ((Instructor)user).setSpecialization(specialization);

                        System.out.println("Enter cities you are available to work in, seperated by a comma. Ex: 'Montreal,Laval'");
                        String[] availabilities = scanner.nextLine().replaceAll("[^a-zA-Z,-]","").split(",");
                        ((Instructor)user).setCities(availabilities);

                        try {
                            UserDAO.saveUser(user);

                            for (String city: availabilities) {
                                InstructorAvailabilityDAO.addAvailability(user.getId(), city);
                            }

                            System.out.println("Instructor account created with ID: " + user.getId());
                        } catch (SQLException e) {
                            System.err.println("Failed to save user: " + e.getMessage());
                        }
                        user.run();
                        break;

                    case 'C':
                        System.out.println("Enter your name:");
                        String clientName = scanner.nextLine();

                        System.out.print("Enter your age: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        String guardianName = null;
                        if (age < 18) {
                            System.out.print("Enter the name of the parent or guardian making the account: ");
                            guardianName = scanner.nextLine();
                        }

                        user = new Client(UUID.randomUUID(), clientName, age, guardianName);
                        try {
                            UserDAO.saveUser(user);
                            System.out.println("Client account created with ID: " + user.getId());
                        } catch (SQLException e) {
                            System.err.println("Failed to save user: " + e.getMessage());
                        }
                        user.run();
                        break;

                    default:
                        System.out.println("Invalid role code. Please try again.");
                        break;
                }
            } else {
                try {
                    UUID userId = UUID.fromString(input);
                    user = UserDAO.getUserById(userId);
                    if (user != null) {
                        System.out.println("Welcome back, " + user.getName());
                        user.run();
                    } else {
                        System.out.println("User not found. Please try again.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid UUID format. Please try again.");
                } catch (SQLException e) {
                    System.err.println("Failed to retrieve user: " + e.getMessage());
                }
            }
        }
    }
}
