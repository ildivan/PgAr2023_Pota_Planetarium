package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.HashMap;

public class SolarSystem {
    private final Star star;

    public SolarSystem(Position starPosition, long starMass){
        star = new Star(starPosition,starMass);
    }

    public SolarSystem(long x,long y, long starMass){
        star = new Star(x,y,starMass);
    }

    public Star getStar(){
        return star;
    }

    public Position getCenterOfMass(){
        final long systemMass = getSystemTotalMass();
        final Position centerOfMass = new Position(0,0);

        // Add weighted position of the Star
        final double starMass = getStar().getMass();
        final Position adjustedStarPosition = getStar().getPosition().multiplyBy(starMass);
        centerOfMass.increase(adjustedStarPosition);

        // Add weighted positions of the Planets
        final var planets = getStar().getPlanets();
        for(final var planet : planets){
            final double planetMass = planet.getMass();
            final Position adjustedPlanetPosition = planet.getPosition().multiplyBy(planetMass);
            centerOfMass.increase(adjustedPlanetPosition);

            final var moons = planet.getMoons();
            // Add weighted positions of the Moons
            for(final var moon : moons){
                final double moonMass = moon.getMass();
                final Position adjustedMoonPosition = moon.getPosition().multiplyBy(moonMass);
                centerOfMass.increase(adjustedMoonPosition);
            }
        }

        //Divide by total mass
        centerOfMass.setX(centerOfMass.getX()/systemMass);
        centerOfMass.setY(centerOfMass.getY()/systemMass);

        return centerOfMass;
    }

    private long getSystemTotalMass(){
        // Star mass as starting mass
        long totalMass = getStar().getMass();

        final var planets = getStar().getPlanets();

        // Add mass of all the planets and their moons to the total
        for (final var planet: planets) {
            totalMass += planet.getMass();

            // Add mass of all the moons of the planet to the total
            final var moons = planet.getMoons();
            for(final var moon : moons){
                totalMass += moon.getMass();
            }
        }

        return totalMass;
    }

    public CelestialBody findCelestialBody(int identifier){
        var star = getStar();
        if(identifier == star.getIdentifier()) return star;

        var searchedPlanet = star.findPlanet(identifier);
        if(searchedPlanet != null) return searchedPlanet;

        for(var planet : star.getPlanets()){
            var searchedMoon = planet.findMoon(identifier);
            if(searchedMoon != null) return searchedMoon;
        }

        return null;
    }

    public boolean detectCollisions(){
        var planets = getStar().getPlanets();
        var distancesFromStar = new ArrayList<Double>();
        //Check collisions between planets
        for(var planet : planets){
            double distance = planet.distanceToStar();
            if(distancesFromStar.contains(distance)){
                return true;
            }
            distancesFromStar.add(distance);
        }

        //Check collisions between moons of the same planet
        for(var planet : planets){
            var moons = planet.getMoons();
            var distancesFromPlanet = new ArrayList<Double>();
            for(var moon : moons){
                double distance = moon.distanceToPlanet();
                if(distancesFromPlanet.contains(distance)){
                    return true;
                }
                distancesFromPlanet.add(distance);
            }
        }

        

        return false;
    }
}
