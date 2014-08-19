
package fr.seki.duphunter;

/**
 * Types of available outputs
 * - screen
 * - file
 * - database (SQLite)
 * 
 * @author Sebastien
 */
public enum OutputKind {
	
	/** output to the console */
	STDOUT("STDOUT"), 
	/** output to a text file */
	FILE("File"), 
	/** output to a database file */
	DB("DB");
	
	private final String text;

	private OutputKind(String out) {
		text = out;
	}

	@Override
	public String toString() {
		return text;
	}

	public static OutputKind fromString(String inp) {
		for (OutputKind ok : values()) {
			if (ok.text.equalsIgnoreCase(inp)) {
				return ok;
			}
		}
		return STDOUT;
	}
	
}
