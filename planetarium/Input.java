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

    public static byte choice() {
		byte choice;
		do {
			System.out.print(INPUT_NUMBER_DESIRED);
			try {
				choice = scanner.nextByte();
				return choice;
			} catch (InputMismatchException e) {
				System.out.println(INPUT_INVALID_FORMAT);
			}
		} while (true);
	}

	public static long readLong() {
		long input;
		do {
			try {
				input = scanner.nextLong();
				return input;
			} catch (InputMismatchException e) {
				System.out.println(INPUT_INVALID_FORMAT);
			}
		} while (true);
	}

	public static int readInt() {
		int input;
		do {
			try {
				input = scanner.nextInt();
				return input;
			} catch (InputMismatchException e) {
				System.out.println(INPUT_INVALID_FORMAT);
			}
		} while (true);
	}
}
