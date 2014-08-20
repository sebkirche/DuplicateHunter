package fr.seki.duphunter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;

/**
 * the main controller for the index
 * @author Sebastien
 */
public class IndexController {

	IndexModel model;
	boolean svnInit = false;

	public IndexController(IndexModel model) {
		this.model = model;
	}

	/**
	 * Open an existing SQLite database file
	 * @param f 
	 */
	public void openDB(File f) {
		if (f.exists()) {
			model.connectDBFile(f);
		}
	}

	/**
	 * Create a new database file (and create the db schema)
	 * @param f 
	 */
	public void newDB(File f) {
		model.initDBFile(f);
	}

	/**
	 * Discover and get the files attributes from the given path.
	 * The pas can be a repository url or a local filesystem
	 * @param path
	 * @param ignoreEmpty 
	 */
	public void index(String path) {
		Indexer indexer;
		model.disconnect();
		if (path.startsWith("svn://")) {
			try {
				if (!svnInit) {
					setupSvnLib();
					svnInit = true;
				}
				indexer = new SVNIndexer(path);
				indexer.setOutput(OutputKind.DB, model.getDbFile().getCanonicalPath());
				indexer.process();
			} catch (IOException ex) {
				Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			try {
				indexer = new FSIndexer(path);
				indexer.setOutput(OutputKind.DB, model.getDbFile().getCanonicalPath());
				indexer.process();
			} catch (IOException ex) {
				Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		model.reconnect();
	}

	private static void setupSvnLib() {
		DAVRepositoryFactory.setup(); //support for http/https
		SVNRepositoryFactoryImpl.setup(); //support for svn/svn+xxx
		FSRepositoryFactory.setup();	//support for file://
	}

}
