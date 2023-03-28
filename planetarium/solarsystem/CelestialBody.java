package planetarium.solarsystem;

abstract class CelestialBody{
    private final int identifier;
    private final Position position;
    private final long mass;
    private static int counter = 1;




    CelestialBody(Position position, long mass) {
        this.position = position;
        this.mass = mass;
        identifier = counter;
        updateCounter();
    }

    CelestialBody(long x, long y, long mass) {
        this.position = new Position(x,y);
        this.mass = mass;
        identifier = counter;
        updateCounter();
    }

    public Position getRelativePosition() {
        return position;
    }

    public long getMass() {
        return mass;
    }

    public int getIdentifier() {
        return identifier;
    }

    private void updateCounter(){
        counter++;
    }

}
