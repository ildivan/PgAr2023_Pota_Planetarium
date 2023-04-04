package planetarium;

public class Menu {
	private static final boolean IS_WINDOWS = System.getProperty("os.name").contains("Windows");

	private static String getFrame() {
		if (IS_WINDOWS)
			return "----------------------------------------------------------";
		else
			return "――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";
	}

	protected static void welcome() {
		System.out.println("\n"
				+ "\t██████╗░██╗░░░░░░█████╗░███╗░░██╗███████╗████████╗░█████╗░██████╗░██╗██╗░░░██╗███╗░░░███╗\n"
				+ "\t██╔══██╗██║░░░░░██╔══██╗████╗░██║██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██║██║░░░██║████╗░████║\n"
				+ "\t██████╔╝██║░░░░░███████║██╔██╗██║█████╗░░░░░██║░░░███████║██████╔╝██║██║░░░██║██╔████╔██║\n"
				+ "\t██╔═══╝░██║░░░░░██╔══██║██║╚████║██╔══╝░░░░░██║░░░██╔══██║██╔══██╗██║██║░░░██║██║╚██╔╝██║\n"
				+ "\t██║░░░░░███████╗██║░░██║██║░╚███║███████╗░░░██║░░░██║░░██║██║░░██║██║╚██████╔╝██║░╚═╝░██║\n"
				+ "\t╚═╝░░░░░╚══════╝╚═╝░░╚═╝╚═╝░░╚══╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░╚═════╝░╚═╝░░░░░╚═╝");
		System.out.println("\nBenvenuto al gestionale degli Xylophaxians");
	}

	protected static void printMainMenu() {
		System.out.println(getFrame());
		System.out.println("	1. Aggiungi nuovo Pianeta/Luna");
		System.out.println("	2. Rimuovi nuova Pianeta/Luna");
		System.out.println("	3. Ottieni informazioni del corpo celeste");
		System.out.println("	4. Mostra la lista dei corpi di tutto il sistema");
		System.out.println("	5. Ottieni il centro di massa");
		System.out.println("	6. Calcola rotta tra due corpi");
		System.out.println("	7. Mostra possibili collisioni");
		System.out.println("	8. Esci");
	}

	protected static void printAddCelestialBodyMenu(boolean emptyPlanets) {
		System.out.println(getFrame());
		System.out.println("	1. Aggiungi nuovo Pianeta");
		if (emptyPlanets) {
			System.out.println("	2. Esci");
			return;
		}
		System.out.println("	2. Aggiungi nuova Luna");
		System.out.println("	3. Esci");
	}

	protected static void printRemoveCelestialBodyMenu(boolean emptyPlanets, boolean emptyMoons) {
		System.out.println(getFrame());
		if (emptyPlanets) {
			System.out.println("Hey, cosa stai cercando? Il sistema e' vuoto!");
			pressEnterToContinue();
			return;
		}

		System.out.println("	1. Rimuovi Pianeta");
		if (emptyMoons) {
			System.out.println("	2. Esci");
			return;
		}
		System.out.println("	2. Rimuovi Luna");
		System.out.println("	3. Esci");
	}

	protected static void clearConsole() {
		try {
			if (IS_WINDOWS)
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
		} catch (Exception e) {
		}
	}

	protected static void pressEnterToContinue() {
		System.out.print("\n\nPremi Invio per continuare...");
		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}