
package fr.seki.duphunter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A simple FileFilter to accept a single extension
 * @author Sebastien
 */
public class SimpleExtFileFilter extends FileFilter{

	private String ext;
	
	/**
	 * Make a FileFilter from the extension
	 * @param extension the file extension (without the '.')
	 */
	public SimpleExtFileFilter(String extension) {
		
		ext = "." + extension;
		
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		else
			return f.getName().toLowerCase().endsWith(ext);
	}

	@Override
	public String getDescription() {
		return "Duplicate Hunter files (*" + ext + ")";
	}
	
}
