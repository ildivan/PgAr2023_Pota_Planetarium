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

    Planet(long x, long y, long planetMass, Star star){
        super(new Position(x,y),planetMass);
        this.star = star;
        moons = new ArrayList<>();
    }

    private Star getStar() {
        return star;
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