package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a planet that orbits a star.
 */
public class Planet extends CelestialBody{
    private Star star;
    private final List<Moon> moons;
    private int numberOfMoons = 0;

    /**
     * The maximum number of moons that can orbit a planet.
     */
    public static final int MAX_NUMBER_OF_MOONS = 5000;

    //Package-private because it should be instantiated ONLY from a Star object's appropriate method.
    Planet(Position planetPosition, long planetMass, Star star){
        super(planetPosition, planetMass, star.getIdentifier() + "P" + (star.getNumberOfPlanets() + 1));
        this.star = star;
        moons = new ArrayList<>();
    }

    //Package-private because it should be instantiated ONLY from a Star object's appropriate method.
    Planet(double x, double y, long planetMass, Star star){
        this(new Position(x,y), planetMass, star);
    }

    /**
     * Getter method for the star the planet orbits around.
     * @return The star which the planet orbits.
     */
    public Star getStar() {
        return star;
    }

    /**
     * Getter method for the absolute position of the planet, relative to an arbitrary origin.
     * @return the absolute position of the planet.
     * @see Position
     */
    @Override
    public Position getPosition() {
        Position absolutePosition = getRelativePosition();
        absolutePosition.increase(getStar().getPosition());
        return absolutePosition;
    }

    /**
     * Getter method for the list of moons orbiting the planet.
     * @return the list of moons orbiting the planet.
     * @see Moon
     */
    public List<Moon> getMoons() {
        return moons;
    }

    //Counter of the moons created around the planet, used to create identifiers that are unique.
    int getNumberOfMoons() {
        return numberOfMoons;
    }

    /**
     * Given an identifier, it searches for a moon around the planet with the same identifier.
     * WARNING: It does not search for moons on another planets!
     * WARNING: Acelestial body that is not a planet will not be found given its  identifier.
     * @param identifier The identifier of the moon we are searching
     * @return The instance of the moon searched. May return null if the moon is not found.
     * @see Moon
     */
    public Moon findMoon(String identifier){
        for(var moon : getMoons()){
            if(identifier.equals(moon.getIdentifier())){
                return moon;
            }
        }
        return null;
    }

    /**
     * Creates a new moon orbiting the planet.
     * WARNING: It may not create the moon if it exceeds the max number.
     * @param moonRelativePosition the new moon's position relative to the planet.
     * @param moonMass the new moon's mass.
     * @see Moon
     */
    public void addNewMoon(Position moonRelativePosition,long moonMass){
        if(moons.size() < MAX_NUMBER_OF_MOONS){
            moons.add(new Moon(moonRelativePosition,moonMass,this));
            numberOfMoons++;
        }
    }

    /**
     * Creates a new moon orbiting the planet.
     * WARNING: It may not create the moon if it exceeds the max number.
     * @param relativeX the new moon's offset along the x-axis relative to the planet.
     * @param relativeY the new moon's offset along the y-axis relative to the planet.
     * @param moonMass the new moon's mass.
     * @see Moon
     */
    public void addNewMoon(long relativeX, long relativeY,long moonMass){
        addNewMoon(new Position(relativeX,relativeY),moonMass);
    }

    //Removes a moon given its instance.
    void removeOldMoon(Moon moonToDelete){
        moons.remove(moonToDelete);
    }

    /**
     * Removes a moon orbiting the planet given an identifier.
     * WARNING: If the identifier corresponds to a moon of another planet, the moon will not be deleted.
     * @param identifier The identifier of the moon to be removed.
     * @see Moon
     */
    public void removeOldMoon(String identifier){
        Moon moonToDelete = findMoon(identifier);
        if(moonToDelete != null){
            moonToDelete.removeFromSystem();
        }
    }

    /**
     * Removes the planet from its solar system, and does it from the planet's instance.
     */
    public void removeFromSystem() {
        getStar().removeOldPlanet(this);
        star = null;
    }

    /**
     * Calculates the distance from the planet to its star, equivalently its orbiting radius.
     * @return the distance from the planet to its star.
     * @see Star
     */
    public double distanceToStar(){
        Position relative = getRelativePosition();
        return Math.sqrt( Math.pow(relative.getX(),2)+Math.pow(relative.getY(),2) );
    }


    //Returns a list with the Planet and its Star as elements.
    ArrayList<CelestialBody> pathToStar(){
        var path = new ArrayList<CelestialBody>();
        path.add(this);
        path.add(getStar());
        return path;
    }

    //Returns a list with the Planet and the Moon as elements, throw exception if the moon does not orbit the planet.
    ArrayList<CelestialBody> pathToMoon(Moon moonToGo) throws IllegalArgumentException{
        var moons = getMoons();

        if(!moonToGo.getPlanet().getIdentifier().equals(getIdentifier()))
            throw new IllegalArgumentException("La luna non appartiene al pianeta");

        var path = new ArrayList<CelestialBody>();
        path.add(this);
        path.add(moonToGo);
        return path;
    }
}