package fr.seki.duphunter;

import java.io.File;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * Utility class for SVN working copies
 * @author Sebastien
 */
public class SVNWorkingCopyHelper {

	private SVNWCClient wcClt;

	public SVNWorkingCopyHelper() {
		SVNClientManager clientMgr = SVNClientManager.newInstance();
		wcClt = clientMgr.getWCClient();
	}
	
	/**
	 * Getter for the working copy repository root URL
	 * @param path working copy directory
	 * @return the URL of the repository root
	 */
	public String getRepoUrl(File path){
		try {
			SVNInfo info = wcClt.doInfo(path, SVNRevision.WORKING);
			return info.getRepositoryRootURL().toDecodedString();
		} catch (SVNException ex) {
			if(!ex.getMessage().contains("not a working copy"))
				ex.printStackTrace();
			return null;
		}
	}

}
