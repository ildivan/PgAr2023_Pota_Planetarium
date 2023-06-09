package planetarium.solarsystem;

import planetarium.solarsystem.error.CelestialBodyNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a planet that orbits a star.
 */
public class Planet extends Satellite {
    private final List<Moon> moons;
    private int numberOfMoons = 0;

    /**
     * The maximum number of moons that can orbit a planet.
     */
    public static final int MAX_NUMBER_OF_MOONS = 5000;

    //Protected because it should be instantiated ONLY from a Star object's appropriate method.
    Planet(Position planetPosition, long planetMass, Star star) {
        super(planetPosition, planetMass, star.getIdentifier() + "P" + (star.getNumberOfPlanets() + 1),star);
        moons = new ArrayList<>();
    }

    //Protected because it should be instantiated ONLY from a Star object's appropriate method.
    protected Planet(double x, double y, long planetMass, Star star) {
        this(new Position(x,y), planetMass, star);
    }

    /**
     * Getter method for the absolute position of the planet, relative to an arbitrary origin.
     * @return The absolute position of the planet.
     * @see Position
     */
    @Override
    public Position getAbsolutePosition() {
        Position absolutePosition = getRelativePosition();
        absolutePosition.increase(getParent().getAbsolutePosition());
        return absolutePosition;
    }

    /**
     * Getter method for the list of moons orbiting the planet.
     * @return The list of moons orbiting the planet.
     * @see Moon
     */
    public List<Moon> getMoons() {
        return moons;
    }

    //Counter of the moons created around the planet, used to create identifiers that are unique.
    protected int getNumberOfMoons() {
        return numberOfMoons;
    }

    /**
     * Given an identifier, it searches for a moon around the planet with the same identifier.
     * WARNING: It does not search for moons on another planets!
     * WARNING: A celestial body that is not a planet will not be found given its  identifier.
     * @param identifier The identifier of the moon we are searching
     * @return The instance of the moon searched. May throw CelestialBodyNotFoundException if the moon is not found.
     * @see Moon
     * @see CelestialBodyNotFoundException
     */
    public Moon findMoon(String identifier) throws CelestialBodyNotFoundException{
        for(Moon moon : getMoons()){
            if(identifier.equals(moon.getIdentifier())){
                return moon;
            }
        }
        throw new CelestialBodyNotFoundException(identifier);
    }

    /**
     * Creates a new moon orbiting the planet.
     * WARNING: It may not create the moon if it exceeds the max number.
     * @param moonRelativePosition The new moon's position relative to the planet.
     * @param moonMass The new moon's mass.
     * @see Moon
     */
    public void addNewMoon(Position moonRelativePosition, long moonMass) {
        if(moons.size() < MAX_NUMBER_OF_MOONS) {
            moons.add(new Moon(moonRelativePosition, moonMass, this));
            numberOfMoons++;
        }
    }

    /**
     * Creates a new moon orbiting the planet.
     * WARNING: It may not create the moon if it exceeds the max number.
     * @param relativeX The new moon's offset along the x-axis relative to the planet.
     * @param relativeY The new moon's offset along the y-axis relative to the planet.
     * @param moonMass The new moon's mass.
     * @see Moon
     */
    public void addNewMoon(double relativeX, double relativeY, long moonMass) {
        addNewMoon(new Position(relativeX, relativeY), moonMass);
    }

    //Removes a moon given its instance.
    protected void removeOldMoon(Moon moonToDelete) {
        moons.remove(moonToDelete);
    }


    /**
     * Removes the planet from its solar system, and does it from the planet's instance.
     */
    public void removeFromSystem() {
        ((Star)getParent()).removeOldPlanet(this);
        deleteStarReference();
    }

    //Sets the reference to its star to null, used when deleting the planet.
    protected void deleteStarReference(){
        parent = new Star(0,0,0);
    }

}