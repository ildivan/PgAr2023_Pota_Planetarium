package planetarium;

import planetarium.solarsystem.SolarSystem;
import planetarium.solarsystem.Star;

public class Planetarium {

	private static final String INVALID_NUMBER = "ATTENZIONE: Il numero inserito non e' valido!";

	public static void main(String[] args) {
		
		SolarSystem system = new SolarSystem(0, 0, 3);
		Star star = system.getStar();

		Menu.welcome();

		byte choice;
		do {
			Menu.printMainMenu();
			choice = Input.choice();
			switch (choice) {
			case 1:
				addCelestialBody(star);
				break;
			case 2:
				removeCelestialBody(star);
				break;
			case 3:
				infoCelestialBody();
				break;
			case 4:
				showListCelestialBodies();
				break;
			case 5:
				getCenterOfMass();
				break;
			case 6:
				calculateRoute();
				break;
			case 7:
				showCollisions();
				break;
			case 8:
				return;
			default:
				System.out.println(INVALID_NUMBER);
				break;
			}
		} while (true);
	}

	private static void addCelestialBody(Star star) {
		Menu.printAddCelestialBodyMenu();
		byte scelta;
		do {
			scelta = Input.choice();
			switch (scelta) {
			case 1:
				addPlanet(star);
				return;
			case 2:
				addMoon(star);
				return;
			case 3:
				return;
			default:
				System.out.println(INVALID_NUMBER);
				break;
			}
		} while (true);
	}

	private static void addPlanet(Star star) {
		System.out.print("Inserire coordinate X del pianeta: ");
		long x = Input.readLong();
		System.out.print("\nInserire coordinate Y del pianeta: ");
		long y = Input.readLong();
		System.out.print("\nInserire la massa del pianeta [Kg]: ");
		long massa = Input.readLong();
		star.addNewPlanet(x, y, massa);
	}

	private static void addMoon(Star star) {
		System.out.print("Inserire ID del pianeta della luna: ");
		int id = Input.readInt();
		System.out.print("Inserire coordinate X della luna: ");
		long x = Input.readLong();
		System.out.print("\nInserire coordinate Y della luna: ");
		long y = Input.readLong();
		System.out.print("\nInserire la massa della luna [Kg]: ");
		long mass = Input.readLong();
		// To-Do: creare nuovo oggetto luna
	}

	private static void removeCelestialBody(Star star) {
		Menu.printRemoveCelestialBodyMenu();
		byte choice;
		do {
			choice = Input.choice();
			switch (choice) {
			case 1:
				removePlanet(star);
				return;
			case 2:
				removeMoon(star);
				return;
			case 3:
				return;
			default:
				System.out.println(INVALID_NUMBER);
				break;
			}
		} while (true);
	}

	private static void removePlanet(Star star) {
		System.out.print("Inserire ID del pianeta da rimuovere: ");
		int id = Input.readInt();
		star.removeOldPlanet(id);
	}

	private static void removeMoon(Star star) {
		System.out.print("Inserire ID della luna da rimuovere: ");
		int id = Input.readInt();
		// To-Do: rimuovere oggetto luna
	}

	private static void infoCelestialBody() {

	}

	private static void showListCelestialBodies() {

	}

	private static void getCenterOfMass() {

	}

	private static void calculateRoute() {

	}

	private static void showCollisions() {

	}
}