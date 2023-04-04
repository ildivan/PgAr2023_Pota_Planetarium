package planetarium;

import planetarium.solarsystem.*;

public class Planetarium {

	private static final String INVALID_NUMBER = "ATTENZIONE: Il numero inserito non e' valido!";

	public static void main(String[] args) {
		SolarSystem system = new SolarSystem(0, 0, 3);

		Menu.clearConsole();

		byte choice;
		do {
			Menu.welcome();
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

	private static void addCelestialBody(SolarSystem system) {
		Star star = system.getStar();
		boolean emptyPlanets = star.getPlanets().isEmpty();
		Menu.clearConsole();
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
		System.out.print("\nInserire coordinate X del pianeta: ");
		long x = Input.readLong();
		System.out.print("Inserire coordinate Y del pianeta: ");
		long y = Input.readLong();
		System.out.print("Inserire la massa del pianeta [MKg]: ");
		long mass = Input.readLong();
		star.addNewPlanet(x, y, mass);
	}

	private static void addMoon(SolarSystem system) {
		String id;
		Star star = system.getStar();

		while(true){
			System.out.print("\nInserire ID del pianeta della luna: ");
			id = Input.readString();

			if(star.findPlanet(id) != null)
				break;
			System.out.println("Pianeta non trovato.");
		}

		System.out.print("\nInserire coordinate X della luna: ");
		long x = Input.readLong();
		System.out.print("Inserire coordinate Y della luna: ");
		long y = Input.readLong();
		System.out.print("Inserire la massa della luna [MKg]: ");
		long mass = Input.readLong();
		star.findPlanet(id).addNewMoon(x,y,mass);
	}

	private static void removeCelestialBody(SolarSystem system) {
		var planets = system.getStar().getPlanets();
		boolean emptyPlanets = planets.isEmpty();
		boolean emptyMoons = true;
		
		Menu.clearConsole();

		for (var planet : planets) {
			if (!planet.getMoons().isEmpty()) {
				emptyMoons = false;
				break;
			}
		}
		Menu.printRemoveCelestialBodyMenu(emptyPlanets, emptyMoons);
		
		if (emptyPlanets)
			return;

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
		String idPlanet;
		CelestialBody body;
		while (true) {
			System.out.print("\nInserire ID del pianeta da rimuovere: ");
			idPlanet = Input.readString();
			body = system.findCelestialBody(idPlanet);
			if (body != null && (body instanceof Planet))
				break;
			System.out.println("Pianeta non esistente, oppure non è un pianeta.");
		}
		if(body instanceof Planet planet){
			planet.removeFromSystem();
		}
	}

	private static void removeMoon(SolarSystem system) {
		String idLuna;
		CelestialBody body;
		while(true){
			System.out.print("\nInserire ID della luna: ");
			idLuna = Input.readString();
			body = system.findCelestialBody(idLuna);
			if (body != null && (body instanceof Moon))
				break;
			System.out.println("Luna non esistente, oppure non è una luna.");
		}

		if(body instanceof Moon moon){
			moon.removeFromSystem();
		}
	}

	private static void infoCelestialBody(SolarSystem system) {
		Menu.clearConsole();
		System.out.print("Inserire ID del corpo celeste: ");
		var body = system.findCelestialBody(Input.readString());
		if (body != null)
			System.out.println("\n"+body);
		else
			System.out.println("\nCorpo celeste non trovato. L'ID e' corretto?");
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