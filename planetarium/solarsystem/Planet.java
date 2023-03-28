package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Planet extends CelestialBody{
    private final Star star;
    private final List<Moon> moons;

    Planet(Position planetPosition, long planetMass, Star star){
        super(planetPosition,planetMass);
        this.star = star;
        moons = new ArrayList<>();
    }

    Planet(double x, double y, long planetMass, Star star){
        super(new Position(x,y),planetMass);
        this.star = star;
        moons = new ArrayList<>();
    }

    private Star getStar() {
        return star;
    }

    @Override
    public Position getPosition() {
        Position absolutePosition = getRelativePosition();
        absolutePosition.increase(getStar().getPosition());
        return absolutePosition;
    }

    public List<Moon> getMoons() {
        return Collections.unmodifiableList(moons);
    }

    public void addNewMoon(Position moonRelativePosition,long moonMass){
        moons.add(new Moon(moonRelativePosition,moonMass,this));
    }

    public void addNewMoon(long relativeX, long relativeY,long moonMass){
        moons.add(new Moon(relativeX,relativeY,moonMass,this));
    }
}