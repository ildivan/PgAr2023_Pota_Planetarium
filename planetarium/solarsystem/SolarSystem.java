package planetarium.solarsystem;

import planetarium.solarsystem.error.CelestialBodyNotFoundException;
import planetarium.solarsystem.error.PathBetweenDifferentSystemException;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  Represent a solar system : a star, his planets and the planets moons.
 */
public class SolarSystem {
    private final Star star;

    /**
     * SolarSystem constructor.
     * <p>
     * The system is instantiated with its star's parameters.
     *
     * @param starPosition The position of the system's star relative to an arbitrary origin.
     * @param starMass The mass of the star.
     * @see Position
     */
    public SolarSystem(Position starPosition, long starMass) {
        star = new Star(starPosition, starMass);
    }

    /**
     * SolarSystem constructor.
     * The system is instantiated with its star's parameters but receives coordinates instead of an
     * instance of Position.
     *
     * @param x The offset along the x-axis of the star relative to an arbitrary origin.
     * @param y The offset along the y-axis of the star relative to an arbitrary origin.
     * @param starMass The mass of the star.
     *
     */
    public SolarSystem(double x, double y, long starMass) {
        this(new Position(x,y), starMass);
    }

    /**
     * Getter method for the star.
     * @return The star instance.
     * @see Star
     */
    public Star getStar() {
        return star;
    }

    /**
     * Calculates the center of mass of the system.
     * @return The position of the  center of mass of the system
     * @see Position
     */
    public Position getCenterOfMass() {
        final long systemMass = getSystemTotalMass();
        final Position centerOfMass = new Position(0, 0);

        // Add weighted position of the Star
        final double starMass = getStar().getMass();
        final Position adjustedStarPosition = getStar().getPosition().multiplyBy(starMass);
        centerOfMass.increase(adjustedStarPosition);

        // Add weighted positions of the Planets
        final var planets = getStar().getPlanets();
        for(final var planet : planets) {
            final double planetMass = planet.getMass();
            final Position adjustedPlanetPosition = planet.getPosition().multiplyBy(planetMass);
            centerOfMass.increase(adjustedPlanetPosition);

            final var moons = planet.getMoons();
            // Add weighted positions of the Moons for every Planet
            for(final var moon : moons) {
                final double moonMass = moon.getMass();
                final Position adjustedMoonPosition = moon.getPosition().multiplyBy(moonMass);
                centerOfMass.increase(adjustedMoonPosition);
            }
        }

        //Divide coordinates by total mass
        centerOfMass.multiplyBy(1.0/systemMass);

        return centerOfMass;
    }

    //Gets the total mass of the system (star + planets + moons)
    private long getSystemTotalMass() {
        // Star mass as starting mass
        long totalMass = getStar().getMass();

        final var planets = getStar().getPlanets();

        // Add mass of all the planets and their moons to the total
        for (final var planet: planets) {
            totalMass += planet.getMass();

            // Add mass of all the moons of the planet to the total
            final var moons = planet.getMoons();
            for(final var moon : moons)
                totalMass += moon.getMass();
        }

        return totalMass;
    }

    /**
     * Finds a celestial body (star, planet or moon) given its identifier.
     * @param identifier The celestial body unique identifier
     * @return <p>The instance of the celestial body, may return null if it does not exist.
     * May throw CelestialBodyNotFoundException if the celestial body is not found inside the solar system</p>
     * @see CelestialBody
     */
    public CelestialBody findCelestialBody(String identifier) throws CelestialBodyNotFoundException {
        var star = getStar();
        if(identifier.equals(star.getIdentifier()))
            return star;

        try {
            return star.findPlanet(identifier);
        } catch(CelestialBodyNotFoundException planetNotFound) {
            for(var planet : star.getPlanets()) {
                try {
                    return planet.findMoon(identifier);
                } catch(CelestialBodyNotFoundException ignored) {}
            }
        }
        throw new CelestialBodyNotFoundException(identifier);
    }




