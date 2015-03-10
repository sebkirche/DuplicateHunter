
package fr.seki.duphunter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Dump the index into standard output
 * @author Sebastien
 */
public class StdoutDumper implements IndexDumper{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void dump(List<IndexNode> index, String output) {
		for (IndexNode node : index) {
			System.out.println(node.getCanonicalPath() 
					+ " (" 
					+ node.getAuthor() 
					+ " / " 
					+ String.valueOf(node.getSize())
					+ " / " 
					+ sdf.format(node.getDate()) 
					+ ") " 
					+ node.getChecksum());
		}
	}
	
}
