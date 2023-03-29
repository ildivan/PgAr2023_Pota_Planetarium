package planetarium.solarsystem;

public class Moon extends CelestialBody{
    private final Planet planet;

    Moon(Position moonPosition,long moonMass, Planet planet){
        super(moonPosition,moonMass);
        this.planet = planet;
    }

    Moon(double x, double y,long moonMass, Planet planet){
        super(new Position(x,y),moonMass);
        this.planet = planet;
    }

    public Position getPosition() {
        Position absolutePosition = getRelativePosition();
        absolutePosition.increase(getPlanet().getPosition());
        return absolutePosition;
    }

    private Planet getPlanet() {
        return planet;
    }
}
