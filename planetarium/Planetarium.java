package planetarium;

import planetarium.solarsystem.*;
import planetarium.solarsystem.error.CelestialBodyNotFoundException;
import java.util.Random;

public class Planetarium {
	private static final String INVALID_NUMBER = "ATTENZIONE: Il numero inserito non e' valido!";

	private static final String INSERT_STAR_X = "Inserire la coordinata X della stella: ";
	private static final String INSERT_STAR_Y = "Inserire la coordinata Y della stella: ";
	private static final String INSERT_STAR_MASS = "Inserire la massa della stella: ";

	private static final String INSERT_PLANET_X = "Inserire coordinata X del pianeta (relativa alla sua stella): ";
	private static final String INSERT_PLANET_Y = "Inserire coordinata Y del pianeta (relativa alla sua stella): ";
	private static final String INSERT_PLANET_MASS = "Inserire la massa del pianeta: ";

	private static final String INSERT_MOON_X = "Inserire coordinata X della luna (relativa al suo pianeta): ";
	private static final String INSERT_MOON_Y = "Inserire coordinata Y della luna (relativa al suo pianeta): ";
	private static final String INSERT_MOON_MASS = "Inserire la massa della luna: ";
	
	public static void main(String[] args) {
		SolarSystem system = introduction();

		byte choice;
		do {
			Menu.planetarium();
			Menu.printMainMenu();
			choice = Input.choice();
			switch (choice) {
				case 1 -> addCelestialBody(system);
				case 2 -> removeCelestialBody(system);
				case 3 -> infoCelestialBody(system);
				case 4 -> showListCelestialBodies(system);
				case 5 -> getCenterOfMass(system);
				case 6 -> calculateRoute(system);
				case 7 -> showCollisions(system);
				case 8 -> generateTest(system);
				case 9 -> {
					return;
				}
				default -> System.out.println(INVALID_NUMBER);
			}
			Menu.clearConsole();
		} while (true);
	}

	//First interaction with user.
	private static SolarSystem introduction() {
		Menu.clearConsole();
		System.out.println("Hey, da qualche parte bisogna pur cominciare...\n");

		double x  = Input.readDouble(INSERT_STAR_X);
		double y  = Input.readDouble(INSERT_STAR_Y);
		long mass = Input.readLong(INSERT_STAR_MASS);

		Menu.clearConsole();
		return new SolarSystem(x, y, mass);
	}

	//Main Switch Case 1: print menù and await user input.
	//If the SolarSystem is empty, the menu will be different.
	private static void addCelestialBody(SolarSystem system) {
		Star star = system.getStar();
		boolean emptyPlanets = star.getPlanets().isEmpty();
		Menu.printAddCelestialBodyMenu(emptyPlanets);
		byte scelta;
		do {
			scelta = Input.choice();
			switch (scelta) {
				case 1 -> {
					addPlanet(system);
					return;
				}
				case 2 -> {
					if (emptyPlanets)
						return;
					addMoon(system);
					return;
				}
				case 3 -> {
					if (emptyPlanets) {
						System.out.println(INVALID_NUMBER);
						break;
					}
					return;
				}
				default -> System.out.println(INVALID_NUMBER);
			}
		} while (true);
	}

	//Gets values from user input and creates a new object Planet.
	private static void addPlanet(SolarSystem system) {
		Star star = system.getStar();
		double x = Input.readDouble(INSERT_PLANET_X);
		double y = Input.readDouble(INSERT_PLANET_Y);
		long mass = Input.readLong(INSERT_PLANET_MASS);
		star.addNewPlanet(x, y, mass);
	}

