package planetarium;

import java.util.InputMismatchException;
import java.util.Scanner;

import planetarium.solarsystem.SolarSystem;

public class Planetarium {

	private static final String DIGITA_NUMERO_OPZIONE_DESIDERATA = "\nDigita il numero dell'opzione desiderata > ";

	private static void welcome() {
		System.out.println("\n"
						+ "\t██████╗░██╗░░░░░░█████╗░███╗░░██╗███████╗████████╗░█████╗░██████╗░██╗██╗░░░██╗███╗░░░███╗\n"
						+ "\t██╔══██╗██║░░░░░██╔══██╗████╗░██║██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██║██║░░░██║████╗░████║\n"
						+ "\t██████╔╝██║░░░░░███████║██╔██╗██║█████╗░░░░░██║░░░███████║██████╔╝██║██║░░░██║██╔████╔██║\n"
						+ "\t██╔═══╝░██║░░░░░██╔══██║██║╚████║██╔══╝░░░░░██║░░░██╔══██║██╔══██╗██║██║░░░██║██║╚██╔╝██║\n"
						+ "\t██║░░░░░███████╗██║░░██║██║░╚███║███████╗░░░██║░░░██║░░██║██║░░██║██║╚██████╔╝██║░╚═╝░██║\n"
						+ "\t╚═╝░░░░░╚══════╝╚═╝░░╚═╝╚═╝░░╚══╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░╚═════╝░╚═╝░░░░░╚═╝");
		System.out.println("\nBenvenuto al gestionale degli Xylophaxians");
	}

	private static byte scelta() {
		byte scelta;
		do {
			System.out.print(DIGITA_NUMERO_OPZIONE_DESIDERATA);
			try {
				Scanner scanner = new Scanner(System.in);
				scelta = scanner.nextByte();
				return scelta;
			} catch (InputMismatchException e) {
				System.out.println("Attenzione: il dato inserito non e' nel formato corretto");
			}
		} while (true);
	}

	public static void main(String[] args) {
		var system = new SolarSystem(0, 0, 3);
		var star = system.getStar();

		welcome();
		Menu.printMainMenu();

		byte scelta;
		do {
			scelta = scelta();
			switch (scelta) {
			case 1:
				addCelestialBody();
				break;
			case 2:
				removeCelestialBody();
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
				continue;
			default:
				System.out.println("ATTENZIONE: Il numero inserito non e' valido!");
				break;
			}
		} while (scelta != 8);

		for (int i = 0; i < 1000; i++) {
			star.addNewPlanet(i, i, i);
		}

		for (var planet : star.getPlanets()) {
			for (int i = 0; i < 1000; i++) {
				planet.addNewMoon(i, i, i);
			}
		}

		star.findPlanet(2).removeFromSystem();
	}

	private static void addCelestialBody() {
		Menu.printAddCelestialBodyMenu();
	}

	private static void removeCelestialBody() {
		Menu.printRemoveCelestialBodyMenu();
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