import java.util.Scanner;
import java.util.UUID;
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
                        "'I': Instructor");
                String roleInput = scanner.nextLine();
                char role = roleInput.charAt(0);

                switch (role) {
                    case 'A':
                        System.out.println("Enter your name:");
                        String adminName = scanner.nextLine();
                        user = new Administrator(UUID.randomUUID(), adminName);
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
                        Instructor instructor = new Instructor(UUID.randomUUID(), instructorName);
                        System.out.println("Enter your phone number:");
                        String phoneNumber = scanner.nextLine();
                        instructor.setPhoneNumber(phoneNumber);
                        System.out.println("Enter your specialization:");
                        String specialization = scanner.nextLine();
                        instructor.setSpecialization(specialization);
                        try {
                            UserDAO.saveUser(instructor);
                            System.out.println("Instructor account created with ID: " + instructor.getId());
                        } catch (SQLException e) {
                            System.err.println("Failed to save user: " + e.getMessage());
                        }
                        instructor.run();
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
