package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a star, the center of its solar system.
 */
public class Star extends CelestialBody{
    private final List<Planet> planets;
    private int numberOfPlanets = 0;
    
    private static int numberOfStars = 0;

    /**
     * The maximum number of planets that can orbit a star.
     */
    public static final int MAX_NUMBER_OF_PLANETS = 26000;

    //Package-private because it should be instantiated ONLY from a SolarSystem's constructor.
    Star(Position starPosition, long starMass) {
        super(starPosition, starMass, "S" + (numberOfStars + 1));
        planets = new ArrayList<>();
        numberOfStars++;
    }
    //Package-private because it should be instantiated ONLY from a SolarSystem's constructor.
    Star(double x, double y, long starMass) {
        this(new Position(x,y), starMass);
    }

    /**
     * Getter method for the absolute position of the star.
     * @return Returns the position of the star relaltive to an arbitrary origin,
     * equivalent to {@link CelestialBody#getRelativePosition()}.
     * @see CelestialBody#getRelativePosition()
     * @see Position
     */
    @Override
    public Position getPosition() {
        return getRelativePosition();
    }

    /**
     * Getter method for the list of planets orbiting the star.
     * @return the list of Planet instances orbiting the star.
     * @see Planet
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    //Counter of planets created around the star, used to make unique identifiers.
    int getNumberOfPlanets(){
        return numberOfPlanets;
    }


    /**
     * Searches for an instance of a planet around the star given its identifier.
     * WARNING: It does not search for planets orbiting other stars.
     * WARNING: A celestial body that is not a planet will not be found given its identifier.
     * @param identifier The identifier of the searched planet.
     * @return the instance of the planet searched. May return null if the planet is not found.
     * @see Planet
     */
    public Planet findPlanet(String identifier){
        for(var planet : getPlanets()){
            if(identifier.equals(planet.getIdentifier())){
                return planet;
            }
        }
        return null;
    }

    /**
     * Creates a new planet orbiting the star.
     * WARNING: It may not create the planet if it exceeds the max number.
     * @param planetRelativePosition The new planet's position relative to the star.
     * @param planetMass The new planet's mass.
     * @see Planet
     */
    public void addNewPlanet(Position planetRelativePosition, long planetMass){
        if(planets.size() < MAX_NUMBER_OF_PLANETS){
            planets.add(new Planet(planetRelativePosition,planetMass,this));
            numberOfPlanets++;
        }
    }

    /**
     * Creates a new planet orbiting the star.
     * WARNING: It may not create the planet if it exceeds the max number.
     * @param relativeX the new planet's offset along the x-axis relative to the star.
     * @param relativeY the new planet's offset along the y-axis relative to the star.
     * @param planetMass The new planet's mass.
     * @see Planet
     */
    public void addNewPlanet(long relativeX, long relativeY, long planetMass){
        addNewPlanet(new Position(relativeX,relativeY),planetMass);
    }

    //Removes a planet given its instance
    void removeOldPlanet(Planet planetToRemove){
        planets.remove(planetToRemove);
    }

    /**
     * Removes a planet orbiting the star given an identifier.
     * WARNING: If the identifier corresponds to a planet of another star, the planet will not be deleted.
     * @param identifier The identifier of the planet to be removed.
     * @see Planet
     */
    public void removeOldPlanet(String identifier){
        Planet planetToRemove = findPlanet(identifier);
        if(planetToRemove != null){
            planetToRemove.removeFromSystem();
        }
    }


    //Returns a list with the Star and the Planet as elements.
    ArrayList<CelestialBody> pathToPlanet(Planet planetToGo) throws IllegalArgumentException{
        if(!planetToGo.getStar().getIdentifier().equals(getIdentifier()))
            throw new IllegalArgumentException("I corpi celesti non appartengono allo stesso sistema.");

        var path = new ArrayList<CelestialBody>();
        path.add(this);
        path.add(planetToGo);
        return path;
    }
}