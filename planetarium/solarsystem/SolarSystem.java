package planetarium.solarsystem;

import planetarium.solarsystem.error.CelestialBodyNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        long systemMass = getSystemTotalMass();
        Position centerOfMass = new Position(0, 0);

        // Add weighted position of the Star
        double starMass = getStar().getMass();
        Position adjustedStarPosition = getStar().getAbsolutePosition().multiplyBy(starMass);
        centerOfMass.increase(adjustedStarPosition);

        // Add weighted positions of the Planets
        List<Planet> planets = getStar().getPlanets();
        for(Planet planet : planets) {
            double planetMass = planet.getMass();
            Position adjustedPlanetPosition = planet.getAbsolutePosition().multiplyBy(planetMass);
            centerOfMass.increase(adjustedPlanetPosition);

            List<Moon> moons = planet.getMoons();
            // Add weighted positions of the Moons for every Planet
            for(Moon moon : moons) {
                double moonMass = moon.getMass();
                Position adjustedMoonPosition = moon.getAbsolutePosition().multiplyBy(moonMass);
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

        List<Planet> planets = getStar().getPlanets();

        // Add mass of all the planets and their moons to the total
        for (Planet planet: planets) {
            totalMass += planet.getMass();

            // Add mass of all the moons of the planet to the total
            List<Moon> moons = planet.getMoons();
            for(Moon moon : moons)
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
        Star star = getStar();
        if(identifier.equals(star.getIdentifier()))
            return star;

        try {
            return star.findPlanet(identifier);
        } catch(CelestialBodyNotFoundException planetNotFound) {
            for(Planet planet : star.getPlanets()) {
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
        List<Planet> planets = getStar().getPlanets();
        ArrayList<Double> distancesFromStar = new ArrayList<>();

        for(Planet planet : planets) {
            double distance = planet.distanceToParent();
            if(distancesFromStar.contains(distance))
                return true;

            distancesFromStar.add(distance);
        }
        return false;
    }

    //Check collisions of moons with the star
    //Returns true if found
    private boolean checkCollisionStarAndMoons() {
        List<Planet> planets = getStar().getPlanets();

        for(Planet planet : planets) {
            List<Moon> moons = planet.getMoons();
            for(Moon moon : moons) {
                if(planet.distanceToParent() <= moon.distanceToParent())
                    return true;
            }
        }

        return false;
    }

    //Check collisions within moons.
    //Returns true if found
    private boolean checkCollisionsBetweenMoons() {
        List<Planet> planets = getStar().getPlanets();

        for (int i = 0; i < planets.size(); i++) {
            for (int j = i; j < planets.size(); j++) {
                Planet first = planets.get(i);
                Planet second = planets.get(j);

                List<Moon> firstMoons = first.getMoons();
                List<Moon> secondMoons = second.getMoons();

                for (Moon firstMoon : firstMoons) {
                    for (Moon secondMoon : secondMoons) {
                        if(canMoonsCollide(firstMoon,secondMoon)){
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
        if(first.getParent() == second.getParent()){
            return (first.distanceToParent() == second.distanceToParent());
        }

        final Planet firstPlanet = (Planet)first.getParent();
        final Planet secondPlanet = (Planet)second.getParent();
        //equivalent of the distance between the two planets if they are aligned in their orbits.
        final double distanceToStarDifference =
                Math.abs( firstPlanet.distanceToParent() - secondPlanet.distanceToParent() );

        return (first.distanceToParent() + second.distanceToParent() >= distanceToStarDifference);
    }

    //Checks collisions within planets and moons of other planets
    //Returns true if found
    private boolean checkCollisionPlanetsAndMoons() {
        final List<Planet> planets = getStar().getPlanets();

        for (Planet firstPlanet: planets) {
            for (Planet otherPlanet : planets) {
                final List<Moon> otherPlanetMoons = otherPlanet.getMoons();

                //Distance between the two planets if they were aligned in their orbits.
                final double distanceToStarDifference =
                        Math.abs(firstPlanet.distanceToParent() - otherPlanet.distanceToParent());

                for (Moon moon : otherPlanetMoons) {
                    if (moon.distanceToParent() >= distanceToStarDifference)
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
     * @return <p>A list that represents the path between the two celestial bodies.
     * May throw CelestialBodyNotFoundException if either of the two celestial bodies are not found in the system</p>
     */
    public List<CelestialBody> findPath(String startIdentifier,String endIdentifier) throws CelestialBodyNotFoundException {
        CelestialBody start = findCelestialBody(startIdentifier);
        CelestialBody end = findCelestialBody(endIdentifier);

        //Check if the start and end are the same.
        if(startIdentifier.equals(endIdentifier))
            return new ArrayList<>();

        LinkedList<CelestialBody> fromStart = new LinkedList<>();
        LinkedList<CelestialBody> fromEnd = new LinkedList<>();
        fromStart.add(start);
        fromEnd.add(end);

        //While common parent not found
        while(fromStart.getFirst() != fromEnd.getFirst()){
            //Checks if the current bodies are satellites.
            //If true, add the parents to the linked lists.
            if(fromStart.getFirst() instanceof Satellite){
                fromStart.push(((Satellite) fromStart.getFirst()).getParent());
            }
            if(fromEnd.getFirst() instanceof Satellite){
                fromEnd.push(((Satellite) fromEnd.getFirst()).getParent());
            }
        }

        Collections.reverse(fromStart);
        fromStart.addAll(fromEnd);
        return fromStart;
    }

}