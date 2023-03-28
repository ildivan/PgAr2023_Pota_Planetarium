package planetarium.solarsystem;

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

        //Multiply star coordinates by its mass
        final double starMass = getStar().getMass();
        final Position adjustedStarPosition = getStar().getPosition().multiplyBy(starMass);
        centerOfMass.increase(adjustedStarPosition);

        //Multiply planets coordinates by their mass
        final var planets = getStar().getPlanets();
        for(final var planet : planets){
            final double planetMass = planet.getMass();
            final Position adjustedPlanetPosition = planet.getPosition().multiplyBy(planetMass);
            centerOfMass.increase(adjustedPlanetPosition);

            final var moons = planet.getMoons();
            //Multiply moons coordinates by their mass
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
}
