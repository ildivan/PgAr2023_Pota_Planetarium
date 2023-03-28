package planetarium.solarsystem;

class Moon extends CelestialBody{
    private final Planet planet;

    Moon(Position moonPosition,long moonMass, Planet planet){
        super(moonPosition,moonMass);
        this.planet = planet;
    }

    Moon(long x, long y,long moonMass, Planet planet){
        super(new Position(x,y),moonMass);
        this.planet = planet;
    }

    private Planet getPlanet() {
        return planet;
    }
}
