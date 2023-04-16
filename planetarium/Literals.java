package planetarium;

public class Literals {
    protected static final String INVALID_NUMBER = "ATTENZIONE: Il numero inserito non e' valido!";

	protected static final String INSERT_STAR_X = "Inserire la coordinata X della stella: ";
	protected static final String INSERT_STAR_Y = "Inserire la coordinata Y della stella: ";
	protected static final String INSERT_STAR_MASS = "Inserire la massa della stella: ";

	protected static final String INSERT_PLANET_X = "Inserire coordinata X del pianeta (relativa alla sua stella): ";
	protected static final String INSERT_PLANET_Y = "Inserire coordinata Y del pianeta (relativa alla sua stella): ";
	protected static final String INSERT_PLANET_MASS = "Inserire la massa del pianeta: ";

	protected static final String INSERT_MOON_X = "Inserire coordinata X della luna (relativa al suo pianeta): ";
	protected static final String INSERT_MOON_Y = "Inserire coordinata Y della luna (relativa al suo pianeta): ";
	protected static final String INSERT_MOON_MASS = "Inserire la massa della luna: ";

	protected static final String NUMBER_OF_PLANET_TO_GENERATE_PROMPT = "\nQuanti pianeti vuoi generare? [Max %d]\nInserisci una quantita': ";
	protected static final String NUMBER_OF_MOONS_PER_PLANET_TO_GENERATE_PROMPT = "\nQuante lune vuoi generare per pianeta? [Max %d]\nInserisci una quantita': ";

	protected static final String GENERATE_PLANET_ERROR_MESSAGE = "Sono ammessi un massimo di %d pianeti!";
	protected static final String GENERATE_MOON_ERROR_MESSAGE = "Sono ammesse un massimo di %d lune per pianeta!";
	protected static final String INSERT_PLANET_ID_FOR_MOON_CREATION = "Inserire ID del pianeta a cui aggiungere la luna: ";
	protected static final String INSERT_PLANET_ID_TO_REMOVE = "Inserire ID del pianeta da rimuovere: ";

	protected static final String PLANET_FORMAT = " |__ %s\t\t%s\n";
	protected static final String MOON_FORMAT = " |      |__ %s\t%s\n";
	protected static final String LAST_PLANET_FORMAT = " |__ %s\t\t%s\n";
	protected static final String LAST_PLANET_MOON_FORMAT = "        |__ %s\t%s\n";
	protected static final String STAR_FORMAT = "%s\t\t\t%s\n";
	
	protected static final String INSERT_MOON_ID = "Inserire ID della luna: ";
	protected static final String NOT_A_MOON_ID = "Non hai inserito l'id di una luna.";
	protected static final String CENTER_OF_MASS_FORMAT = "Il centro di massa è alle coordinate ( %.3f, %.3f )\n";
	protected static final String INSERT_CELESTIAL_BODY_ID = "Inserire ID del corpo celeste: ";
	protected static final String INSERT_FIRST_BODY_ID = "Inserire l'identificativo del primo corpo celeste: ";
	protected static final String INSERT_SECOND_BODY_ID = "Inserire l'identificativo del secondo corpo celeste: ";
	protected static final String POSSIBLE_COLLISIONS = "ATTENZIONE!!! Possibili collisioni tra corpi celesti!";
	protected static final String NO_COLLISIONS = "Tutto tranquillo. Nessuna collisione rilevata.";
	protected static final String ALL_PLANETS_CANCELLED = "Tutti i pianeti e lune sono stati cancellati!";
}
