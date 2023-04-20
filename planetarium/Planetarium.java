package planetarium;

import planetarium.solarsystem.*;
import planetarium.solarsystem.error.CelestialBodyNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planetarium {
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
				case 6 -> calculatePath(system);
				case 7 -> showCollisions(system);
				case 8 -> generateTest(system);
				case 9 -> {
					return;
				}
				default -> System.out.println(Literals.INVALID_NUMBER);
			}
			Menu.clearConsole();
		} while (true);
	}
	
	//Read user input and checks the value is positive.
	private static long readCelestialBodyMass(String message){
		while(true){
			long mass = Input.readLong(message);
			if(mass > 0) return mass;
			System.out.println("La massa non può essere negativa.");
		}
	}
	
	//First interaction with user.
	private static SolarSystem introduction() {
		Menu.clearConsole();
		System.out.println("Hey, da qualche parte bisogna pur cominciare...\n");

		double x  = Input.readDouble(Literals.INSERT_STAR_X);
		double y  = Input.readDouble(Literals.INSERT_STAR_Y);
		long mass = readCelestialBodyMass(Literals.INSERT_STAR_MASS);
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
				default -> System.out.println(Literals.INVALID_NUMBER);
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
				default -> System.out.println(Literals.INVALID_NUMBER);
			}
		}
	}
	//Gets values from user input and creates a new object Planet.
	private static void addPlanet(SolarSystem system) {
		Star star = system.getStar();
		double x = Input.readDouble(Literals.INSERT_PLANET_X);
		double y = Input.readDouble(Literals.INSERT_PLANET_Y);
		long mass = readCelestialBodyMass(Literals.INSERT_PLANET_MASS);
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
				id = Input.readString(Literals.INSERT_PLANET_ID_FOR_MOON_CREATION);
				planet = star.findPlanet(id);
				break;
			} catch(CelestialBodyNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		double x = Input.readDouble(Literals.INSERT_MOON_X);
		double y = Input.readDouble(Literals.INSERT_MOON_Y);
		long mass = readCelestialBodyMass(Literals.INSERT_MOON_MASS);
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
				default -> System.out.println(Literals.INVALID_NUMBER);
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
				default -> System.out.println(Literals.INVALID_NUMBER);
			}
		}
	}
	//Gets planet's ID from user input and remove it.
	//If planet code is invalid, it will ask planet's ID again.
	private static void removePlanet(SolarSystem system) {
		while (true) {
			String idPlanet = Input.readString(Literals.INSERT_PLANET_ID_TO_REMOVE);
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
			String idLuna = Input.readString(Literals.INSERT_MOON_ID);

			try {
				var found = (Moon)(system.findCelestialBody(idLuna));
				found.removeFromSystem();
				return;
			} catch(Exception e) {
				if(e instanceof ClassCastException){
					System.out.println(Literals.NOT_A_MOON_ID);
				}else{
					System.out.println(e.getMessage());
				}
			}
		}
	}





	//Main Switch Case 3: asks user to insert CelestialBody's ID.
	//If ID CelestialBody is found, it'll print all body's properties.
	private static void infoCelestialBody(SolarSystem system) {
		Menu.clearConsole();

		try {
			var body = system.findCelestialBody(Input.readString(Literals.INSERT_CELESTIAL_BODY_ID));
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

		printStar(star);

		for(int i=0; i < planets.size() - 1; i++) {
			var planet = planets.get(i);

			printPlanetAndMoons(planet,false);
			System.out.println(" |");
		}

		if (!planets.isEmpty()) printPlanetAndMoons(planets.get(planets.size()-1),true);
	}
	//Prints the star.
	private static void printStar(Star star){
		System.out.printf(Literals.STAR_FORMAT,star.getIdentifier(),star.toStringWithoutID());
	}
	//Prints planet and its moons based on the format given.
	private static void printPlanetAndMoons(Planet planet,boolean isLastPlanet){
		String planetFormat = (isLastPlanet ? Literals.LAST_PLANET_FORMAT : Literals.PLANET_FORMAT);
		String moonFormat = (isLastPlanet ? Literals.LAST_PLANET_MOON_FORMAT : Literals.MOON_FORMAT);

		System.out.printf(planetFormat,planet.getIdentifier(),planet.toStringWithoutID());
		for(var moon : planet.getMoons()){
			System.out.printf(moonFormat,moon.getIdentifier(),moon.toStringWithoutID());
		}
	}



	//Main Switch Case 5: calculates and prints the center of mass.
	private static void getCenterOfMass(SolarSystem system) {
		Position centerOfMass = system.getCenterOfMass();
		Menu.clearConsole();
		System.out.printf(Literals.CENTER_OF_MASS_FORMAT,centerOfMass.getX(),centerOfMass.getY());
		Menu.pressEnterToContinue();
	}



	//Main Switch Case 6: prompts the user to enter the ID of the starting and ending Celestial Body.
	private static void calculatePath(SolarSystem system) {
		Menu.clearConsole();
		String path = getPath(system);
		System.out.println(path.concat("\n\n"));
		Menu.pressEnterToContinue();
	}

	private static String getPath(SolarSystem system){
		while(true) {
			try {
				String id1 = Input.readString(Literals.INSERT_FIRST_BODY_ID);
				String id2 = Input.readString(Literals.INSERT_SECOND_BODY_ID);

				return pathToString(system.findPath(id1, id2));
			} catch(CelestialBodyNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}


	//Main Switch Case 7: it prints if there could be collisions.
	private static void showCollisions(SolarSystem system) {
		Menu.clearConsole();
		if (system.detectCollisions())
			System.out.println(Literals.POSSIBLE_COLLISIONS);
		else
			System.out.println(Literals.NO_COLLISIONS);
		Menu.pressEnterToContinue();
	}




	//Main Switch Case 8: automatically generates planets and moons.
	private static void generateTest(SolarSystem system) {
		Star star = system.getStar();
		int numOfPlanets, numOfMoons;

		if (!system.getStar().getPlanets().isEmpty())
			clearSystem(system);

		Menu.clearConsole();
		System.out.println("Genera randomicamente i pianeti e le lune per te ;)");

		numOfPlanets = getNumberOfBodiesToTest(Literals.NUMBER_OF_PLANET_TO_GENERATE_PROMPT
				,Literals.GENERATE_PLANET_ERROR_MESSAGE,Star.MAX_NUMBER_OF_PLANETS);
		numOfMoons = getNumberOfBodiesToTest(Literals.NUMBER_OF_MOONS_PER_PLANET_TO_GENERATE_PROMPT
				,Literals.GENERATE_MOON_ERROR_MESSAGE,Planet.MAX_NUMBER_OF_MOONS);

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
		star.removeAllPlanets();
		System.out.println(Literals.ALL_PLANETS_CANCELLED);
		Menu.pressEnterToContinue();
	}

	//Converts list of celestial bodies to a string representing a path.
	//Does not check if the path makes sense, that is up to the methods that creates it.
	private static String pathToString(List<CelestialBody> path) {
		if (path == null || path.isEmpty()) return "";

		StringBuilder sPath = new StringBuilder(String.format("\n%s ", path.get(0).getIdentifier()));

		for(int i = 1; i < path.size(); i++)
			sPath.append(String.format(" > %s", path.get(i).getIdentifier()));

		//Appends the length of the path
		sPath.append(String.format("\ndistanza totale : %.3f", totalDistanceOfPath(path)));

		return sPath.toString();
	}

	//Calculates the total distance of the path between two celestial bodies.
	private static double totalDistanceOfPath(List<CelestialBody> path) {
		long totalDistance = 0;

		for (int i = 0; i < path.size() - 1; i++) {
			//There's not a risk of going out of bounds because path is at least two elements.
			CelestialBody current = path.get(i);
			CelestialBody next = path.get(i+1);

			//Assuming that from a moon you can go only to its planet.
			if(current instanceof Moon currentMoon){
				totalDistance += currentMoon.distanceToParent();
			}//Assuming that from a planet you can go to one of its moons or to its star.
			else if(current instanceof Planet currentPlanet){
				if(next instanceof Moon nextMoon){
					totalDistance += nextMoon.distanceToParent();
				}else{
					totalDistance += currentPlanet.distanceToParent();
				}
			}//Assuming that from a star you can only go to one of its planets.
			else{
				totalDistance += ((Planet) next).distanceToParent();
			}
		}

		return totalDistance;
	}
}