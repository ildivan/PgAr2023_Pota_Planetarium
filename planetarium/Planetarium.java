package planetarium;

import planetarium.solarsystem.*;
import planetarium.solarsystem.error.CelestialBodyNotFoundException;
import planetarium.solarsystem.error.PathBetweenDifferentSystemException;

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

	private static final String NUMBER_OF_PLANET_TO_GENERATE_PROMPT = "\nQuanti pianeti vuoi generare? [Max %d]\nInserisci una quantita': ";
	private static final String NUMBER_OF_MOONS_PER_PLANET_TO_GENERATE_PROMPT = "\nQuante lune vuoi generare per pianeta? [Max %d]\nInserisci una quantita': ";

	private static final String GENERATE_PLANET_ERROR_MESSAGE = "Sono ammessi un massimo di %d pianeti!";
	private static final String GENERATE_MOON_ERROR_MESSAGE = "Sono ammesse un massimo di %d lune per pianeta!";

	public static void main(String[] args) {
		SolarSystem system = introduction();

		byte choice;
		do {
			//Advised full screen
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
				case 9 -> clearSystem(system);
				case 10 -> {
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

		if(emptyPlanets){
			Menu.printAddPlanetMenu();
			addPlanetOrExit(system);
		}else{
			Menu.printAddCelestialBodyMenu();
			addCelestialBodyOrExit(system);
		}
	}
	//Gets the choice for the add celestial body menu in case there are no planets
	private static void addPlanetOrExit(SolarSystem system){
		while (true) {
			byte choice = Input.choice();
			switch (choice) {
				case 1 -> {
					addPlanet(system);
					return;
				}
				case 2 -> {return;}
				default -> System.out.println(INVALID_NUMBER);
			}
		}
	}
	//Gets choice for the add celestial body menu.
	private static void addCelestialBodyOrExit(SolarSystem system){
		while (true) {
			byte choice = Input.choice();
			switch (choice) {
				case 1 -> {
					addPlanet(system);
					return;
				}
				case 2 -> {
					addMoon(system);
					return;
				}
				case 3 -> {return;}
				default -> System.out.println(INVALID_NUMBER);
			}
		}
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

		for (var planet : planets) {
			if (!planet.getMoons().isEmpty()) {
				emptyMoons = false;
				break;
			}
		}

		if(emptyPlanets){
			Menu.printRemoveEmptyMenu();
		}else if(emptyMoons){
			Menu.printRemovePlanetMenu();
			removePlanetOrExit(system);
		}else{
			Menu.printRemoveCelestialBodyMenu();
			removeCelestialBodyOrExit(system);
		}
	}
	//Gets the choice for the menu in case there are no moons in the system.
	private static void removePlanetOrExit(SolarSystem system){
		while(true){
			byte choice = Input.choice();
			switch (choice) {
				case 1 -> {
					removePlanet(system);
					return;
				}
				case 2 -> {return;}
				default -> System.out.println(INVALID_NUMBER);
			}
		}
	}
	//Gets the choice for the remove celestial body menu.
	private static void removeCelestialBodyOrExit(SolarSystem system){
		while(true){
			byte choice = Input.choice();
			switch (choice) {
				case 1 -> {
					removePlanet(system);
					return;
				}
				case 2 -> {
					removeMoon(system);
					return;
				}
				case 3 -> {return;}
				default -> System.out.println(INVALID_NUMBER);
			}
		}
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
		printCelestialBodyTree(system);
		Menu.pressEnterToContinue();
	}
	//Prints the structure of the entire solar system
	private static void printCelestialBodyTree(SolarSystem system){
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
	}




	//Main Switch Case 5: calculates and prints the center of mass.
	private static void getCenterOfMass(SolarSystem system) {
		Position centerOfMass = system.getCenterOfMass();
		Menu.clearConsole();
		System.out.printf("Il centro di massa è alle coordinate ( %.3f, %.3f )\n",centerOfMass.getX(),centerOfMass.getY());
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
			} catch(PathBetweenDifferentSystemException e) {
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
	private static void generateTest(SolarSystem system) {
		Star star = system.getStar();
		int numOfPlanets, numOfMoons;

		Menu.clearConsole();
		System.out.println("Genera randomicamente i pianeti e le lune per te ;)");

		numOfPlanets = getNumberOfBodiesToTest(NUMBER_OF_PLANET_TO_GENERATE_PROMPT
				,GENERATE_PLANET_ERROR_MESSAGE,Star.MAX_NUMBER_OF_PLANETS);
		numOfMoons = getNumberOfBodiesToTest(NUMBER_OF_MOONS_PER_PLANET_TO_GENERATE_PROMPT
				,GENERATE_MOON_ERROR_MESSAGE,Planet.MAX_NUMBER_OF_MOONS);

		generateTestCelestialBodies(star,numOfPlanets);
		for(var planet : star.getPlanets()){
			generateTestCelestialBodies(planet,numOfMoons);
		}
	}
	//Gets how many of a type of celestial body to generate for the testing.
	private static int getNumberOfBodiesToTest(String message, String errorMessage,long maxNumber){
		while(true) {
			int numberOfBodiesToTest = Input.readInt(String.format(message,maxNumber));

			if (numberOfBodiesToTest <= maxNumber)
				return numberOfBodiesToTest;

			System.out.printf(errorMessage,maxNumber);
			Menu.pressEnterToContinue();
			Menu.clearConsole();
		}
	}
	//Generates the celestial bodies to test.
	private static void generateTestCelestialBodies(CelestialBody parent, int numberOfBodiesToGenerate){
		Random random = new Random();

		int maxVal = (parent instanceof Star ? 200000 : 10000);

		for (int i=0; i<numberOfBodiesToGenerate; i++) {
			double x = (random.nextBoolean() ? 1.0 : -1.0) * random.nextDouble()*maxVal;
			double y = (random.nextBoolean() ? 1.0 : -1.0) * random.nextDouble()*maxVal;
			long mass = Math.abs(random.nextLong()%maxVal + 1);
			if(parent instanceof Star star) star.addNewPlanet(x, y, mass);
			else if(parent instanceof Planet planet) planet.addNewMoon(x, y, mass);
		}
	}

	//Main switch case 9: deletes all planets and moons (identifiers are not reset)
	private static void clearSystem(SolarSystem system){
		Menu.clearConsole();
		final Star star = system.getStar();
		//Can't loop through the planets and use planet.removeFromSystem() because it calls an exception.
		star.getPlanets().clear();
		System.out.println("Tutti i pianeti e lune sono stati cancellati!");
		Menu.pressEnterToContinue();
	}
}