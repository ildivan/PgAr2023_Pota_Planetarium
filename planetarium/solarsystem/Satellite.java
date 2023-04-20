package planetarium.solarsystem;

public abstract class Satellite extends CelestialBody{

    protected CelestialBody parent;

    public Satellite(Position position, long mass, String identifier, CelestialBody parent) {
        super(position, mass, identifier);
        this.parent = parent;
    }

    public Satellite(double x, double y, long mass, String identifier, CelestialBody parent) {
        super(x, y, mass, identifier);
        this.parent = parent;
    }

    /**
     * @return The relative position of the satellite (moon or planet)  relative to his parent.
     */
    public Position getRelativePosition() {
        return new Position(position.getX(), position.getY());
    }

    public CelestialBody getParent() { return parent; }

    /**
     * @return The distance from the satellite to its parent, equivalently its orbiting radius..
     */
    public double distanceToParent() {
        Position relative = getRelativePosition();
        return Math.sqrt( Math.pow(relative.getX(), 2) + Math.pow(relative.getY(), 2) );
    }
}
