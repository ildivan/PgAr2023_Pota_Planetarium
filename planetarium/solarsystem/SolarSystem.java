package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolarSystem {
    private final Star star;

    public SolarSystem(Position starPosition, long starMass){
        star = new Star(starPosition,starMass);
    }

    public SolarSystem(long x,long y, long starMass){
        star = new Star(new Position(x,y),starMass);
    }

    public Star getStar(){
        return star;
    }














}
