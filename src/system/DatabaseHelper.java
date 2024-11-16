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
                "room TEXT," +
                "lessonType TEXT," +
                "isPrivate INTEGER," +
                "startTime TEXT," +
                "endTime TEXT," +
                "date TEXT," +
                "status TEXT," +
                // Constraint: “Offerings are unique. In other words, multiple offerings on the same day and
                // time slot must be offered at a different location"
                "UNIQUE(location, date, startTime, endTime)" +
                ");";
        stmt.execute(createOfferingsTable);

        // Create Users table
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "role TEXT," +
                "phoneNumber TEXT," +  // For instructors
                "specialization TEXT," +  // For instructors
                "age INTEGER," +  // For clients
                "guardianName TEXT," +  // For clients
                "CONSTRAINT age_guardian_check CHECK (age >= 18 OR guardianName IS NOT NULL)" + // Constraint: Any client who is underage must necessarily be accompanied by an adult who acts as their guardian.
                ");";
        stmt.execute(createUsersTable);

        String createInstructorAvailabilityTable = "CREATE TABLE IF NOT EXISTS availability ("+
                "id TEXT PRIMARY KEY," +
                "city TEXT REFERENCES offerings(location)," +
                "instructor TEXT REFERENCES users(id));";

        stmt.execute(createInstructorAvailabilityTable);

        String createBookingTable = "CREATE TABLE IF NOT EXISTS booking ("+
                "id TEXT PRIMARY KEY," +
                "offering TEXT REFERENCES offerings(id)," +
                "client TEXT REFERENCES users(id)" +
                ");";
        stmt.execute(createBookingTable);

        stmt.close();
    }
}
