import java.util.Scanner;

public class Admin {
	public static void openMenu(Scanner scanner) {
		String input = "-1";
		
		while (!"0".equals(input)) {
			Main.clear();
			
			System.out.println("-- ADMIN OPERATIONS --");
			System.out.print("0. Quit\n1. Book Room\n2. Check Equipment\n");
			input = scanner.nextLine();
			switch (input) {
				case "0" -> Main.quit();
				case "1" -> {
					// ROOM PROMPT
					continue;
				}
				case "2" -> {
					// EQUIPMENT PROMPT
					continue;
				}
				default -> System.out.println("Invalid command!");
			}
			System.out.println();
		}
	}
}
