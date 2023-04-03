package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.List;

public class Planet extends CelestialBody{
    private Star star;
    private final List<Moon> moons;
    private int numberOfMoons = 0;

    public static final int MAX_NUMBER_OF_MOONS = 5000;

    Planet(Position planetPosition, long planetMass, Star star){
        super(planetPosition, planetMass, star.getIdentifier() + "P" + (star.getNumberOfPlanets() + 1));
        this.star = star;
        moons = new ArrayList<>();
    }

    Planet(double x, double y, long planetMass, Star star){
        this(new Position(x,y), planetMass, star);
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

    public int getNumberOfMoons() {
        return numberOfMoons;
    }

    public Moon findMoon(String identifier){
        for(var moon : getMoons()){
            if(identifier.equals(moon.getIdentifier())){
                return moon;
            }
        }
        return null;
    }

    public void addNewMoon(Position moonRelativePosition,long moonMass){
        if(moons.size() < MAX_NUMBER_OF_MOONS){
            moons.add(new Moon(moonRelativePosition,moonMass,this));
            numberOfMoons++;
        }
    }

    public void addNewMoon(long relativeX, long relativeY,long moonMass){
        if(moons.size() < MAX_NUMBER_OF_MOONS){
            moons.add(new Moon(relativeX,relativeY,moonMass,this));
            numberOfMoons++;
        }
    }

    void removeOldMoon(Moon moonToDelete){
        moons.remove(moonToDelete);
    }

    public void removeOldMoon(String identifier){
        Moon moonToDelete = findMoon(identifier);
        if(moonToDelete != null){
            moonToDelete.removeFromSystem();
        }
    }

    public void removeFromSystem() {
        star.removeOldPlanet(this);
        star = null;
    }

    public double distanceToStar(){
        Position relative = getRelativePosition();
        return Math.sqrt( Math.pow(relative.getX(),2)+Math.pow(relative.getY(),2) );
    }
}