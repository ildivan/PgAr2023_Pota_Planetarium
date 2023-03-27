package planetarium;

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



    public abstract class CelestialBody{
        private final int identifier;
        private final Position position;
        private final long mass;
        private static int counter = 1;




        public CelestialBody(Position position, long mass) {
            this.position = position;
            this.mass = mass;
            identifier = counter;
            updateCounter();
        }

        public CelestialBody(long x, long y, long mass) {
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




    public class Star extends CelestialBody{
        private List<Planet> planets;

        private Star(Position starPosition, long starMass) {
            super(starPosition, starMass);
            planets = new ArrayList<>();
        }

        private Star(long x, long y, long starMass) {
            super(x, y, starMass);
            planets = new ArrayList<>();
        }

        public List<Planet> getPlanets() {
            return Collections.unmodifiableList(planets);
        }

        public void addNewPlanet(Position planetRelativePosition, long mass){
            planets.add(new Planet(planetRelativePosition,mass,this));
        }

        public void addNewPlanet(long relativeX, long relativeY, long mass){
            planets.add(new Planet(new Position(relativeX,relativeY),mass,this));
        }

    }



    public class Planet extends CelestialBody{
        private final Star star;
        private List<Moon> moons;

        private Planet(Position planetPosition, long planetMass, Star star){
            super(planetPosition,planetMass);
            this.star = star;
        }

        private Planet(long x, long y, long planetMass, Star star){
            super(new Position(x,y),planetMass);
            this.star = star;
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
            moons.add(new Moon(new Position(relativeX,relativeY),moonMass,this));
        }
    }




    public class Moon extends CelestialBody{
        private final Planet planet;

        private Moon(Position moonPosition,long moonMass, Planet planet){
            super(moonPosition,moonMass);
            this.planet = planet;
        }

        private Moon(long x, long y,long moonMass, Planet planet){
            super(new Position(x,y),moonMass);
            this.planet = planet;
        }

        private Planet getPlanet() {
            return planet;
        }
    }
}
