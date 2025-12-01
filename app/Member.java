import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Scanner;

public class Member {
	public static void openMenu(Scanner scanner) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- MEMBER OPERATIONS --");
			System.out.print("0. Quit\n1. Dashboard\n2. Manage Profile\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" -> Main.quit();
				case "1" -> {
					showDashboard(Main.getUserId());
					Main.waitForInput();
				}
				case "2" -> {
					profilePrompt(scanner, Main.getUserId());
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}

	public static void printHealthMetrics(int id) {
		try (Connection connection = Main.connect()) {
            String sql = "SELECT weight, heart_rate, timestamp FROM HealthHistory WHERE member_id = ? ORDER BY timestamp DESC LIMIT 1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				double weight = resultSet.getDouble("weight");
				int heartRate = resultSet.getInt("heart_rate");
				Timestamp timestamp = resultSet.getTimestamp("timestamp");
				System.out.println("Latest Health Metrics:");
				System.out.println("Weight: " + weight + " kg");
				System.out.println("Heart Rate: " + heartRate + " bpm");
				System.out.println("Last Recorded: " + timestamp);
			}
			else
				System.out.println("Health Metric Data - Missing");
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void printFitnessGoals(int id) {
		try (Connection connection = Main.connect()) {
            String sql = "SELECT goal_info, start_date, end_date FROM FitnessGoal WHERE member_id = ? AND end_date > CURRENT_DATE";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			System.out.println("\nActive Fitness Goals:");
			boolean hasGoals = false;
			while (resultSet.next()) {
				String goalInfo = resultSet.getString("goal_info");
				Date startDate = resultSet.getDate("start_date");
				Date endDate = resultSet.getDate("end_date");
				System.out.println("Goal: " + goalInfo);
				System.out.println("Start Date: " + startDate);
				System.out.println("End Date: " + endDate);
				hasGoals = true;
			}
			if (!hasGoals)
				System.out.println("Fitness Goal Data - Missing");
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void printPastSessions(int id) {
		try (Connection connection = Main.connect()) {
            String sql = "SELECT COUNT(*) AS class_count FROM Booking b JOIN Session s ON b.session_id = s.session_id WHERE b.member_id = ? AND s.session_date < CURRENT_DATE";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				int sessionCount = resultSet.getInt("class_count");
				System.out.println("\nPast Sessions Attended: " + sessionCount);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void printUpcomingSession(int id) {
		try (Connection connection = Main.connect()) {
            String sql = "SELECT s.session_type, s.session_date, s.session_time, t.first_name AS trainer_first_name, t.last_name AS trainer_last_name " +
						"FROM Booking b JOIN Session s ON b.session_id = s.session_id JOIN Trainer t ON s.trainer_id = t.trainer_id " +
						"WHERE b.member_id = ? AND s.session_date >= CURRENT_DATE ORDER BY s.session_date, s.session_time";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			System.out.println("\nUpcoming Sessions:");
			boolean hasSessions = false;
			while (resultSet.next()) {
				String sessionType = resultSet.getString("session_type");
				Date sessionDate = resultSet.getDate("session_date");
				Time sessionTime = resultSet.getTime("session_time");
				String trainerFirstName = resultSet.getString("trainer_first_name");
				String trainerLastName = resultSet.getString("trainer_last_name");
				System.out.println("Session Type: " + sessionType);
				System.out.println("Date: " + sessionDate);
				System.out.println("Time: " + sessionTime);
				System.out.println("Trainer: " + trainerFirstName + " " + trainerLastName);
				System.out.println("--------");
				hasSessions = true;
			}
			if (!hasSessions)
				System.out.println("Upcoming Session Data - Missing");
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

    public static void showDashboard(int id) {
		Main.clear();
		System.out.println("-- DASHBOARD --");

		// Current health metrics
		printHealthMetrics(id);

		// Current fitness goals
		printFitnessGoals(id);

		// Sessions attended
		printPastSessions(id);

		// Upcoming sessions
		printUpcomingSession(id);
    }

    public static void profilePrompt(Scanner scanner, int id) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- PROFILE OPERATIONS --");
			System.out.print("0. Back\n1. Update Personal Details\n2. Update Fitness Goal\n3. Manage Health Metrics\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" ->  {
					return;
				}
				case "1" -> {
					detailPrompt(scanner, id);
				}
				case "2" -> {
					fitnessPrompt(scanner, id);
				}
				case "3" -> {
					healthMetricPrompt(scanner, id);
					continue;
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
    }

    public static void detailPrompt(Scanner scanner, int id) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- UPDATE PERSONAL DETAILS --");
			System.out.print("0. Back\n1. Change Email\n2. Change Phone Number\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" ->  {
					return;
				}
				case "1" -> {
					Main.clear();
					
					System.out.println("-- Enter NEW EMAIL --");
					input = scanner.nextLine();
					String email = input;

					try (Connection connection = Main.connect()) {
            			String sql = "UPDATE Member SET email = ? WHERE member_id = ?";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setString(1, email);
						preparedStatement.setInt(2, id);

						int result = preparedStatement.executeUpdate();
						
						if (result > 0)
							System.out.println("Email changed!");
						else
							System.out.println("ERROR: Could not update email!");
					}
					catch (Exception e) {
						System.out.println(e);
					}
					Main.waitForInput();
					break;
				}
				case "2" -> {
					Main.clear();
					
					System.out.println("-- Enter NEW PHONE NUMBER --");
					input = scanner.nextLine();
					String phoneNumber = input;

					try (Connection connection = Main.connect()) {
            			String sql = "UPDATE Member SET phone_number = ? WHERE member_id = ?";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setString(1, phoneNumber);
						preparedStatement.setInt(2, id);

						int result = preparedStatement.executeUpdate();
						
						if (result > 0)
							System.out.println("Phone Number changed!");
						else
							System.out.println("ERROR: Could not update phone number!");
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

	public static void fitnessPrompt(Scanner scanner, int id) {
			String input = "-1";
			
			while (!"0".equals(input)) {
				Main.clear();
				
				System.out.println("-- UPDATE FITNESS GOAL --");
				System.out.print("0. Back\n1. Change Goal Info\n");
				input = scanner.nextLine();
				switch (input) {
					case "0" ->  {
						return;
					}
					case "1" -> {
						Main.clear();
						
						try (Connection connection = Main.connect()) {
							String sql = "SELECT goal_id, goal_info FROM FitnessGoal WHERE member_id = ? AND end_date > CURRENT_DATE LIMIT 1";
							PreparedStatement preparedStatement = connection.prepareStatement(sql);

							preparedStatement.setInt(1, id);
							ResultSet resultSet = preparedStatement.executeQuery();
							
							if (resultSet.next()) {
								int goalId = resultSet.getInt("goal_id");
								System.out.println("Current Goal: " + resultSet.getString("goal_info"));
								Main.waitForInput();

								Main.clear();
								System.out.println("-- Enter NEW GOAL INFO--");
								input = scanner.nextLine();
								String goalInfo = input;

								// New goal statement
								sql = "UPDATE FitnessGoal SET goal_info = ? WHERE goal_id = ?";
								preparedStatement = connection.prepareStatement(sql);

								preparedStatement.setString(1, goalInfo);
                        		preparedStatement.setInt(2, goalId);

								int result = preparedStatement.executeUpdate();

								if (result > 0)
									System.out.println("Goal changed!");
								else
									System.out.println("ERROR: Could not update goal!");
							}
							else
								System.out.println("No goals found!");
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

	public static void healthMetricPrompt(Scanner scanner, int id) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- MANAGE HEALTH METRICS --");
			System.out.print("0. Back\n1. View Health Metrics\n2. Add Health Metric\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" ->  {
					return;
				}
				case "1" -> {
					Main.clear();
					
					try (Connection connection = Main.connect()) {
						String sql = "SELECT weight, heart_rate, timestamp FROM HealthHistory WHERE member_id = ? ORDER BY timestamp DESC";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setInt(1, id);
						ResultSet resultSet = preparedStatement.executeQuery();

						if (!resultSet.isBeforeFirst())
							System.out.println("No health metrics found!");
						else {
							System.out.println("-- Your Health Metrics --");
							while (resultSet.next()) {
								double weight = resultSet.getDouble("weight");
								int heartRate = resultSet.getInt("heart_rate");
								Timestamp timestamp = resultSet.getTimestamp("timestamp");

								System.out.print("Weight: " + weight + " kg");
								System.out.print(", Heart Rate: " + heartRate + " bpm");
								System.out.print(", Timestamp: " + timestamp);
								System.out.println();
							}
						}
					}
					catch (Exception e) {
						System.out.println(e);
					}
					Main.waitForInput();
					break;
				}
				case "2" -> {
					Main.clear();
					System.out.println("-- Enter METRIC WEIGHT --");
					input = scanner.nextLine();
					Double weight = Double.valueOf(input);
					System.out.println("-- Enter METRIC BPM --");
					input = scanner.nextLine();
					int heartRate = Integer.parseInt(input);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());

					Main.clear();
					try (Connection connection = Main.connect()) {
						String sql = "INSERT INTO HealthHistory (member_id, weight, heart_rate, timestamp) VALUES (?, ?, ?, ?)";
						PreparedStatement preparedStatement = connection.prepareStatement(sql);

						preparedStatement.setInt(1, id);
						preparedStatement.setDouble(2, weight);
						preparedStatement.setInt(3, heartRate);
						preparedStatement.setTimestamp(4, timestamp);

						int result = preparedStatement.executeUpdate();
						
						if (result > 0)
							System.out.println("New health metric added!");
						else
							System.out.println("ERROR: Could not add metric!");
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
