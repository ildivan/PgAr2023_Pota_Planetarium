package planetarium.solarsystem;

/**
 * Represents a moon that orbits around a planet.
 */
public class Moon extends Satellite {

    //Protected because it should be instantiated ONLY from a Planet object's appropriate method.
    protected Moon(Position moonPosition, long moonMass, Planet planet) {
        super(moonPosition, moonMass, planet.getIdentifier() + "M" + (planet.getNumberOfMoons() + 1), planet);
    }

    //Protected because it should be instantiated ONLY from a Planet object's appropriate method.
    protected Moon(double x, double y, long moonMass, Planet planet) {
        this(new Position(x,y), moonMass, planet);
    }

    /**
     * Getter method for the absolute position of the moon, relative to an arbitrary origin.
     * @return The absolute position of the moon.
     * @see Position
     */
    @Override
    public Position getAbsolutePosition() {
        Position absolutePosition = getRelativePosition();
        absolutePosition.increase(getParent().getAbsolutePosition());
        return absolutePosition;
    }

    /**
     * Removes a moon from its solar system, and does it from the moon instance.
     */
    public void removeFromSystem() {
        ((Planet)getParent()).removeOldMoon(this);
        parent = new Planet(0,0,0,new Star(0,0,0));
    }


}