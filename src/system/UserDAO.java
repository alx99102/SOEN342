package system;

import java.sql.*;
import java.util.List;
import java.util.UUID;
import user.*;
import java.util.ArrayList;

public class UserDAO {

    public static void saveUser(User user) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO users (id, name, role, phoneNumber, specialization, age, guardianName) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

        if (user instanceof Client) {
            Client client = (Client) user;
            pstmt.setInt(6, client.getAge());
            pstmt.setString(7, client.getGuardianName());
        } else {
            pstmt.setInt(6, 99999);
            pstmt.setString(7, null);
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
                Administrator admin = Administrator.getInstance();
                admin.setId(id);
                return admin;
            } else if (role.equals("Instructor")) {
                Instructor instructor = new Instructor(UUID.fromString(rs.getString("id")), rs.getString("name"));
                instructor.setPhoneNumber(rs.getString("phoneNumber"));
                instructor.setSpecialization(rs.getString("specialization"));
                return instructor;
            } else if (role.equals("Client")) {
                return new Client(UUID.fromString(rs.getString("id")), rs.getString("name"), rs.getInt("age"), rs.getString("guardianName"));
            }
        }

        rs.close();
        pstmt.close();

        return null; // User not found
    }

    public static List<User> getAllUsers() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "SELECT * FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<User> users = new ArrayList<>();

        while (rs.next()) {
            String role = rs.getString("role");
            // Ignore the Administrator user, as there is only one and it is the user logged in (only the administrator can call this method)
            if (role.equals("Instructor")) {
                Instructor instructor = new Instructor(UUID.fromString(rs.getString("id")), rs.getString("name"));
                instructor.setPhoneNumber(rs.getString("phoneNumber"));
                instructor.setSpecialization(rs.getString("specialization"));
                users.add(instructor);
            } else if (role.equals("Client")) {
                users.add(new Client(UUID.fromString(rs.getString("id")), rs.getString("name"), rs.getInt("age"), rs.getString("guardianName")));
            }
        }

        rs.close();
        stmt.close();

        return users;
    }

    public static void deleteUser(UUID id) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id.toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static boolean adminExists() {
        try {
            Connection conn = DatabaseHelper.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE role = 'Administrator'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            stmt.close();
            return count > 0;
        } catch (SQLException e) {
            System.err.println("Failed to check if administrator exists: " + e.getMessage());
            return false;
        }
    }
}
