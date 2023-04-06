package planetarium.solarsystem;

/**
 * Represents a moon that orbits around a planet.
 */
public class Moon extends CelestialBody{

    private Planet planet;

    //Package-private because it should be instantiated ONLY from a Planet object's appropriate method.
    Moon(Position moonPosition,long moonMass, Planet planet){
        super(moonPosition, moonMass, planet.getIdentifier() + "M" + (planet.getNumberOfMoons() + 1));
        this.planet = planet;
    }

    //Package-private because it should be instantiated ONLY from a Planet object's appropriate method.
    Moon(double x, double y,long moonMass, Planet planet){
        this(new Position(x,y),moonMass,planet);
    }

    /**
     * Getter method for the absolute position of the moon, relative to an arbitrary origin.
     * @return the absolute position of the moon.
     * @see Position
     */
    @Override
    public Position getPosition() {
        Position absolutePosition = getRelativePosition();
        absolutePosition.increase(getPlanet().getPosition());
        return absolutePosition;
    }

    /**
     * Getter method for the planet the moon orbits around.
     * @return The planet which the moon orbits.
     */
    public Planet getPlanet() {

        return planet;
    }

    /**
     * Removes a moon from the system, and does it from the moon instance.
     */
    public void removeFromSystem(){
        getPlanet().removeOldMoon(this);
        planet = null;
    }

    /**
     * Calculates the distance from the moon to its planet, equivalently its orbiting radius.
     * @return the distance from the moon to its planet.
     * @see Planet
     */
    public double distanceToPlanet(){
        Position relative = getRelativePosition();
        return Math.sqrt(Math.pow(relative.getX(),2) + Math.pow(relative.getY(), 2));
    }


    String pathToPlanet(){
        return String.format(" > %s", getPlanet().getIdentifier());
    }

}