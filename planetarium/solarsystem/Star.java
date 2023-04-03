package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.List;

public class Star extends CelestialBody{
    private final List<Planet> planets;
    private int numberOfPlanets = 0;
    
    private static int numberOfStars = 0;
    
    public static final int MAX_NUMBER_OF_PLANETS = 26000;

    Star(Position starPosition, long starMass) {
        super(starPosition, starMass, "S" + (numberOfStars + 1));
        planets = new ArrayList<>();
        numberOfStars++;
    }

    Star(double x, double y, long starMass) {
        this(new Position(x,y), starMass);
    }

    @Override
    public Position getPosition() {
        return getRelativePosition();
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public int getNumberOfPlanets(){
        return numberOfPlanets;
    }

    public Planet findPlanet(String identifier){
        for(var planet : getPlanets()){
            if(identifier.equals(planet.getIdentifier())){
                return planet;
            }
        }
        return null;
    }

    public void addNewPlanet(Position planetRelativePosition, long mass){
        if(planets.size() < MAX_NUMBER_OF_PLANETS){
            planets.add(new Planet(planetRelativePosition,mass,this));
            numberOfPlanets++;
        }
    }

    public void addNewPlanet(long relativeX, long relativeY, long mass){
        if(planets.size() < MAX_NUMBER_OF_PLANETS){
            planets.add(new Planet(relativeX,relativeY,mass,this));
            numberOfPlanets++;
        }
    }

    void removeOldPlanet(Planet planetToRemove){
        planets.remove(planetToRemove);
    }

    public void removeOldPlanet(String identifier){
        Planet planetToRemove = findPlanet(identifier);
        if(planetToRemove != null){
            planetToRemove.removeFromSystem();
        }
    }
}