package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Planet extends CelestialBody{
    private Star star;
    private final List<Moon> moons;

    public static final int MAX_NUMBER_OF_MOONS = 5000;

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
        return moons;
    }

    public Moon findMoon(int identifier){
        for(var moon : getMoons()){
            if(identifier == moon.getIdentifier()){
                return moon;
            }
        }
        return null;
    }

    public void addNewMoon(Position moonRelativePosition,long moonMass){
        if(moons.size() < MAX_NUMBER_OF_MOONS){
            moons.add(new Moon(moonRelativePosition,moonMass,this));
        }
    }

    public void addNewMoon(long relativeX, long relativeY,long moonMass){
        if(moons.size() < MAX_NUMBER_OF_MOONS){
            moons.add(new Moon(relativeX,relativeY,moonMass,this));

        }
    }

    void removeOldMoon(Moon moonToDelete){
        moons.remove(moonToDelete);
    }

    public void removeOldMoon(int identifier){
        Moon moonToDelete = findMoon(identifier);
        if(moonToDelete != null){
            moonToDelete.removeFromSystem();
        }
    }

    public void removeFromSystem() {
        star.removeOldPlanet(this);
        star = null;
    }
}