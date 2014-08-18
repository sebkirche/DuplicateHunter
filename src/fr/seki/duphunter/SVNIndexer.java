
package fr.seki.duphunter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 * RepositoryIndexer can walk a given SVN repository and return a list of file checksums
 *
 * @author Sebastien
 */
public class SVNIndexer extends Indexer {

	private String repoUrl;
	private String repoRoot;
	private String baseDir = "";
	
	private OutputKind outKind;
	private String outName;

	public SVNIndexer(String url, OutputKind kind, String output) {
		repoUrl = url;
		outKind = kind;
		outName = output;
	}
	
	public SVNIndexer(String url){
		repoUrl = url;
		outKind = OutputKind.STDOUT;
		outName = null;
	}
	
	@Override
	public void process(){
		List<IndexNode> index = crawlRepo(repoUrl);
		switch(outKind){
			case STDOUT:
				dumpToStdout(index);
				break;
			case FILE:
				dumpToFile(index);
				break;
			case DB:
				dumpToBase(index);
				break;
		}
		System.out.println(index.size() + " items indexed");
	}

	private void dumpToStdout(List<IndexNode> index) {
		new StdoutDumper().dump(index, null);
	}

	private void dumpToFile(List<IndexNode> index) {
		new FileDumper().dump(index, outName);
	}

	private void dumpToBase(List<IndexNode> index) {	
		new SqliteDbDumper().dump(index, outName);
	}
	
	/**
	 * Access a repository by its url and return a list for its content.
	 *
	 * @param repoUrl the url
	 * @return a list of IndexNodes
	 */
	private List<IndexNode> crawlRepo(String repoUrl) {
		List<IndexNode> index = new ArrayList<>();
		SVNRepository repo = null;
		try {
			repo = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repoUrl));
		} catch (SVNException svne) {
			System.err.println("Error while creating repository data for location '" + repoUrl + "':" + svne.getMessage());
			System.exit(1);
		}

		/* if authentication needed
		 ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		 repository.setAuthenticationManager(authManager);
		 */

		try {
			SVNNodeKind nodeKind = repo.checkPath("", -1);	//-1 = HEAD
			if (nodeKind == SVNNodeKind.NONE) {
				System.err.println("There is nothing at '" + repoUrl + "' ?");
				System.exit(1);
			} else if (nodeKind == SVNNodeKind.FILE) {
				System.err.println("Entry at '" + repoUrl + "' is a file while I am expecting a directory...");
				System.exit(1);
			} else if (nodeKind == SVNNodeKind.UNKNOWN) {
				System.err.println("I dunno what is located at '" + repoUrl + "' ??!");
				System.exit(1);
			}

			//DEBUG:
			baseDir = repo.getLocation().toDecodedString();
			repoRoot = repo.getRepositoryRoot(true).toDecodedString();
			System.out.printf("Repo root/curr rev/UUID = %s / %d / %s\n", repoRoot, repo.getLatestRevision(), repo.getRepositoryUUID(true));

			addEntriesToList(repo, "", index);

		} catch (SVNException svne) {
			//Logger.getLogger(RepositoryIndexer.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while indexing repository: " + svne.getMessage());
		}

		return index;
	}

	/**
	 * Recursive walker for the repository
	 *
	 * @param repo
	 * @param path current path (will be augmented while descending the repository arborescence
	 * @param index the {@link fr.seki.svnhelper.IndexNode} list that is filled during walking
	 * @throws SVNException
	 */
	private void addEntriesToList(SVNRepository repo, String path, List<IndexNode> index) throws SVNException {
		Collection entries = repo.getDir(path, -1, null, (Collection) null);
		SVNProperties fileProps = new SVNProperties();

		if (!path.isEmpty()) {
			path += "/";
		}

		Iterator iter = entries.iterator();
		while (iter.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iter.next();

			if (entry.getKind() == SVNNodeKind.FILE) {
				if(ignoreEmptyFiles && entry.getSize()==0)
					continue;
				IndexNode node = new IndexNode();
				node.setRepoRoot(repoUrl);
				node.setName(entry.getName());
				node.setCanonicalPath(baseDir + "/" + path + entry.getName());
				node.setAuthor(entry.getAuthor());
				node.setSize(entry.getSize());
				node.setDate(entry.getDate());
				repo.getFile(path + entry.getName(), -1, fileProps, null);
				node.setChecksum(fileProps.getSVNPropertyValue("svn:entry:checksum").toString());
				index.add(node);
			}

			if (entry.getKind() == SVNNodeKind.DIR) {
				addEntriesToList(repo, path + entry.getName(), index);
			}

		}
	}

	@Override
	public void setOutput(OutputKind kind, String output) {
		this.outKind = kind;
		this.outName = output;
	}
	
}
