package planetarium.solarsystem.error;

import planetarium.solarsystem.CelestialBody;

public class CelestialBodyNotFoundException extends Exception{
    public CelestialBodyNotFoundException(CelestialBody toFind) {
        super(String.format("%s %s not found",toFind.getClass().getSimpleName(),toFind.getIdentifier()));
    }

    public CelestialBodyNotFoundException(String identifier) {
        super(String.format("Celestial body %s not found",identifier));
    }

}
