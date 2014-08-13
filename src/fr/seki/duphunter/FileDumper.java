
package fr.seki.duphunter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author Sebastien
 */
public class FileDumper implements IndexDumper {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void dump(List<IndexNode> index, String output) {
		try {
			PrintWriter pw = new PrintWriter(output, "UTF-8");
			for (IndexNode node : index) {
				pw.printf("%s\t%s\n", node.getChecksum(), node.getCanonicalPath());
			}
			pw.close();

		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			//Logger.getLogger(RepositoryIndexer.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while creating index file " + output + ":" + ex.getMessage());
		}
	}
}
