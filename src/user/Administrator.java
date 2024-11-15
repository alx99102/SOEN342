package user;

import java.util.Scanner;
import java.util.UUID;
import java.sql.SQLException;
import java.util.List;

import entities.Booking;
import entities.Offering;
import system.BookingDAO;
import system.OfferingDAO;
import system.UserDAO;

public class Administrator extends User {
    private Administrator() {
        this.name = "Admin";
        this.id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    public static Administrator getInstance() {
        if(instance == null) {
            instance = new Administrator();
        }
        return instance;
    }

    private static Administrator instance;

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
            System.out.println("4. View all bookings");
            System.out.println("5. Delete booking");
            System.out.println("6. View all users");
            System.out.println("7. Delete user");
            System.out.println("8. Logout");
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
                    viewAllBookings();
                    break;
                case "5":
                    deleteBooking(scanner);
                    break;
                case "6":
                    viewUsers();
                    break;
                case "7":
                    deleteUser(scanner);
                    break;
                case "8":
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

        System.out.print("Room: ");
        String room = scanner.nextLine();

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

        Offering offering = new Offering(location, room, lessonType, isPrivate, startTime, endTime, date);

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

    private void viewAllBookings() {
        try {
            List<Booking> bookings = BookingDAO.getAllBookings();
            System.out.println("\nAll Bookings:");
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve bookings: " + e.getMessage());
        }
    }

    private void deleteBooking(Scanner scanner) {
        try {
            System.out.print("Enter the ID of the booking you want to cancel: ");
            String bookingId = scanner.nextLine();
            UUID id = UUID.fromString(bookingId);
            Booking booking = BookingDAO.getBookingById(id);
            if (booking != null) {
                Offering offering = booking.getOffering();
                offering.setStatus("Taken");
                OfferingDAO.updateOffering(offering);
                BookingDAO.deleteBooking(id);
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Booking not found.");
            }
        } catch (Exception e) {
            System.err.println("Failed to cancel booking: " + e.getMessage());
        }
    }

    private void viewUsers() {
        try {
            List<User> users = UserDAO.getAllUsers();
            System.out.println("\nAll Users:");
            for (User user : users) {
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve users: " + e.getMessage());
        }
    }

    private void deleteUser(Scanner scanner) {
        System.out.print("Enter User ID to delete: ");
        String userId = scanner.nextLine();
        try {
            UUID id = UUID.fromString(userId);
            UserDAO.deleteUser(id);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        } catch (SQLException e) {
            System.err.println("Failed to delete user: " + e.getMessage());
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
