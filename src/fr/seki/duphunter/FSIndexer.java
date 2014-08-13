package fr.seki.duphunter;

import com.twmacinta.util.MD5;
import static fr.seki.duphunter.OutputKind.DB;
import static fr.seki.duphunter.OutputKind.FILE;
import static fr.seki.duphunter.OutputKind.STDOUT;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 *
 * @author Sebastien
 */
public class FSIndexer implements Indexer {

	private OutputKind outKind;
	private String outName;
	private String fsPath;

	public FSIndexer(String path) {
		fsPath = path;
		outKind = OutputKind.STDOUT;
		outName = null;
	}

	@Override
	public void process() {
		List<IndexNode> index = crawlFS(fsPath);
		switch (outKind) {
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

	@Override
	public void setOutput(OutputKind kind, String output) {
		outKind = kind;
		outName = output;
	}

	private List<IndexNode> crawlFS(String fsPath) {
		List<IndexNode> index = new ArrayList<>();
		File entryDir = new File(fsPath);
		MD5.initNativeLibrary(false);
		
		System.out.println("Computing files count...");
		Collection<File> files = FileUtils.listFiles(entryDir,
				FileFilterUtils.trueFileFilter(),
				FileFilterUtils.and(
				FileFilterUtils.directoryFileFilter(),
				FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter(".svn"))));
		System.out.println("Done. " + String.valueOf(files.size()) + " files found. Now Indexing files...");
		
		int shunk = files.size() / 20;
		int c = 0;
		for (File f : files) {
			try {
				/* try to have some visual prograssion */
				c++;
				if(c % shunk == 0)
					System.out.print(".");
				
				IndexNode node = new IndexNode();
				node.setCanonicalPath(f.getCanonicalPath());
				node.setRepoRoot(fsPath);
				node.setName(f.getName());
				node.setSize(f.length());
				node.setDate(new Date());
				Path p = Paths.get(f.toURI());
				node.setAuthor(Files.getOwner(p, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}).getName());
				node.setChecksum(MD5.asHex(MD5.getHash(f)));
				index.add(node);
			} catch (IOException ex) {
				System.err.println("Error while processing file " + f.getPath() + ": " + ex.getMessage());
			}	
		}
		System.out.println("\nDone.");

		return index;
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
}
