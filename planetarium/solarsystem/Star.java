package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Star extends CelestialBody{
    private final List<Planet> planets;

    Star(Position starPosition, long starMass) {
        super(starPosition, starMass);
        planets = new ArrayList<Planet>();
    }

    Star(long x, long y, long starMass) {
        super(x, y, starMass);
        planets = new ArrayList<Planet>();
    }

    public List<Planet> getPlanets() {
        return Collections.unmodifiableList(planets);
    }

    public void addNewPlanet(Position planetRelativePosition, long mass){
        planets.add(new Planet(planetRelativePosition,mass,this));
    }

    public void addNewPlanet(long relativeX, long relativeY, long mass){
        planets.add(new Planet(relativeX,relativeY,mass,this));
    }

}
