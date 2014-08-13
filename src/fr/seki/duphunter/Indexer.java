
package fr.seki.duphunter;

/**
 *
 * @author Sebastien
 */
public interface Indexer {

	void process();

	void setOutput(OutputKind kind, String output);
	
}