    /**
     * Checks for all types of possible collisions in the system, return true if found.
     * @return True if there are possible collisions, false if not.
     */
    public boolean detectCollisions() {
        return checkCollisionBetweenPlanets() || checkCollisionsSamePlanetMoons()
                || checkCollisionStarAndMoons() || checkCollisionPlanetsAndMoons()
                || checkCollisionDifferentPlanetMoons();
    }

    //Check collisions between planets
    //Returns true if found
    private boolean checkCollisionBetweenPlanets() {
        var planets = getStar().getPlanets();
        var distancesFromStar = new ArrayList<Double>();

        for(var planet : planets) {
            double distance = planet.distanceToStar();
            if(distancesFromStar.contains(distance))
                return true;

            distancesFromStar.add(distance);
        }
        return false;
    }

    //Check collisions between moons of the same planet
    //Returns true if found
    private boolean checkCollisionsSamePlanetMoons() {
        var planets = getStar().getPlanets();

        for(var planet : planets) {
            var moons = planet.getMoons();
            var distancesFromPlanet = new ArrayList<Double>();
            for(var moon : moons) {
                double distance = moon.distanceToPlanet();
                if(distancesFromPlanet.contains(distance))
                    return true;

                distancesFromPlanet.add(distance);
            }
        }

        return false;
    }

    //Check collisions of moons with the star
    //Returns true if found
    private boolean checkCollisionStarAndMoons() {
        var planets = getStar().getPlanets();

        for(var planet : planets) {
            var moons = planet.getMoons();
            for(var moon : moons) {
                if(planet.distanceToStar() <= moon.distanceToPlanet())
                    return true;
            }
        }

        return false;
    }

