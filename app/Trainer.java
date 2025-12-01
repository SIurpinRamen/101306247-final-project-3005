import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Scanner;

public class Trainer {
	public static void openMenu(Scanner scanner) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- TRAINER OPERATIONS --");
			System.out.print("0. Quit\n1. Set Availability\n2. Schedule View\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" -> Main.quit();
				case "1" -> {
					availabilityPrompt(scanner, Main.getUserId());
				}
				case "2" -> {
					schedulePrompt(scanner, Main.getUserId());
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}

	public static void availabilityPrompt(Scanner scanner, int id) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- SET AVAILABILITY --");
			System.out.print("0. Back\n1. Add New Availability\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" ->  {
					return;
				}
				case "1" -> {
					Main.clear();
					System.out.println("-- Enter START TIME --");
					input = scanner.nextLine();
					Time startTime = Time.valueOf(input + ":00");
					System.out.println("-- Enter END TIME--");
					input = scanner.nextLine();
					Time endTime = Time.valueOf(input + ":00");

					Main.clear();
					try (Connection connection = Main.connect()) {
						// Check overlap first
						String sql = "SELECT * FROM Availability " +
									"WHERE trainer_id = ? " +
									"AND ( (start_time <= ? AND end_time > ?) " +
									"OR (start_time < ? AND end_time >= ?) )";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setInt(1, id);
						preparedStatement.setTime(2, startTime);
						preparedStatement.setTime(3, startTime);
						preparedStatement.setTime(4, endTime);
						preparedStatement.setTime(5, endTime);

						ResultSet resultSet = preparedStatement.executeQuery();

						if (resultSet.isBeforeFirst()) {
							System.out.println("ERROR: Overlap detected!");
							Main.waitForInput();
							continue;
						}
						else {
							sql = "INSERT INTO Availability (trainer_id, start_time, end_time) VALUES (?, ?, ?)";
							preparedStatement = connection.prepareStatement(sql);

							preparedStatement.setInt(1, id);
							preparedStatement.setTime(2, startTime);
							preparedStatement.setTime(3, endTime);

							int result = preparedStatement.executeUpdate();

							if (result > 0)
								System.out.println("New availability added!");
							else
								System.out.println("ERROR: Could not add availability!");
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

	public static void schedulePrompt(Scanner scanner, int id) {
		try (Connection connection = Main.connect()) {
            String sql = "SELECT " +
						"s.session_type, " +
						"s.session_date, " +
						"s.session_time, " +
						"r.room_id, " +
						"r.capacity AS room_capacity " +
						"FROM Session s " +
						"JOIN Room r ON s.room_id = r.room_id " +
						"WHERE s.trainer_id = ? " +
						"ORDER BY s.session_date, s.session_time";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No planned sessions found!");
			}
			else {
				System.out.println("-- Your Schedule --");
				while (resultSet.next()) {
                    String sessionType = resultSet.getString("session_type");
                    Date sessionDate = resultSet.getDate("session_date");
                    Time sessionTime = resultSet.getTime("session_time");
                    int roomId = resultSet.getInt("room_id");
                    int roomCapacity = resultSet.getInt("room_capacity");

                    System.out.print("\nSession Type: " + sessionType);
                    System.out.print(", Session Date: " + sessionDate);
                    System.out.print("\nSession Time: " + sessionTime);
                    System.out.print(", Room ID: " + roomId + ", Capacity: " + roomCapacity);
					System.out.println();
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		Main.waitForInput();
	}
}