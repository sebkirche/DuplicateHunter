package fr.seki.duphunter;

import com.twmacinta.util.MD5;
import static fr.seki.duphunter.OutputKind.*;
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
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 *
 * @author Sebastien
 */
public class FSIndexer extends Indexer {

	private OutputKind outKind;
	private String outName;
	private String fsPath;
	private UndefinedConsoleProgressor indetProgress = new UndefinedConsoleProgressor();

	public FSIndexer(String path) {
		fsPath = path;
		outKind = OutputKind.STDOUT;
		outName = null;
	}

	@Override
	public void process() {
		System.out.println("Indexing " + fsPath + "...");
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
		if(fsPath.endsWith(File.pathSeparator))
			fsPath = fsPath.substring(0, fsPath.length()-1);
		File entryDir = new File(fsPath);
		
		MD5.initNativeLibrary(false);
		
		System.out.println("Computing files count... ");
		Collection<File> files = FileUtils.listFiles(entryDir,
				FileFilterUtils.trueFileFilter(),
				new SpecialDirFilter()
				);
		
		System.out.println(" Done. " + String.valueOf(files.size()) + " files found.");
                System.out.println("Indexing files... ");
		
		int count = 0, emptyCount = 0;
		DefinedConsoleProgressor bar = new DefinedConsoleProgressor(files.size());

		for (File f : files) {
			try {
				/* try to have some visual progression */
				count++;
				if(count % 20 == 0)
					bar.progress(count);
				
				if(f.length()==0){
					emptyCount++;
					continue;
				}
				
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
		bar.progress(count);
		System.out.print(" Done.");
		if(emptyCount > 0)
			System.out.print(" ("+String.valueOf(emptyCount)+" empty files ignored)");
		System.out.println();

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
	
	private class SpecialDirFilter implements IOFileFilter{
		private long count = 0;

		@Override
		public boolean accept(File file) {
			count++;
			if(count % 10 == 0)
				indetProgress.progress();
			return file.isDirectory() && !file.getName().startsWith(".");
		}

		@Override
		public boolean accept(File dir, String name) {
			count++;
			if(count % 10 == 0)
				indetProgress.progress();
			
			return accept(dir);
		}
		
	}
}
