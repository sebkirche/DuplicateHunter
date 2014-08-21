

package fr.seki.duphunter.gui;

/**
 * Storage class for an indexing source properties
 * 
 * @author Sebastien
 */
public class SourceData {
	public String path;
	public boolean active;

	public SourceData(String path, boolean active) {
		this.path = path;
		this.active = active;
	}
	
}
