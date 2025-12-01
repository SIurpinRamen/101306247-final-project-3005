import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Login {
	public static void openMenu(Scanner scanner) throws ParseException {
		boolean flag = true;
		String input = "-1";
		
		while (!"0".equals(input) && flag) {
			Main.clear();
			
			System.out.println("-- SIGN IN --");
			System.out.print("0. Quit\n1. Login\n2. Register\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" -> Main.quit();
				case "1" -> {
					loginPrompt(scanner);
					flag = false;
				}
				case "2" -> {
					registerPrompt(scanner);
					flag = false;
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}

	public static void loginPrompt(Scanner scanner) {
		String username, password;

		String input = "-1";
		boolean flag = true;
		
		while (flag) {
			Main.clear();
			
			System.out.println("-- Select User Type --");
			System.out.print("1. Member\n2. Trainer\n3. Admin\n");
			input = scanner.nextLine();
			switch (input) {
				case "1" -> {
					Main.setUserType("Member");
					flag = false;
				}
				case "2" -> {
					Main.setUserType("Trainer");
					flag = false;
				}
				case "3" -> {
					Main.setUserType("Admin");
					flag = false;
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
		while (true) {
			Main.clear();
			
			System.out.println("-- Enter ID / USERNAME --");
			input = scanner.nextLine();
			username = input;
			System.out.println("-- Enter LAST NAME / PASSWORD --");
			input = scanner.nextLine();
			password = input;
			
			System.out.println();
			if (loginUser(username, password)) {
				Main.setUserId(Integer.parseInt(username));
				return;
			}
			else {
				System.out.println("Login failed!");
				Main.waitForInput();
			}
		}
	}

	public static boolean loginUser(String user, String pass) {
		try (Connection connection = Main.connect()) {
			PreparedStatement preparedStatement = null;
			String sql = "";

			if (Main.getUserType().equals("Member"))
				sql = "SELECT member_id FROM Member WHERE member_id = ? AND last_name = ?";
			else if (Main.getUserType().equals("Trainer"))
				sql = "SELECT trainer_id FROM Trainer WHERE trainer_id = ? AND last_name = ?";
			else if (Main.getUserType().equals("Admin"))
				sql = "SELECT admin_id FROM Admin WHERE admin_id = ? AND password = ?";
			
			if (!sql.isEmpty()) {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, Integer.parseInt(user));
                preparedStatement.setString(2, pass);
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				return true;
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public static void registerPrompt(Scanner scanner) throws ParseException {
		String first_name, last_name, email, gender, phone_number;
		Date date_of_birth;
		String input = "-1";

		while (true) {
			Main.clear();
			
			System.out.println("-- Enter FIRST NAME --");
			input = scanner.nextLine();
			first_name = input;
			System.out.println("-- Enter LAST NAME --");
			input = scanner.nextLine();
			last_name = input;
			System.out.println("-- Enter EMAIL --");
			input = scanner.nextLine();
			email = input;
			System.out.println("-- Enter DATE OF BIRTH --");
			input = scanner.nextLine();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date_of_birth = new java.sql.Date(dateFormat.parse(input).getTime());
			System.out.println("-- Enter GENDER --");
			input = scanner.nextLine();
			gender = input;
			System.out.println("-- Enter PHONE NUMBER --");
			input = scanner.nextLine();
			phone_number = input;

			System.out.println();
			if (registerUser(first_name, last_name, email, date_of_birth, gender, phone_number))
				return;
			else {
				System.out.println("Register failed!");
				Main.waitForInput();
			}
		}
	}

	public static boolean registerUser(String first_name, String last_name, String email, Date date_of_birth, String gender, String phone_number) {
		try (Connection connection = Main.connect()) {
			String sql = "INSERT INTO Member (first_name, last_name, email, date_of_birth, gender, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, first_name);
			preparedStatement.setString(2, last_name);
			preparedStatement.setString(3, email);
			preparedStatement.setDate(4, date_of_birth);
			preparedStatement.setString(5, gender);
			preparedStatement.setString(6, phone_number);
			
			int result = preparedStatement.executeUpdate();
            return result > 0;
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}
}