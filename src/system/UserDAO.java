package system;

import java.sql.*;
import java.util.UUID;
import user.*;

public class UserDAO {

    public static void saveUser(User user) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO users (id, name, role, phoneNumber, specialization) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getId().toString());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getRole());

        if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            pstmt.setString(4, instructor.getPhoneNumber());
            pstmt.setString(5, instructor.getSpecialization());
        } else {
            pstmt.setString(4, null);
            pstmt.setString(5, null);
        }

        pstmt.executeUpdate();
        pstmt.close();
    }

    public static User getUserById(UUID id) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String role = rs.getString("role");
            if (role.equals("Administrator")) {
                return new Administrator(UUID.fromString(rs.getString("id")), rs.getString("name"));
            } else if (role.equals("Instructor")) {
                Instructor instructor = new Instructor(UUID.fromString(rs.getString("id")), rs.getString("name"));
                instructor.setPhoneNumber(rs.getString("phoneNumber"));
                instructor.setSpecialization(rs.getString("specialization"));
                return instructor;
            } else {
                // Handle other user types if needed
                return null;
            }
        }

        rs.close();
        pstmt.close();

        return null; // User not found
    }
}
