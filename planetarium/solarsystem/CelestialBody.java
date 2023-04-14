package planetarium.solarsystem;

/**
 * Represent a celestial body, an object in the universe with an identifier, a position and a mass.
 */
public abstract class CelestialBody {
    /**
     * A string that identifies the celestial body, has to be unique.
     * Star id examples: "S1" "S2" "S3"
     * Planet id examples: "S1P1" "S1P2" "S2P1" "S2P2"
     * Moon id examples: "S1P1M1" "S1P1M2" "S1P2M1" "S2P1M1"
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
     * @param position The celestial body's position relative to his parent.
     * @param mass The celestial body's mass.
     * @param identifier The unique identifier for the celestial body.
     */
    public CelestialBody(Position position, long mass, String identifier) {
        this.position = position;
        this.mass = mass;
        this.identifier = identifier;
    }

    /**
     * Constructor for a generic celestial body.
     * @param x The celestial body's offset along the x-axis relative to his parent.
     * @param y The celestial body's offset along the y-axis relative to his parent.
     * @param mass The celestial body's mass.
     * @param identifier The unique identifier for the celestial body.
     */
    public CelestialBody(double x, double y, long mass, String identifier) {
        this(new Position(x,y), mass, identifier);
    }

    /**
     * Getter method for the position of the celestial body relative to his parent.
     * @return The relative position of the celestial body.
     */
    public Position getRelativePosition() {
        return new Position(position.getX(), position.getY());
    }

    /**
     * Getter method for the absolute position.
     * Gets position relative to an arbitrary origin , has to  be implemented by every subclass.
     * @return Absolute position of the celestial body.
     * @see Position
     */
    public abstract Position getPosition();

    /**
     * Getter method for the mass.
     * @return The celestial body's mass.
     */
    public long getMass() {
        return mass;
    }

    /**
     * Getter method for the identifier.
     * @return The celestial body's unique identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * To string method for celestial bodies.
     * @return The celestial body's information in a readable way.
     */
    @Override
    public String toString() {
        String className = getClass().getSimpleName();
        return String.format("[ %s %s\tmass: %dMkg position: %s]", className, getIdentifier(), getMass(), getPosition());
    }
}
