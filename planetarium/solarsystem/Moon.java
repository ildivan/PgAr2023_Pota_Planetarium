package planetarium.solarsystem;

public class Moon extends CelestialBody{
    private Planet planet;

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

    public void removeFromSystem(){
        planet.removeOldMoon(this);
        planet = null;
    }

    public double distanceToPlanet(){
        Position relative = getRelativePosition();

        return Math.sqrt(Math.pow(relative.getX(),2) + Math.pow(relative.getY(), 2));
    }
}
