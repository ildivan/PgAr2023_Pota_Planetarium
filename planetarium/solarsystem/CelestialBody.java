package planetarium.solarsystem;

public abstract class CelestialBody{
    private final String identifier;
    private final Position position;
    private final long mass;

    CelestialBody(Position position, long mass, String identifier) {
        this.position = position;
        this.mass = mass;
        this.identifier = identifier;
    }

    CelestialBody(double x, double y, long mass, String identifier) {
        this.position = new Position(x,y);
        this.mass = mass;
        this.identifier = identifier;
    }

    public Position getRelativePosition() {
        return new Position(position.getX(),position.getY());
    }

    public abstract Position getPosition();

    public long getMass() {
        return mass;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        String className = getClass().getSimpleName();

        return "[ " + className + " " +
                getIdentifier() +
                "\tmass: " +
                getMass() +
                "Mkg position: " +
                getPosition() +
                ")]";
    }
}