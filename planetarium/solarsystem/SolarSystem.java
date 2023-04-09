package planetarium.solarsystem;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  Represent a solar system : a star, his planets and the planets moons.
 * */

public class SolarSystem {

    private final Star star;


    /**
     * SolarSystem constructor.
     * <p>
     * The system is instantiated with its star's parameters.
     *
     * @param starPosition the position of the system's star relative to an arbitrary origin.
     * @param starMass the mass of the star.
     * @see Position
     * */
    public SolarSystem(Position starPosition, long starMass){
        star = new Star(starPosition,starMass);
    }

    /**
     * SolarSystem constructor.
     * The system is instantiated with its star's parameters but receives coordinates instead of an
     * instance of Position.
     *
     * @param x the offset along the x-axis of the star relative to an arbitrary origin.
     * @param y the offset along the y-axis of the star relative to an arbitrary origin.
     * @param starMass the mass of the star.
     *
     * */
    public SolarSystem(long x,long y, long starMass){
        this(new Position(x,y), starMass);
    }

    /**
     * Getter method for the star.
     * @return the star instance.
     * @see Star
     * */
    public Star getStar(){
        return star;
    }

    /**
     * Calculates the center of mass of the system.
     * @return the position of the  center of mass of the system
     * @see Position
     * */
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

    //Gets the total mass of the system (star + planets + moons)
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

    /**
     * Finds a celestial body (star, planet or moon) given its identifier.
     * @param identifier the celestial body unique identifier
     * @return the instance of the celestial body, may return null if it does not exist.
     * @see CelestialBody
     */
    public CelestialBody findCelestialBody(String identifier){
        var star = getStar();
        if(identifier.equals(star.getIdentifier())) return star;

        var searchedPlanet = star.findPlanet(identifier);
        if(searchedPlanet != null) return searchedPlanet;

        for(var planet : star.getPlanets()){
            var searchedMoon = planet.findMoon(identifier);
            if(searchedMoon != null) return searchedMoon;
        }

        return null;
    }

    /**
     * Checks for all types of possible collisions in the system, return true if found.
     * @return true if there are possible collisions, false if not.
     */
    public boolean detectCollisions(){
        return checkCollisionBetweenPlanets() || checkCollisionsSamePlanetMoons()
                || checkCollisionStarAndMoons() || checkCollisionDifferentPlanetMoons()
                || checkCollisionPlanetsAndMoons();
    }

    //Check collisions between planets
    private boolean checkCollisionBetweenPlanets(){
        var planets = getStar().getPlanets();
        var distancesFromStar = new ArrayList<Double>();

        for(var planet : planets){
            double distance = planet.distanceToStar();
            if(distancesFromStar.contains(distance)){
                return true;
            }
            distancesFromStar.add(distance);
        }
        return false;
    }

