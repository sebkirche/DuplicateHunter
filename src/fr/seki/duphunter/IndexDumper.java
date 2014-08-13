
package fr.seki.duphunter;

import java.util.List;

/**
 *
 * @author Sebastien
 */
public interface IndexDumper {
	public void dump(List<IndexNode> index, String output);
}
