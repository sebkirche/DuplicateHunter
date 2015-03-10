
package fr.seki.duphunter;

import java.util.List;

/**
 * Interface for Index dumpers (to save an index into something like a file, a database...)
 * @author Sebastien
 */
public interface IndexDumper {
	public void dump(List<IndexNode> index, String output);
}