	//Gets values from user input and creates a new object Moon.
	//If planet code is invalid, it will ask planet's ID again.
	private static void addMoon(SolarSystem system) {
		String id;
		Star star = system.getStar();
		Planet planet;

		while(true) {
			try {
				id = Input.readString("Inserire ID del pianeta della luna: ");
				planet = star.findPlanet(id);
				break;
			} catch(CelestialBodyNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		double x = Input.readDouble(INSERT_MOON_X);
		double y = Input.readDouble(INSERT_MOON_Y);
		long mass = Input.readLong(INSERT_MOON_MASS);
		planet.addNewMoon(x, y, mass);
	}

	//Main Switch Case 2: print menù and await user input.
	//If the SolarSystem is empty, the menù will be different.
	private static void removeCelestialBody(SolarSystem system) {
		var planets = system.getStar().getPlanets();
		boolean emptyPlanets = planets.isEmpty();
		boolean emptyMoons = true;

		if (emptyPlanets) {
			Menu.printRemoveCelestialBodyMenu(emptyPlanets, emptyMoons);
			return;
		} else {
			for (var planet : planets) {
				if (!planet.getMoons().isEmpty()) {
					emptyMoons = false;
					break;
				}
			}
			Menu.printRemoveCelestialBodyMenu(emptyPlanets, emptyMoons);
		}

		byte choice;
		do {
			choice = Input.choice();
			switch (choice) {
				case 1 -> {
					removePlanet(system);
					return;
				}
				case 2 -> {
					if (emptyMoons)
						return;
					removeMoon(system);
					return;
				}
				case 3 -> {
					if (emptyMoons) {
						System.out.println(INVALID_NUMBER);
						break;
					}
					return;
				}
				default -> System.out.println(INVALID_NUMBER);
			}
		} while (true);
	}

	//Gets planet's ID from user input and remove it.
	//If planet code is invalid, it will ask planet's ID again.
	private static void removePlanet(SolarSystem system) {
		while (true) {
			String idPlanet = Input.readString("Inserire ID del pianeta da rimuovere: ");
			try {
				system.getStar().findPlanet(idPlanet).removeFromSystem();
				return;
			} catch(CelestialBodyNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	//Gets moon's ID from user input and remove it.
	//If moon code is invalid, it will ask moon's ID again.
	private static void removeMoon(SolarSystem system) {
		while(true){
			String idLuna = Input.readString("\nInserire ID della luna: ");

			try {
				var found = (Moon)(system.findCelestialBody(idLuna));
				found.removeFromSystem();
				return;
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	//Main Switch Case 3: asks user to insert CelestialBody's ID.
	//If ID CelestialBody is found, it'll print all body's properties.
	private static void infoCelestialBody(SolarSystem system) {
		Menu.clearConsole();

		try {
			var body = system.findCelestialBody(Input.readString("Inserire ID del corpo celeste: "));
			System.out.printf("\n%s\n",body);
		} catch(CelestialBodyNotFoundException e) {
			System.out.printf("\n%s\n",e.getMessage());
		}

		Menu.pressEnterToContinue();
	}

	//Main Switch Case 4: prints the list of all bodies in the SolarSystem.
	private static void showListCelestialBodies(SolarSystem system) {
		Menu.clearConsole();
		var star = system.getStar();
		var planets = star.getPlanets();

		System.out.println(star.getIdentifier() + "\t\t\t" + star);

		for(int i=0; i < planets.size() - 1; i++) {
			var planet = planets.get(i);
			System.out.println(" |__ " + planet.getIdentifier() + "\t\t" + planet);
			var moons = planet.getMoons();
			for(var moon : moons) {
				System.out.println(" |      |__ " + moon.getIdentifier() + "\t" + moon);
			}
			System.out.println(" |");
		}
		var lastPlanet = planets.isEmpty() ? null : planets.get(planets.size()-1);
		if (lastPlanet != null) {
			System.out.println(" |__ " + lastPlanet.getIdentifier() + "\t\t" + lastPlanet);
			for(var moon : lastPlanet.getMoons()){
				System.out.println("        |__ " + moon.getIdentifier() + "\t" + moon);
			}
		}
		Menu.pressEnterToContinue();
	}

	//Main Switch Case 5: calculates and prints the center of mass.
	private static void getCenterOfMass(SolarSystem system) {
		Position centerOfMass = system.getCenterOfMass();
		Menu.clearConsole();
		System.out.println("Il centro di massa è alle coordinate (" + centerOfMass.getX() + ", " + centerOfMass.getY() + ")");
		Menu.pressEnterToContinue();
	}

	//Main Switch Case 6: prompts the user to enter the ID of the starting and ending Celestial Body.
	private static void calculateRoute(SolarSystem system) {
		Menu.clearConsole();
		String path;
		while(true) {
			try {
				String id1 = Input.readString("Inserire l'identificativo del primo corpo celeste: ");
				String id2 = Input.readString("Inserire l'identificativo del secondo corpo celeste: ");

				path = system.findPath(id1, id2);
				break;
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println(path.concat("\n\n"));
		Menu.pressEnterToContinue();
	}

	//Main Switch Case 7: it prints if there could be collisions.
	private static void showCollisions(SolarSystem system) {
		Menu.clearConsole();
		if (system.detectCollisions())
			System.out.println("ATTENZIONE!!! Possibili collisioni tra corpi celesti!");
		else
			System.out.println("Tutto tranquillo. Nessuna collisione rilevata.");
		Menu.pressEnterToContinue();
	}

	//Main Switch Case 8: automatically generates planets and moons.
	private static Object generateTest(SolarSystem system) {
		Star star = system.getStar();
		int numOfPlanets, numOfMoons;

		Menu.clearConsole();
		System.out.println("Genera randomicamente i pianeti e le lune per te ;)");

		while(true) {
			numOfPlanets = Input.readInt(String.format("\nQuanti pianeti vuoi generare? [Max %d]\nInserisci una quantita': ",Star.MAX_NUMBER_OF_PLANETS));

			if (numOfPlanets <= Star.MAX_NUMBER_OF_PLANETS)
				break;
			System.out.printf("Sono ammessi un massimo di %d pianeti!",Star.MAX_NUMBER_OF_PLANETS);
			Menu.pressEnterToContinue();
			Menu.clearConsole();
		}

		while(true) {
			numOfMoons = Input.readInt(String.format("\nQuante lune vuoi generare per pianeta? [Max %d]\nInserisci una quantita': ",Planet.MAX_NUMBER_OF_MOONS));

			if (numOfMoons <= Planet.MAX_NUMBER_OF_MOONS)
				break;
			System.out.printf("Sono ammessi un massimo di %d lune!",Planet.MAX_NUMBER_OF_MOONS);
			Menu.pressEnterToContinue();
			Menu.clearConsole();
		}

		Random random = new Random();

		for (int i=0; i<numOfPlanets; i++) {
			double x = random.nextDouble()*100000;
			double y = random.nextDouble()*100000;
			long mass = Math.abs(random.nextLong()%100000 + 1);
			star.addNewPlanet(x, y, mass);
		}

		for (var planet : star.getPlanets()) {
			for (int i=0; i<numOfMoons; i++) {
				double x = random.nextDouble()*10000;
				double y = random.nextDouble()*10000;
				long mass = Math.abs(random.nextLong()%10000 + 1);
				planet.addNewMoon(x, y, mass);
			}
		}

		return null;
	}
}