    //Check collisions between moons of the same planet
    private boolean checkCollisionsSamePlanetMoons(){
        var planets = getStar().getPlanets();

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

    //Check collisions of moons with the star
    private boolean checkCollisionStarAndMoons(){
        var planets = getStar().getPlanets();

        for(var planet : planets){
            var moons = planet.getMoons();
            for(var moon : moons){
                if(planet.distanceToStar() <= moon.distanceToPlanet()){
                    return true;
                }
            }
        }

        return false;
    }

    //Check collisions within moons of different planets
    private boolean checkCollisionDifferentPlanetMoons() {
        var planets = getStar().getPlanets();

        for (int i = 0; i < planets.size(); i++) {
            for (int j = i+1; j < planets.size(); j++) {
                Planet first = planets.get(i);
                Planet second = planets.get(j);

                var firstMoons = first.getMoons();
                var secondMoons = second.getMoons();

                for (int k = 0; k < firstMoons.size(); k++) {
                    for (int l = k+1; l < firstMoons.size(); l++) {
                        double planetRadiusDifference = Math.abs(first.distanceToStar()-second.distanceToStar());
                        Moon firstMoon = firstMoons.get(i);
                        Moon secondMoon = secondMoons.get(j);
                        if(firstMoon.distanceToPlanet() + secondMoon.distanceToPlanet() >= planetRadiusDifference){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //Checks collisions within planets and moons of other planets
    private boolean checkCollisionPlanetsAndMoons(){
        var planets = getStar().getPlanets();

        for (int i = 0; i < planets.size(); i++) {
            for (int j = i+1; j < planets.size(); j++) {
                var planet = planets.get(i);
                var nextPlanet = planets.get(j);
                var moons = nextPlanet.getMoons();

                double planetRadiusDifference = Math.abs(planet.distanceToStar()-nextPlanet.distanceToStar());

                for(var moon : moons){
                    if(moon.distanceToPlanet() <= planetRadiusDifference){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Finds the path from a celestial body in the universe to another.
     * Example: " S1P1M1 > S1P1 > S1 > S1P3 "
     * @param startIdentifier Identifier of the celestial body at the start of the  path.
     * @param endIdentifier Identifier of the celestial body at the end of the path.
     * @return A string that represents the path between the two celestial bodies.
     */
    public String findPath(String startIdentifier,String endIdentifier) throws IllegalArgumentException{
        if(startIdentifier.equals(endIdentifier)) return "Nessun percorso richiesto.";

        CelestialBody start = findCelestialBody(startIdentifier);
        CelestialBody end = findCelestialBody(endIdentifier);
        if(start == null || end == null) throw new IllegalArgumentException("Identificatori non validi.");

        ArrayList<CelestialBody> path = null;

        if(start instanceof Moon startMoon){
            if(end instanceof Moon endMoon){
                path =  moonToMoon(startMoon,endMoon);
            } else if (end instanceof Planet endPlanet){
                path =  moonToPlanet(startMoon,endPlanet);
            } else if (end instanceof Star endStar){
                path =  moonToStar(startMoon,endStar);
            }
        } else if (start instanceof Planet startPlanet){
            if(end instanceof Moon endMoon){
                path =  planetToMoon(startPlanet,endMoon);
            } else if (end instanceof Planet endPlanet){
                path =  planetToPlanet(startPlanet,endPlanet);
            } else if (end instanceof Star endStar){
                path =  planetToStar(startPlanet,endStar);
            }
        } else if (start instanceof Star startStar){
            if (end instanceof Moon endMoon){
                path =  starToMoon(startStar,endMoon);
            } else if (end instanceof Planet endPlanet){
                path =  starToPlanet(startStar,endPlanet);
            }
        }

        return pathToString(path);

    }

    //Converts list of celestial bodies to a string representing a path.
    private static String pathToString(ArrayList<CelestialBody> path){
        if(path == null || path.isEmpty()) return "Percorso non supportato.";

        String sPath = String.format("%s ",path.get(0).getIdentifier());

        for(int i = 1; i < path.size(); i++){
            sPath = sPath.concat(String.format(" > %s",path.get(i).getIdentifier()));
        }

        return sPath;
    }

    private ArrayList<CelestialBody> moonToMoon(Moon start, Moon end){
        var path = moonToPlanet(start,end.getPlanet());
        path.add(end);
        return path;
    }

    private ArrayList<CelestialBody> moonToPlanet(Moon start, Planet end){
        var path = planetToMoon(end,start);
        Collections.reverse(path);
        return path;
    }

    private ArrayList<CelestialBody> moonToStar(Moon start, Star end){
        var path = starToMoon(end,start);
        Collections.reverse(path);
        return path;
    }



    private ArrayList<CelestialBody> planetToMoon(Planet start, Moon end){
        //Checks if the moon is not orbiting the planet
        if(!end.getPlanet().getIdentifier().equals(start.getIdentifier())){
            var path = planetToPlanet(start,end.getPlanet());
            path.add(end);
            return path;
        }

        return start.pathToMoon(end);
    }

    private ArrayList<CelestialBody> planetToPlanet(Planet start, Planet end){
        var path = planetToStar(start,end.getStar());
        path.add(end);
        return path;
    }

    private ArrayList<CelestialBody> planetToStar(Planet start, Star end){
        var path = starToPlanet(end,start);
        Collections.reverse(path);
        return path;
    }

    private ArrayList<CelestialBody> starToMoon(Star start, Moon end){
        var path = star.pathToPlanet(end.getPlanet());
        path.add(end);
        return path;
    }

    private ArrayList<CelestialBody> starToPlanet(Star start, Planet end){
        return start.pathToPlanet(end);
    }



}