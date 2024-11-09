package system;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import entities.Offering;

public class OfferingDAO {

    public static void saveOffering(Offering offering) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO offerings (id, location, lessonType, isPrivate, startTime, endTime, date, status, availToPublic) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, offering.getId().toString());
        pstmt.setString(2, offering.getLocation());
        pstmt.setString(3, offering.getLessonType());
        pstmt.setInt(4, offering.isPrivate() ? 1 : 0);
        pstmt.setString(5, offering.getStartTime());
        pstmt.setString(6, offering.getEndTime());
        pstmt.setString(7, offering.getDate());
        pstmt.setString(8, offering.getStatus());
        pstmt.setInt(9, offering.isAvailToPublic() ? 1 : 0);

        pstmt.executeUpdate();
        pstmt.close();
    }

    public static List<Offering> getAllOfferings() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM offerings";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Offering> offerings = new ArrayList<>();

        while (rs.next()) {
            Offering offering = new Offering(
                    rs.getString("id"),
                    rs.getString("location"),
                    rs.getString("lessonType"),
                    rs.getInt("isPrivate") == 1,
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("date"),
                    rs.getString("status"),
                    rs.getInt("availToPublic") == 1
            );
            offerings.add(offering);
        }

        rs.close();
        stmt.close();

        return offerings;
    }

    public static Offering getOfferingById(UUID id) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM offerings WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Offering offering = new Offering(
                    rs.getString("id"),
                    rs.getString("location"),
                    rs.getString("lessonType"),
                    rs.getInt("isPrivate") == 1,
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("date"),
                    rs.getString("status"),
                    rs.getInt("availToPublic") == 1
            );
            rs.close();
            pstmt.close();
            return offering;
        } else {
            rs.close();
            pstmt.close();
            return null;
        }
    }

    public static void updateOffering(Offering offering) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "UPDATE offerings SET location = ?, lessonType = ?, isPrivate = ?, startTime = ?, endTime = ?, date = ?, status = ?, availToPublic = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, offering.getLocation());
        pstmt.setString(2, offering.getLessonType());
        pstmt.setInt(3, offering.isPrivate() ? 1 : 0);
        pstmt.setString(4, offering.getStartTime());
        pstmt.setString(5, offering.getEndTime());
        pstmt.setString(6, offering.getDate());
        pstmt.setString(7, offering.getStatus());
        pstmt.setInt(8, offering.isAvailToPublic() ? 1 : 0);
        pstmt.setString(9, offering.getId().toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static boolean deleteOffering(UUID id) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE FROM offerings WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id.toString());
        int affectedRows = pstmt.executeUpdate();
        pstmt.close();
        return affectedRows > 0;
    }
}
