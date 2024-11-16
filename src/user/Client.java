package user;

import entities.Offering;
import system.BookingDAO;

import java.awt.print.Book;
import java.sql.SQLException;
import java.util.UUID;
import java.util.Scanner;
import java.util.List;
import entities.Booking;
import system.OfferingDAO;

public class Client extends User {

    private int age;

    private String guardianName;

    public int getAge() {
        return age;
    }

    public String getGuardianName() {
        return guardianName;
    }


    public String getRole() {
        return "Client";
    }

    public Client(UUID id, String name, int age, String guardianName) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.guardianName = guardianName;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\nClient Menu:");
            System.out.println("1. View available offerings");
            System.out.println("2. Book offering");
            System.out.println("3. View bookings");
            System.out.println("4. Cancel booking");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAvailableOfferings();
                    break;
                case "2":
                    bookOffering(scanner);
                    break;
                case "3":
                    viewBookings();
                    break;
                case "4":
                    cancelBooking(scanner);
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

    }

    private void viewBookings() {
        try {
            List<Booking> bookings = BookingDAO.getBookingsForClient(this.id);
            System.out.println("\nYour Bookings:");
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve bookings: " + e.getMessage());
        }
    }

    private void bookOffering(Scanner scanner) {
        try {
            System.out.print("Enter the ID of the offering you want to book: ");
            String offeringId = scanner.nextLine();
            Offering offering = OfferingDAO.getOfferingById(UUID.fromString(offeringId));
            if (offering == null) {
                System.out.println("Offering not found.");
                return;
            }
            offering.setStatus("Unavailable");
            OfferingDAO.updateOffering(offering);
            Booking booking = new Booking(UUID.randomUUID(), offering, this);
            List<Booking> bookings = BookingDAO.getBookingsForClient(this.id);
            for (Booking b : bookings) {
                if (b.getOffering().getStartTime().equals(booking.getOffering().getStartTime()) &&
                        b.getOffering().getEndTime().equals(booking.getOffering().getEndTime()) &&
                        b.getOffering().getDate().equals(booking.getOffering().getDate())) {
                    System.out.println("You already have a booking at this time.");
                    offering.setStatus("Taken");
                    OfferingDAO.updateOffering(offering);
                    return;
                }
            }
            BookingDAO.saveBooking(booking);
            System.out.println("Offering booked successfully.");
        } catch (Exception e) {
            System.err.println("Failed to book offering: " + e.getMessage());
        }
    }

    private void viewAvailableOfferings() {
        try {
            Offering[] offerings = OfferingDAO.getOfferingsForClient();
            System.out.println("\nAvailable Offerings:");
            for (Offering offering : offerings) {
                System.out.println(offering);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve offerings: " + e.getMessage());
        }
    }

    private void cancelBooking(Scanner scanner) {
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

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", guardianName='" + (guardianName == null ? "N/A" : guardianName) + '\'' +
                '}';
    }
}
