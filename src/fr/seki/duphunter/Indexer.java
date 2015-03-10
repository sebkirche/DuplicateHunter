package fr.seki.duphunter;

/**
 * A common ancestor for Indexers
 * @author Sebastien
 */
public abstract class Indexer {

	/**
	 * start the file indexation
	 */
	abstract void process();

	/**
	 * Specifies the kind of output
	 * @param kind one of the {@link OutputKind} possible values
	 * @param output can be the path to the text or database file, or null if useless (e.g. for {@link OutputKind#STDOUT})
	 */
	abstract void setOutput(OutputKind kind, String output);
}