    //Check collisions within moons of different planets
    //Optimized to not check twice any pair of moons.
    //Returns true if found
    private boolean checkCollisionDifferentPlanetMoons() {
        var planets = getStar().getPlanets();

        for (int i = 0; i < planets.size(); i++) {
            Planet first = planets.get(i);
            for (int j = i+1; j < planets.size(); j++) {
                Planet second = planets.get(j);

                var firstMoons = first.getMoons();
                var secondMoons = second.getMoons();

                for (int k = 0; k < firstMoons.size(); k++) {
                    for (int l = k+1; l < firstMoons.size(); l++) {
                        double planetRadiusDifference = Math.abs( first.distanceToStar() - second.distanceToStar() );
                        Moon firstMoon = firstMoons.get(i);
                        Moon secondMoon = secondMoons.get(j);
                        if(firstMoon.distanceToPlanet() + secondMoon.distanceToPlanet() >= planetRadiusDifference)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    //Checks collisions within planets and moons of other planets
    //Optimized to not check twice any pairs.
    //Returns true if found
    private boolean checkCollisionPlanetsAndMoons() {
        var planets = getStar().getPlanets();

        for (int i = 0; i < planets.size(); i++) {
            var planet = planets.get(i);
            for (int j = i+1; j < planets.size(); j++) {
                var otherPlanet = planets.get(j);
                var otherPlanetMoons = otherPlanet.getMoons();

                double planetRadiusDifference = Math.abs( planet.distanceToStar() - otherPlanet.distanceToStar() );

                for(var moon : otherPlanetMoons) {
                    if(moon.distanceToPlanet() <= planetRadiusDifference)
                        return true;
                }
            }
        }

        return false;
    }




    /**
     * Finds the path from a celestial body in the universe to another.
     * Examples:
     * " S1P1M1 > S1P1 > S1 > S1P3 "
     * " S1 > S1P1 > S1P1M1 "
     * " S1P1 > S1 > S1P2 "
     * @param startIdentifier Identifier of the celestial body at the start of the  path.
     * @param endIdentifier Identifier of the celestial body at the end of the path.
     * @return <p>A string that represents the path between the two celestial bodies.
     * May throw PathBetweenDifferentSolarSystem if the two celestial bodies do not belong to the same system</p>
     */
    public String findPath(String startIdentifier,String endIdentifier) throws PathBetweenDifferentSystemException {
        CelestialBody start;
        CelestialBody end;
        try {
            start = findCelestialBody(startIdentifier);
            end = findCelestialBody(endIdentifier);
        } catch(CelestialBodyNotFoundException e) {
            return e.getMessage();
        }

        //Check if the start and end are the same.
        if(startIdentifier.equals(endIdentifier))
            return "Nessun percorso richiesto.";

        //Casts the start and end into their specific type of CelestialBody and produces the path accordingly.
        if(start instanceof Moon startMoon){
            if(end instanceof Moon endMoon){
                return pathToString(moonToMoon(startMoon,endMoon));
            } else if (end instanceof Planet endPlanet){
                return pathToString(moonToPlanet(startMoon,endPlanet));
            } else if (end instanceof Star endStar){
                return pathToString(moonToStar(startMoon,endStar));
            }
        } else if (start instanceof Planet startPlanet){
            if(end instanceof Moon endMoon){
                return pathToString(planetToMoon(startPlanet,endMoon));
            } else if (end instanceof Planet endPlanet){
                return pathToString(planetToPlanet(startPlanet,endPlanet));
            } else if (end instanceof Star endStar){
                return pathToString(planetToStar(startPlanet,endStar));
            }
        } else if (start instanceof Star startStar){
            if (end instanceof Moon endMoon){
                return pathToString(starToMoon(startStar,endMoon));
            } else if (end instanceof Planet endPlanet){
                return pathToString(starToPlanet(startStar,endPlanet));
            }
        }

        throw new PathBetweenDifferentSystemException(start,end);
    }

    //Converts list of celestial bodies to a string representing a path.
    //Does not check if the path makes sense, that is up to the methods that creates it.
    private static String pathToString(ArrayList<CelestialBody> path) {
        StringBuffer sPath = new StringBuffer(String.format("%s ", path.get(0).getIdentifier()));

        for(int i = 1; i < path.size(); i++)
            sPath.append(String.format(" > %s", path.get(i).getIdentifier()));

        return sPath.toString();
    }


    private ArrayList<CelestialBody> moonToMoon(Moon start, Moon end) throws PathBetweenDifferentSystemException{
        var path = moonToPlanet(start,end.getPlanet());
        path.add(end);
        return path;
    }


    private ArrayList<CelestialBody> moonToPlanet(Moon start, Planet end) throws PathBetweenDifferentSystemException{
        var path = planetToMoon(end,start);
        Collections.reverse(path);
        return path;
    }


    private ArrayList<CelestialBody> moonToStar(Moon start, Star end) throws PathBetweenDifferentSystemException{
        var path = starToMoon(end,start);
        Collections.reverse(path);
        return path;
    }




    private ArrayList<CelestialBody> planetToMoon(Planet start, Moon end) throws PathBetweenDifferentSystemException{
        try {
            return start.pathToMoon(end);
        } catch(CelestialBodyNotFoundException e) {
            //Only executed if the moon does not orbit the planet
            var path = planetToPlanet(start,end.getPlanet());
            path.add(end);
            return path;
        }

    }


    private ArrayList<CelestialBody> planetToPlanet(Planet start, Planet end) throws PathBetweenDifferentSystemException{
        var path = planetToStar(start,end.getStar());
        path.add(end);
        return path;
    }


    private ArrayList<CelestialBody> planetToStar(Planet start, Star end) throws PathBetweenDifferentSystemException{
        var path = starToPlanet(end,start);
        Collections.reverse(path);
        return path;
    }


    private ArrayList<CelestialBody> starToMoon(Star start, Moon end) throws PathBetweenDifferentSystemException {
        var path = start.pathToPlanet(end.getPlanet());
        path.add(end);
        return path;
    }


    private ArrayList<CelestialBody> starToPlanet(Star start, Planet end) throws PathBetweenDifferentSystemException{
        return start.pathToPlanet(end);
    }

}