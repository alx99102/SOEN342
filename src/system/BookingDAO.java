package system;

import entities.Booking;

import java.awt.print.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




public class BookingDAO {
    public static void saveBooking(Booking booking) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO booking (id, offering, client) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, booking.getId().toString());
        pstmt.setString(2, booking.getOffering().getId().toString());
        pstmt.setString(3, booking.getClient().getId().toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static List<Booking> getBookingsForClient(UUID clientId) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM booking WHERE client = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, clientId.toString());
        ResultSet rs = pstmt.executeQuery();

        List<Booking> bookings = new ArrayList<>();

        while (rs.next()) {
            Booking booking = new Booking(
                UUID.fromString(rs.getString("id")),
                OfferingDAO.getOfferingById(UUID.fromString(rs.getString("offering"))),
                UserDAO.getUserById(UUID.fromString(rs.getString("client")))
            );
            bookings.add(booking);
        }

        rs.close();
        pstmt.close();

        return bookings;
    }

    public static List<Booking> getAllBookings() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM booking";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Booking> bookings = new ArrayList<>();

        while (rs.next()) {
            Booking booking = new Booking(
                UUID.fromString(rs.getString("id")),
                OfferingDAO.getOfferingById(UUID.fromString(rs.getString("offering"))),
                UserDAO.getUserById(UUID.fromString(rs.getString("client")))
            );
            bookings.add(booking);
        }

        rs.close();
        stmt.close();

        return bookings;
    }

    public static void deleteBooking(UUID bookingId) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE FROM booking WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bookingId.toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static Booking getBookingById(UUID id) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM booking WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Booking booking = new Booking(
                UUID.fromString(rs.getString("id")),
                OfferingDAO.getOfferingById(UUID.fromString(rs.getString("offering"))),
                UserDAO.getUserById(UUID.fromString(rs.getString("client")))
            );
            rs.close();
            pstmt.close();
            return booking;
        }

        rs.close();
        pstmt.close();

        return null; // Booking not found
    }
}
