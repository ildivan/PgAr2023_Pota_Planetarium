package planetarium;

import planetarium.solarsystem.*;

public class Planetarium {

	private static final String INVALID_NUMBER = "ATTENZIONE: Il numero inserito non e' valido!";

	public static void main(String[] args) {
		
		SolarSystem system = new SolarSystem(0, 0, 3);

		Menu.welcome();

		byte choice;
		do {
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
		} while (true);
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
		System.out.print("Inserire coordinate X del pianeta: ");
		long x = Input.readLong();
		System.out.print("\nInserire coordinate Y del pianeta: ");
		long y = Input.readLong();
		System.out.print("\nInserire la massa del pianeta [Kg]: ");
		long mass = Input.readLong();
		star.addNewPlanet(x, y, mass);
	}

	private static void addMoon(SolarSystem system) {
		String id;
		Star star = system.getStar();

		while(true){
			System.out.print("Inserire ID del pianeta della luna: ");
			id = Input.readString();

			if(star.findPlanet(id) != null)
				break;
			System.out.println("Pianeta non trovato.");
		}

		System.out.print("Inserire coordinate X della luna: ");
		long x = Input.readLong();
		System.out.print("\nInserire coordinate Y della luna: ");
		long y = Input.readLong();
		System.out.print("\nInserire la massa della luna [Kg]: ");
		long mass = Input.readLong();
		star.findPlanet(id).addNewMoon(x,y,mass);
	}

	private static void removeCelestialBody(SolarSystem system) {
		Menu.printRemoveCelestialBodyMenu();
		byte choice;
		do {
			choice = Input.choice();
			switch (choice) {
				case 1 -> {
					removePlanet(system);
					return;
				}
				case 2 -> {
					removeMoon(system);
					return;
				}
				case 3 -> {
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
			System.out.print("Inserire ID del pianeta da rimuovere: ");
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
			System.out.print("Inserire ID della luna: ");
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

	}

	private static void showListCelestialBodies(SolarSystem system) {
		var star = system.getStar();
		System.out.println(star.getIdentifier());
		
		var planets = star.getPlanets();

		for(int i=0; i < planets.size() - 1; i++){
			var planet = planets.get(i);
			System.out.println(" |__ " + planet.getIdentifier());
			var moons = planet.getMoons();
			for(var moon : moons) {
				System.out.println(" |      |__ " + moon.getIdentifier());
			}
		}
		var lastPlanet = planets.get(planets.size()-1);
		System.out.println(" |__ " + lastPlanet.getIdentifier());
		for(var moon : lastPlanet.getMoons()){
			System.out.println("        |__ " + moon.getIdentifier());
		}
			
	}

	private static void getCenterOfMass(SolarSystem system) {

	}

	private static void calculateRoute(SolarSystem system) {

	}

	private static void showCollisions(SolarSystem system) {

	}
}	