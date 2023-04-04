package planetarium.solarsystem;

/**
 * Represent a celestial body, an object in the universe with an identifier, a position and a mass.
 */

public abstract class CelestialBody{
    /**
     * A string that identifies the celestial body, has to be unique.
     */
    private final String identifier;

    /**
     * Represents the position of the celestial body relative to his parent.
     * Examples:
     * A star is positioned relative to an arbitrary origin.
     * A planet is positioned relative to his star.
     * A moon is positioned relative to his planet.
     * @see Star
     * @see Planet
     * @see Moon
     */
    private final Position position;

    /**
     * The celestial body's mass.
     */
    private final long mass;


    /**
     * Constructor for a generic celestial body.
     * @param position the celestial body's position relative to his parent.
     * @param mass the celestial body's mass.
     * @param identifier the unique identifier for the celestial body.
     */
    public CelestialBody(Position position, long mass, String identifier) {
        this.position = position;
        this.mass = mass;
        this.identifier = identifier;
    }

    /**
     * Constructor for a generic celestial body.
     * @param x the celestial body's offset along the x-axis relative to his parent.
     * @param y the celestial body's offset along the y-axis relative to his parent.
     * @param mass the celestial body's mass.
     * @param identifier the unique identifier for the celestial body.
     */
    public CelestialBody(double x, double y, long mass, String identifier) {
        this(new Position(x,y),mass,identifier);
    }

    /**
     * Getter method for the position of the celestial body relative to his parent.
     * @return the relative position of the celestial body.
     */
    public Position getRelativePosition() {
        return new Position(position.getX(),position.getY());
    }

    /**
     * Getter method for the absolute position.
     * Gets position relative to an arbitrary origin , has to  be implemented by every subclass.
     * @return absolute position of the celestial body.
     * @see Position
     */
    public abstract Position getPosition();

    /**
     * Getter method for the mass.
     * @return the celestial body's mass.
     */
    public long getMass() {
        return mass;
    }

    /**
     * Getter method for the identifier.
     * @return the celestial body's unique identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * To string method for celestial bodies.
     * @return the celestial body's information in a readable way.
     */
    @Override
    public String toString() {
        String className = getClass().getSimpleName();
        return String.format("[ %s %s\tmass: %dMkg position: %s]", className, getIdentifier(),getMass(),getPosition());
    }
}
