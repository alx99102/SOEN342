package system;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entities.Offering;

public class OfferingDAO {

    public static void saveOffering(Offering offering) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO offerings (id, location, room, lessonType, isPrivate, startTime, endTime, date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, offering.getId().toString());
        pstmt.setString(2, offering.getLocation());
        pstmt.setString(3, offering.getRoom());
        pstmt.setString(4, offering.getLessonType());
        pstmt.setInt(5, offering.isPrivate() ? 1 : 0);
        pstmt.setString(6, offering.getStartTime());
        pstmt.setString(7, offering.getEndTime());
        pstmt.setString(8, offering.getDate());
        pstmt.setString(9, offering.getStatus());

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
                    rs.getString("room"),
                    rs.getString("lessonType"),
                    rs.getInt("isPrivate") == 1,
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("date"),
                    rs.getString("status")
            );
            offerings.add(offering);
        }

        rs.close();
        stmt.close();

        return offerings;
    }

    public static Offering[] getOfferingsForInstructor(String[] cities) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM offerings WHERE status='Available' AND location IN (";

        for (int i = 0; i < cities.length; i++) {
            sql += "?";
            if (i < cities.length - 1) {
                sql += ", ";
            }
        }
        sql += ")";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (int i = 0; i < cities.length; i++) {
            pstmt.setString(i + 1, cities[i]);
        }

        ResultSet rs = pstmt.executeQuery();
        List<Offering> offerings = new ArrayList<>();

        while (rs.next()) {
            Offering offering = new Offering(
                    rs.getString("id"),
                    rs.getString("location"),
                    rs.getString("room"),
                    rs.getString("lessonType"),
                    rs.getInt("isPrivate") == 1,
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("date"),
                    rs.getString("status")
            );
            offerings.add(offering);
        }


        return offerings.toArray(new Offering[0]);
    }

    public static Offering[] getOfferingsForClient() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM offerings WHERE status='Taken'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Offering> offerings = new ArrayList<>();
        while (rs.next()) {
            Offering offering = new Offering(
                    rs.getString("id"),
                    rs.getString("location"),
                    rs.getString("room"),
                    rs.getString("lessonType"),
                    rs.getInt("isPrivate") == 1,
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("date"),
                    rs.getString("status")
            );
            offerings.add(offering);
        }
        return offerings.toArray(new Offering[0]);
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
                    rs.getString("room"),
                    rs.getString("lessonType"),
                    rs.getInt("isPrivate") == 1,
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("date"),
                    rs.getString("status")
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
        String sql = "UPDATE offerings SET location = ?, lessonType = ?, isPrivate = ?, startTime = ?, endTime = ?, date = ?, status = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, offering.getLocation());
        pstmt.setString(2, offering.getLessonType());
        pstmt.setInt(3, offering.isPrivate() ? 1 : 0);
        pstmt.setString(4, offering.getStartTime());
        pstmt.setString(5, offering.getEndTime());
        pstmt.setString(6, offering.getDate());
        pstmt.setString(7, offering.getStatus());
        pstmt.setString(8, offering.getId().toString());
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
