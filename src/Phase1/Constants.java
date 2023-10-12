package Phase1;
import java.util.Scanner;

public class Constants {
	public static int HORIZONTAL_GRID_SIZE = 6;
	public static int VERTICAL_GRID_SIZE = 5;
	public static char[] INPUT = { 'T', 'U', 'P', 'I', 'V', 'L' };

	public static void inputGameInfo() {
		Scanner scanner = new Scanner(System.in);

		// Ask the user for the horizontal size
		System.out.print("Enter the horizontal size of the game board (number of columns): ");
		HORIZONTAL_GRID_SIZE = scanner.nextInt();

		// Consume the newline character
		scanner.nextLine();

		// Ask the user for the vertical size
		System.out.print("Enter the vertical size of the game board (number of rows): ");
		VERTICAL_GRID_SIZE = scanner.nextInt();

		// Consume the newline character
		scanner.nextLine();

		// Ask the user for the array of pentomino pieces
		System.out.println();
		System.out.println("All possible pieces: T U P I V L F W X Y Z N ");
		System.out.print("Enter the pentomino pieces (characters separated by spaces): ");
		String inputString = scanner.nextLine();
		INPUT = inputString.replaceAll("\\s+", "").toCharArray();

	}

}
