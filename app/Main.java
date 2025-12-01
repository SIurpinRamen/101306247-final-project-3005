import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	static String userType;
	static int userId;
	static Scanner scanner = new Scanner(System.in);

	static String getUserType() { return userType; }
	static int getUserId() { return userId; }
	static void setUserType(String user) { userType = user; }
	static void setUserId(int id) { userId = id; }

	// Connect to the database with JDBC
    public static Connection connect() throws SQLException {
		// PostgreSQL info
		String URL = "jdbc:postgresql://localhost:5432/project_db";
		String USER = "postgres";
		String PASS = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        }
		catch (ClassNotFoundException e) {
        	throw new SQLException("JDBC driver not found", e);
        }
    }

	// Clear the terminal output
	public static void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	// Signal to the user when to continue manually
	public static void waitForInput() {
		System.out.print("\nPress enter to continue ");
		scanner.nextLine();
	}

	// Quit the application
	public static void quit() {
		System.out.println("Quitting...");
		System.exit(0);
	}

	public static void main(String[] args) {
		try {
			Login.openMenu(scanner);

			if (userType != null) {
				if (userType.equals("Member"))
					Member.openMenu(scanner);
				else if (userType.equals("Trainer")) 
					Trainer.openMenu(scanner);
				else if (userType.equals("Admin"))
					Admin.openMenu(scanner);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}