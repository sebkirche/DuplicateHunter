
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
	
	STDOUT("STDOUT"), 
	FILE("File"), 
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
