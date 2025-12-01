import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Admin {
	public static void openMenu(Scanner scanner) throws ParseException {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- ADMIN OPERATIONS --");
			System.out.print("0. Quit\n1. Book Room\n2. Update Equipment\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" -> Main.quit();
				case "1" -> {
					roomPrompt(scanner, Main.getUserId());
				}
				case "2" -> {
					equipmentPrompt(scanner, Main.getUserId());
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}

	public static void roomPrompt(Scanner scanner, int id) throws ParseException {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- BOOK ROOM --");
			System.out.print("0. Back\n1. Book a room/session\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" ->  {
					return;
				}
				case "1" -> {
					Main.clear();
					System.out.println("-- Enter SESSION TYPE --");
					input = scanner.nextLine();
					String sessionType = input;
					System.out.println("-- Enter SESSION DATE --");
					input = scanner.nextLine();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date sessionDate = new java.sql.Date(dateFormat.parse(input).getTime());
					System.out.println("-- Enter SESSION TIME --");
					input = scanner.nextLine();
					Time sessionTime = Time.valueOf(input);
					System.out.println("-- Enter ROOM ID --");
					input = scanner.nextLine();
					int roomId = Integer.parseInt(input);

					Main.clear();
					try (Connection connection = Main.connect()) {
						// Check room
						String sql = "SELECT COUNT(*) FROM Room WHERE room_id = ?";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setInt(1, roomId);

						ResultSet resultSet = preparedStatement.executeQuery();
						resultSet.next();

						if (resultSet.getInt(1) == 0) {
							System.out.println("ERROR: Room cannot be found!");
							Main.waitForInput();
							continue;
						}

						// Check double booking
						sql = "SELECT COUNT(*) FROM Session WHERE room_id = ? AND session_date = ? AND session_time = ?";
						preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setInt(1, roomId);
						preparedStatement.setDate(2, sessionDate);
						preparedStatement.setTime(3, sessionTime);

						resultSet = preparedStatement.executeQuery();
						resultSet.next();

						if (resultSet.getInt(1) > 0) {
							System.out.println("ERROR: Double booking detected!");
							Main.waitForInput();
							continue;
						}
						else {
							sql = "INSERT INTO Session (session_type, session_date, session_time, room_id, trainer_id) VALUES (?, ?, ?, ?, ?)";
							preparedStatement = connection.prepareStatement(sql);

							preparedStatement.setString(1, sessionType);
							preparedStatement.setDate(2, sessionDate);
							preparedStatement.setTime(3, sessionTime);
							preparedStatement.setInt(4 , roomId);
							preparedStatement.setInt(5, id);

							int result = preparedStatement.executeUpdate();

							if (result > 0)
								System.out.println("New session booked!");
							else
								System.out.println("ERROR: Could not book session/room!");
						}
					}
					catch (Exception e) {
						System.out.println(e);
					}
					Main.waitForInput();
					break;
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}

	public static void equipmentPrompt(Scanner scanner, int id) throws ParseException {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- UPDATE EQUIPMENT --");
			System.out.print("0. Back\n1. Update Equipment Information\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" ->  {
					return;
				}
				case "1" -> {
					Main.clear();
					System.out.println("-- Enter EQUIPMENT ID --");
					input = scanner.nextLine();
					int equipmentId = Integer.parseInt(input);
					System.out.println("-- Enter EQUIPMENT STATUS --");
					input = scanner.nextLine();
					String status = input;
					System.out.println("-- Enter ROOM ID --");
					input = scanner.nextLine();
					int roomId = Integer.parseInt(input);

					Main.clear();
					try (Connection connection = Main.connect()) {
						String sql = "SELECT COUNT(*) FROM Equipment WHERE equipment_id = ?";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setInt(1, equipmentId);

						ResultSet resultSet = preparedStatement.executeQuery();
						resultSet.next();

						if (resultSet.getInt(1) == 0) {
							System.out.println("ERROR: Equipment cannot be found!");
							Main.waitForInput();
							continue;
						}
						else {
							sql = "UPDATE Equipment SET room_id = ?, status = ?, updater_id = ? WHERE equipment_id = ?";
							preparedStatement = connection.prepareStatement(sql);

							preparedStatement.setInt(1, roomId);
							preparedStatement.setString(2, status);
							preparedStatement.setInt(3, id);
							preparedStatement.setInt(4, equipmentId);

							int result = preparedStatement.executeUpdate();

							if (result > 0)
								System.out.println("Equipment updated!");
							else
								System.out.println("ERROR: Could not update equipment!");
						}
					}
					catch (Exception e) {
						System.out.println(e);
					}
					Main.waitForInput();
					break;
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}
	
}
