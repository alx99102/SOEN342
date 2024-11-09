package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:lessons.db";
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection(DATABASE_URL);
        }
        return conn;
    }

    public static void initializeDatabase() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        // Create Offerings table
        String createOfferingsTable = "CREATE TABLE IF NOT EXISTS offerings (" +
                "id TEXT PRIMARY KEY," +
                "location TEXT," +
                "lessonType TEXT," +
                "isPrivate INTEGER," +
                "startTime TEXT," +
                "endTime TEXT," +
                "date TEXT," +
                "status TEXT," +
                "availToPublic INTEGER" +
                ");";
        stmt.execute(createOfferingsTable);

        // Create Users table
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "role TEXT," +
                "phoneNumber TEXT," +  // For instructors
                "specialization TEXT" +  // For instructors
                ");";
        stmt.execute(createUsersTable);

        stmt.close();
    }
}
