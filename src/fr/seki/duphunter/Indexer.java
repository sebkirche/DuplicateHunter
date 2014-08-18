
package fr.seki.duphunter;

/**
 *
 * @author Sebastien
 */
public abstract class Indexer {
	/**
	 * Wether or not we should consider files with size of 0
	 */
	boolean ignoreEmptyFiles = true;
	abstract void process();
	abstract void setOutput(OutputKind kind, String output);
	
	void countEmptyFiles(){ ignoreEmptyFiles = false; }
}
