
package fr.seki.duphunter;

import java.util.Date;

/**
 *	Object to store svn entry properties, simpler than SVNDirEntry
 * 
 * @author Sebastien
 */
public class IndexNode {

	private String _name;
	private String _canonicalPath;
	private String _author;
	private long _size;
	private String _checksum;
	private Date _lastDate;
	private String _repoRoot;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public long getSize() {
		return _size;
	}

	public void setSize(long size) {
		this._size = size;
	}

	public String getAuthor() {
		return _author;
	}

	public void setAuthor(String author) {
		this._author = author;
	}

	public String getChecksum() {
		return _checksum;
	}

	public void setChecksum(String checksum) {
		this._checksum = checksum;
	}

	public String getCanonicalPath() {
		return _canonicalPath;
	}

	public void setCanonicalPath(String canonicalPath) {
		this._canonicalPath = canonicalPath;
	}

	public Date getDate() {
		return _lastDate;
	}
	
	public void setDate(Date date) {
		_lastDate = date;
	}

	public String getRepoRoot() {
		return _repoRoot;
	}

	public void setRepoRoot(String repoRoot) {
		this._repoRoot = repoRoot;
	}
}
