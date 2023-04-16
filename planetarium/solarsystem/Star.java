package planetarium.solarsystem;

import planetarium.solarsystem.error.CelestialBodyNotFoundException;
import planetarium.solarsystem.error.PathBetweenDifferentSystemException;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Represent a star, the center of its solar system.
 */
public class Star extends CelestialBody {
    private final List<Planet> planets;
    private int numberOfPlanets = 0;
    
    private static int numberOfStars = 0;

    /**
     * The maximum number of planets that can orbit a star.
     */
    public static final int MAX_NUMBER_OF_PLANETS = 26000;

    //Protected because it should be instantiated ONLY from a SolarSystem's constructor.
    protected Star(Position starPosition, long starMass) {
        super(starPosition, starMass, "S" + (numberOfStars + 1));
        planets = new ArrayList<>();
        numberOfStars++;
    }
    //Protected because it should be instantiated ONLY from a SolarSystem's constructor.
    protected Star(double x, double y, long starMass) {
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
    public Position getAbsolutePosition() {
        return getRelativePosition();
    }

    /**
     * Getter method for the list of planets orbiting the star.
     * @return The list of Planet instances orbiting the star.
     * @see Planet
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    //Counter of planets created around the star, used to make unique identifiers.
    protected int getNumberOfPlanets() {
        return numberOfPlanets;
    }


    /**
     * Searches for an instance of a planet around the star given its identifier.
     * WARNING: It does not search for planets orbiting other stars.
     * WARNING: A celestial body that is not a planet will not be found given its identifier.
     * @param identifier The identifier of the searched planet.
     * @return The instance of the planet searched. May throw CelestialBodyNotFoundException if the planet is not found.
     * @see Planet
     * @see CelestialBodyNotFoundException
     */
    public Planet findPlanet(String identifier) throws CelestialBodyNotFoundException{
        for(var planet : getPlanets()){
            if(identifier.equals(planet.getIdentifier())){
                return planet;
            }
        }
        throw new CelestialBodyNotFoundException(identifier);
    }

    /**
     * Creates a new planet orbiting the star.
     * WARNING: It may not create the planet if it exceeds the max number.
     * @param planetRelativePosition The new planet's position relative to the star.
     * @param planetMass The new planet's mass.
     * @see Planet
     */
    public void addNewPlanet(Position planetRelativePosition, long planetMass) {
        if(planets.size() < MAX_NUMBER_OF_PLANETS){
            planets.add(new Planet(planetRelativePosition, planetMass, this));
            numberOfPlanets++;
        }
    }

    /**
     * Creates a new planet orbiting the star.
     * WARNING: It may not create the planet if it exceeds the max number.
     * @param relativeX The new planet's offset along the x-axis relative to the star.
     * @param relativeY The new planet's offset along the y-axis relative to the star.
     * @param planetMass The new planet's mass.
     * @see Planet
     */
    public void addNewPlanet(double relativeX, double relativeY, long planetMass) {
        addNewPlanet(new Position(relativeX, relativeY), planetMass);
    }

    //Removes a planet given its instance
    protected void removeOldPlanet(Planet planetToRemove) {
        planets.remove(planetToRemove);
    }

    /**
     * Removes a planet orbiting the star given an identifier.
     * WARNING: If the identifier corresponds to a planet of another star, the planet will not be deleted.
     * @param identifier The identifier of the planet to be removed.
     * @see Planet
     */
    public void removeOldPlanet(String identifier) throws CelestialBodyNotFoundException{
        findPlanet(identifier).removeFromSystem();
    }

    //Removes all planets and moon of the system.
    public void removeAllPlanets(){
        ListIterator<Planet> iter = getPlanets().listIterator();
        while(iter.hasNext()){
            iter.next().deleteStarReference();
            iter.remove();
        }
    }

    //Returns a list with the Star and the Planet as elements representing the path between them.
    protected ArrayList<CelestialBody> pathToPlanet(Planet planetToGo) throws PathBetweenDifferentSystemException {
        if(!planetToGo.getStar().getIdentifier().equals(getIdentifier()))
            throw new PathBetweenDifferentSystemException(this,planetToGo);

        var path = new ArrayList<CelestialBody>();
        path.add(this);
        path.add(planetToGo);
        return path;
    }
}