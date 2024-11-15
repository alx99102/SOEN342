package system;

import entities.Availability;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InstructorAvailabilityDAO {
    public static void addAvailability(UUID instructor, String city) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();

        String sql = "INSERT INTO availability (id, instructor, city) VALUES (?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UUID.randomUUID().toString());
        stmt.setString(2, instructor.toString());
        stmt.setString(3, city);

        stmt.executeUpdate();
    }

    public static Availability[] getForInstructor(UUID instructor) throws  SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT id, instructor, city FROM availability WHERE instructor = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, instructor.toString());

        ResultSet rs = stmt.executeQuery();
        List<Availability> availabilities = new ArrayList<>();
        while (rs.next()) {
            Availability availability = new Availability(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("instructor")),
                    rs.getString("city")
            );
            availabilities.add(availability);
        }

        return availabilities.toArray(new Availability[0]);
    }



    public static Availability[] getForCity(String city) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT instructor FROM availability WHERE city = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, city);

        ResultSet rs = stmt.executeQuery();
        List<Availability> availabilities = new ArrayList<>();
        while (rs.next()) {
            Availability availability = new Availability(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("instructor")),
                    rs.getString("city")
            );
            availabilities.add(availability);
        }
        return availabilities.toArray(new Availability[0]);
    }
}
