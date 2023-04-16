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
        final var star = getStar();
        if(identifier.equals(star.getIdentifier()))
            return star;

        try {
            return star.findPlanet(identifier);
        } catch(CelestialBodyNotFoundException planetNotFound) {
            for(final Planet planet : star.getPlanets()) {
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
        return checkCollisionBetweenPlanets() || checkCollisionsBetweenMoons()
                || checkCollisionStarAndMoons() || checkCollisionPlanetsAndMoons();
    }

    //Check collisions between planets
    //Returns true if found
    private boolean checkCollisionBetweenPlanets() {
        final var planets = getStar().getPlanets();
        ArrayList<Double> distancesFromStar = new ArrayList<>();

        for(final Planet planet : planets) {
            double distance = planet.distanceToStar();
            if(distancesFromStar.contains(distance))
                return true;

            distancesFromStar.add(distance);
        }
        return false;
    }

    //Check collisions of moons with the star
    //Returns true if found
    private boolean checkCollisionStarAndMoons() {
        final var planets = getStar().getPlanets();

        for(final Planet planet : planets) {
            final var moons = planet.getMoons();
            for(final Moon moon : moons) {
                if(planet.distanceToStar() <= moon.distanceToPlanet())
                    return true;
            }
        }

        return false;
    }

    //Check collisions within moons.
    //Returns true if found
    private boolean checkCollisionsBetweenMoons() {
        final var planets = getStar().getPlanets();

        for (int i = 0; i < planets.size(); i++) {
            for (int j = i; j < planets.size(); j++) {
                final Planet first = planets.get(i);
                final Planet second = planets.get(j);

                final var firstMoons = first.getMoons();
                final var secondMoons = second.getMoons();

                for (int k = 0; k < firstMoons.size(); k++) {
                    for (int l = 0; l < secondMoons.size(); l++) {
                        if(canMoonsCollide(firstMoons.get(i),secondMoons.get(j))){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //Checks if two moons can collide
    private boolean canMoonsCollide(Moon first, Moon second){
        //If the two moons are from the same planet they collide if they have the same orbiting radius.
        if(first.getPlanet() == second.getPlanet()){
            return (first.distanceToPlanet() == second.distanceToPlanet());
        }

        final Planet firstPlanet = first.getPlanet();
        final Planet secondPlanet = second.getPlanet();
        //equivalent of the distance between the two planets if they are aligned in their orbits.
        final double distanceToStarDifference = Math.abs( firstPlanet.distanceToStar() - secondPlanet.distanceToStar() );

        return (first.distanceToPlanet() + second.distanceToPlanet() >= distanceToStarDifference);
    }

    //Checks collisions within planets and moons of other planets
    //Returns true if found
    private boolean checkCollisionPlanetsAndMoons() {
        final var planets = getStar().getPlanets();

        for (final Planet firstPlanet: planets) {
            for (final Planet otherPlanet : planets) {
                final var otherPlanetMoons = otherPlanet.getMoons();

                //Distance between the two planets if they were aligned in their orbits.
                final double distanceToStarDifference =
                        Math.abs(firstPlanet.distanceToStar() - otherPlanet.distanceToStar());

                for (final Moon moon : otherPlanetMoons) {
                    if (moon.distanceToPlanet() >= distanceToStarDifference)
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
        final CelestialBody start;
        final CelestialBody end;
        try {
            start = findCelestialBody(startIdentifier);
            end = findCelestialBody(endIdentifier);
        } catch(CelestialBodyNotFoundException e) {
            return e.getMessage();
        }

        //Check if the start and end are the same.
        if(startIdentifier.equals(endIdentifier))
            return "No path required.";

        //Casts the start into its specific type of CelestialBody and calls the appropriate method.
        if(start instanceof Moon startMoon){
            return pathFromMoon(startMoon,end);
        }
        if (start instanceof Planet startPlanet){
            return pathFromPlanet(startPlanet,end);
        }
        return pathFromStar((Star) start, end);
    }



    //Method used for all the paths that starts from a moon.
    private String pathFromMoon(Moon start, CelestialBody end) throws PathBetweenDifferentSystemException{
        if(end instanceof Moon endMoon){
            return pathToString(moonToMoon(start,endMoon));
        }
        if (end instanceof Planet endPlanet){
            return pathToString(moonToPlanet(start,endPlanet));
        }
        return pathToString(moonToStar(start,(Star)end));
    }
    //Method used for all the paths that starts from a planet.
    private String pathFromPlanet(Planet start, CelestialBody end) throws PathBetweenDifferentSystemException{
        if(end instanceof Moon endMoon){
            return pathToString(planetToMoon(start,endMoon));
        }
        if (end instanceof Planet endPlanet){
            return pathToString(planetToPlanet(start,endPlanet));
        }
        return pathToString(planetToStar(start,(Star)end));

    }
    //Method used for all the paths that starts from a star.
    private String pathFromStar(Star start, CelestialBody end) throws PathBetweenDifferentSystemException{
        if (end instanceof Moon endMoon){
            return pathToString(starToMoon(start,endMoon));
        }
        if (end instanceof Planet endPlanet){
            return pathToString(starToPlanet(start,endPlanet));
        }
        //The only case not covered is the path between two stars, and it throws an exception in that case.
        throw new PathBetweenDifferentSystemException(start,end);
    }



    //Converts list of celestial bodies to a string representing a path.
    //Does not check if the path makes sense, that is up to the methods that creates it.
    private static String pathToString(ArrayList<CelestialBody> path) {
        if (path == null || path.isEmpty()) return "";

        StringBuilder sPath = new StringBuilder(String.format("%s ", path.get(0).getIdentifier()));

        for(int i = 1; i < path.size(); i++)
            sPath.append(String.format(" > %s", path.get(i).getIdentifier()));

        return sPath.toString();
    }

    //Path between two moons.
    private ArrayList<CelestialBody> moonToMoon(Moon start, Moon end) throws PathBetweenDifferentSystemException{
        var path = moonToPlanet(start,end.getPlanet());
        path.add(end);
        return path;
    }

    //Path from a moon to a planet, the reverse of the path from the planet to the moon.
    private ArrayList<CelestialBody> moonToPlanet(Moon start, Planet end) throws PathBetweenDifferentSystemException{
        var path = planetToMoon(end,start);
        Collections.reverse(path);
        return path;
    }

    //Path from a moon to a star, the reverse of the path from the star to the moon.
    private ArrayList<CelestialBody> moonToStar(Moon start, Star end) throws PathBetweenDifferentSystemException{
        var path = starToMoon(end,start);
        Collections.reverse(path);
        return path;
    }

    //Path from a planet to a moon.
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

    //Path between two planets.
    private ArrayList<CelestialBody> planetToPlanet(Planet start, Planet end) throws PathBetweenDifferentSystemException{
        var path = planetToStar(start,end.getStar());
        path.add(end);
        return path;
    }

    //Path from a planet to a star.
    private ArrayList<CelestialBody> planetToStar(Planet start, Star end) throws PathBetweenDifferentSystemException{
        var path = starToPlanet(end,start);
        Collections.reverse(path);
        return path;
    }

    //Path from a star to a moon.
    private ArrayList<CelestialBody> starToMoon(Star start, Moon end) throws PathBetweenDifferentSystemException {
        var path = start.pathToPlanet(end.getPlanet());
        path.add(end);
        return path;
    }

    //Path from a star to a planet.
    private ArrayList<CelestialBody> starToPlanet(Star start, Planet end) throws PathBetweenDifferentSystemException{
        return start.pathToPlanet(end);
    }

}