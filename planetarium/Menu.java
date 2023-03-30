package planetarium;

public class Menu {
	private static final String CORNICE = "――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

	protected static void printMainMenu() {
		System.out.println(CORNICE);
		System.out.println("	1. Aggiungi nuovo Pianeta/Luna");
		System.out.println("	2. Rimuovi nuova Pianeta/Luna");
		System.out.println("	3. Ottieni informazioni del corpo celeste");
		System.out.println("	4. Mostra la lista dei corpi di tutto il sistema");
		System.out.println("	5. Ottieni il centro di massa");
		System.out.println("	6. Calcola rotta tra due corpi");
		System.out.println("	7. Mostra possibili collisioni");
	}

	protected static void printAddCelestialBodyMenu() {
		System.out.println(CORNICE);
		System.out.println("	1. Aggiungi nuovo Pianeta");
		System.out.println("	2. Aggiungi nuova Luna");
	}

	protected static void printRemoveCelestialBodyMenu() {
		System.out.println(CORNICE);
		System.out.println("	1. Rimuovi Pianeta");
		System.out.println("	2. Rimuovi Luna");
	}
}
