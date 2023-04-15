package planetarium;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    private static final String INPUT_INVALID_FORMAT = "Attenzione: il dato inserito non e' nel formato corretto";
	private static final String INPUT_NUMBER_DESIRED = "\nDigita il numero dell'opzione desiderata > ";
    private static final Scanner scanner = createScanner();

    private static Scanner createScanner() {
        return new Scanner(System.in);
    }

	/**
	 * Read user input and confirm it is a valid byte number.
	 * @return A byte number representing the menu choice.
	 */
    public static byte choice() {
		byte choice;
		do {
			System.out.print(INPUT_NUMBER_DESIRED);
			try {
				choice = scanner.nextByte();
				System.out.println("\n");
				return choice;
			} catch (InputMismatchException e) {
				System.out.println(INPUT_INVALID_FORMAT);
				scanner.next();
			}
		} while (true);
	}

	/**
	 * Read user input and confirm it is a valid long number.
	 * @return A long number representing a celestial body coordinate or his mass.
	 */
	public static long readLong(String message) {
		long input;
		do {
			System.out.print(message);
			try {
				input = scanner.nextLong();
				return input;
			} catch (InputMismatchException e) {
				System.out.println(INPUT_INVALID_FORMAT);
				scanner.next();
			}
		} while (true);
	}

	/**
	 * Read user input and confirm it is a valid String.
	 * If the user unintentionally inserts a space, it will be removed.
	 * @return A String representing the celestial ID.
	 */
	public static String readString(String message) {
		String input;
		do {
			System.out.print(message);
			try {
				input = scanner.next().replaceAll(" ", "");
				return input;
			} catch (InputMismatchException e) {
				System.out.println(INPUT_INVALID_FORMAT);
				scanner.next();
			}
		} while (true);
	}
}