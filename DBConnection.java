import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection{

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");

            // Replace these with your actual database details
            String url = "jdbc:postgresql://localhost:5432/shopping_mart1";
            String user = "postgres";
            String password = "your password";

            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to PostgreSQL database successfully.");
        } catch (Exception e) {
            System.out.println("Database connection failed:");
            e.printStackTrace();
        }
        return con;
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Connection is null. Check credentials or driver.");
        }
    }
}
