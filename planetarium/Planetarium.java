package planetarium;

import planetarium.solarsystem.*;
import planetarium.solarsystem.error.CelestialBodyNotFoundException;

public class Planetarium {
	private static final String INVALID_NUMBER = "ATTENZIONE: Il numero inserito non e' valido!";

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
				case 8 -> {
					return;
				}
				default -> System.out.println(INVALID_NUMBER);
			}
			Menu.clearConsole();
		} while (true);
	}

	private static SolarSystem introduction() {
		Menu.clearConsole();
		System.out.println("Hey, da qualche parte bisogna pur cominciare...\n");

		long x  = Input.readLong("Inserire la coordinata X della stella: ");
		long y  = Input.readLong("Inserire la coordinata Y della stella: ");
		long mass = Input.readLong("Inserire la massa della stella [MKg]: ");

		Menu.clearConsole();
		return new SolarSystem(x, y, mass);
	}

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

	private static void addPlanet(SolarSystem system) {
		Star star = system.getStar();
		long x = Input.readLong("Inserire coordinata X del pianeta (relativa alla sua stella): ");
		long y = Input.readLong("Inserire coordinate Y del pianeta (relativa alla sua stella): ");
		long mass = Input.readLong("Inserire la massa del pianeta [MKg]: ");
		star.addNewPlanet(x, y, mass);
	}

	private static void addMoon(SolarSystem system) {
		String id;
		Star star = system.getStar();
		Planet planet;

		while(true) {
			id = Input.readString("Inserire ID del pianeta della luna: ");

			try{
				planet = star.findPlanet(id);
				break;
			}catch(CelestialBodyNotFoundException e){
				System.out.println(e.getMessage());
			}

		}

		long x = Input.readLong("Inserire coordinata X della luna (relativa al suo pianeta): ");
		long y = Input.readLong("Inserire coordinata Y della luna (relativa al suo pianeta): ");
		long mass = Input.readLong("Inserire la massa della luna [MKg]: ");
		planet.addNewMoon(x, y, mass);
	}

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

	private static void removePlanet(SolarSystem system) {
		while (true) {
			String idPlanet = Input.readString("Inserire ID del pianeta da rimuovere: ");
			try{
				system.getStar().findPlanet(idPlanet).removeFromSystem();
				return;
			}catch(CelestialBodyNotFoundException e){
				System.out.println(e.getMessage());
			}
		}
	}

	private static void removeMoon(SolarSystem system) {
		while(true){
			String idLuna = Input.readString("\nInserire ID della luna: ");

			try{
				var found = (Moon)(system.findCelestialBody(idLuna));
				found.removeFromSystem();
				return;
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

	private static void infoCelestialBody(SolarSystem system) {
		Menu.clearConsole();

		try{
			var body = system.findCelestialBody(Input.readString("Inserire ID del corpo celeste: "));
			System.out.printf("\n%s\n",body);
		}catch(CelestialBodyNotFoundException e){
			System.out.printf("\n%s\n",e.getMessage());
		}

		Menu.pressEnterToContinue();
	}

	private static void showListCelestialBodies(SolarSystem system) {
		Menu.clearConsole();
		var star = system.getStar();
		var planets = star.getPlanets();

		System.out.println(star.getIdentifier());

		for(int i=0; i < planets.size() - 1; i++){
			var planet = planets.get(i);
			System.out.println(" |__ " + planet.getIdentifier());
			var moons = planet.getMoons();
			for(var moon : moons) {
				System.out.println(" |      |__ " + moon.getIdentifier());
			}
		}
		var lastPlanet = planets.isEmpty() ? null : planets.get(planets.size()-1);
		if (lastPlanet != null) {
			System.out.println(" |__ " + lastPlanet.getIdentifier());
			for(var moon : lastPlanet.getMoons()){
				System.out.println("        |__ " + moon.getIdentifier());
			}
		}

		Menu.pressEnterToContinue();
	}

	private static void getCenterOfMass(SolarSystem system) {
		Position centerOfMass = system.getCenterOfMass();
		Menu.clearConsole();
		System.out.println("Il centro di massa è alle coordinate (" + centerOfMass.getX() + ", " + centerOfMass.getY() + ")");
		Menu.pressEnterToContinue();
	}

	private static void calculateRoute(SolarSystem system) {
		Menu.clearConsole();
		String path;
		while(true){
			try{
				String id1 = Input.readString("Inserire l'identificativo del primo corpo celeste: ");
				String id2 = Input.readString("Inserire l'identificativo del secondo corpo celeste: ");

				path = system.findPath(id1, id2);
				break;
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}

		System.out.println(path.concat("\n\n"));
		Menu.pressEnterToContinue();
	}

	private static void showCollisions(SolarSystem system) {
		Menu.clearConsole();
		if (system.detectCollisions())
			System.out.println("ATTENZIONE!!! Possibili collisioni tra corpi celesti!");
		else
			System.out.println("Tutto tranquillo. Nessuna collisione rilevata.");
		Menu.pressEnterToContinue();
	}
}