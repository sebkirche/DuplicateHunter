

package fr.seki.tinytests;

import fr.seki.duphunter.SVNWorkingCopyHelper;
import java.io.File;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNInfoHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 *
 * @author Sebastien
 */
public class TestSvnInfo {
	
	SVNClientManager clientMgr;
	
	public static void main (String[] args) throws SVNException{
		new TestSvnInfo().showInfo(new File("C:/svn/docs_conceptware/Tests/data generator/Excel generator"), SVNRevision.WORKING, true);
		
		System.out.println(new SVNWorkingCopyHelper().getRepoUrl(new File("C:/svn/docs_conceptware/Tests/data generator")));
		System.out.println(new SVNWorkingCopyHelper().getRepoUrl(new File("C:/")));
		
		new TestSvnInfo().showInfo(new File("C:/temp"), SVNRevision.WORKING, true);
		
		
	}

	public TestSvnInfo() {
		clientMgr = SVNClientManager.newInstance();
	}
	
	
	void showInfo(File path, SVNRevision rev, boolean recursive) throws SVNException{
		
		SVNWCClient cl = clientMgr.getWCClient();
		SVNInfo r = cl.doInfo(path, rev);
		System.out.println("Repository of " + path.getPath() + " is " + r.getRepositoryRootURL());
	}
	
	class InfoHandler implements ISVNInfoHandler{

		@Override
		public void handleInfo(SVNInfo info) throws SVNException {
			
		}
		
	}
}
