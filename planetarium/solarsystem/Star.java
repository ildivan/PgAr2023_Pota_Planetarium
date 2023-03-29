package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Star extends CelestialBody{
    private final List<Planet> planets;

    public static final int MAX_NUMBER_OF_PLANETS = 26000;

    Star(Position starPosition, long starMass) {
        super(starPosition, starMass);
        planets = new ArrayList<>();
    }

    Star(double x, double y, long starMass) {
        super(x, y, starMass);
        planets = new ArrayList<>();
    }

    @Override
    public Position getPosition() {
        return getRelativePosition();
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public Planet findPlanet(int identifier){
        for(var planet : getPlanets()){
            if(identifier == planet.getIdentifier()){
                return planet;
            }
        }
        return null;
    }

    public void addNewPlanet(Position planetRelativePosition, long mass){
        if(planets.size() < MAX_NUMBER_OF_PLANETS){
            planets.add(new Planet(planetRelativePosition,mass,this));
        }
    }

    public void addNewPlanet(long relativeX, long relativeY, long mass){
        if(planets.size() < MAX_NUMBER_OF_PLANETS){
            planets.add(new Planet(relativeX,relativeY,mass,this));
        }
    }
}
