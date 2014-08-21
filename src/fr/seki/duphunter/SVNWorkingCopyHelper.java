package fr.seki.duphunter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 *
 * @author Sebastien
 */
public class SVNWorkingCopyHelper {

	private SVNWCClient wcClt;

	public SVNWorkingCopyHelper() {
		SVNClientManager clientMgr = SVNClientManager.newInstance();
		wcClt = clientMgr.getWCClient();
	}
	
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
