package planetarium.solarsystem.error;

import planetarium.solarsystem.CelestialBody;

public class PathBetweenDifferentSystemException extends Exception{
    public PathBetweenDifferentSystemException(CelestialBody fromSystem, CelestialBody fromAnotherSystem) {
        this(String.format("%s %s and %s %s do not belong to the same system.",
                fromSystem.getClass().getSimpleName(), fromSystem.getIdentifier(),
                fromAnotherSystem.getClass().getSimpleName(), fromAnotherSystem.getIdentifier()));
    }

    private PathBetweenDifferentSystemException(String message) {
        super(message);
    }
}
