package fr.seki.duphunter;

import fr.seki.duphunter.gui.MainFrame;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;

/**
 * Master class for SVNHelper, aimed to be the command line interface entry point
 *
 * @author Sebastien
 */
public class Main {

	public static final String APP_NAME = "DuplicateHunter";
	public static final String APP_COPY = "Sébastien Kirche © 2014-2015";
	public static final String APP_VERSION = "0.3.1";
	public static final String VERSION_STR = "v." + APP_VERSION;	
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		new Main(args);
	}

	public Main(String[] args) {
		System.out.println(APP_NAME + " " + VERSION_STR + " - " + APP_COPY);
		handleCLI(args);
	}

	/**
	 * Surprisingly shows the expected parameters from command line
	 *
	 * @param supportedOpts the sat of supported options as defined in {@link #handleCLI(java.lang.String[]) handleCLi()} method
	 * @param message
	 */
	private void printUsage(Options supportedOpts, String message) {
		if (message != null) {
			System.err.println(message);
		}

		HelpFormatter help = new HelpFormatter();
		help.printHelp("java -jar " + APP_NAME + ".jar <options>", "options:", supportedOpts, null);
	}

	/**
	 * Parse the command line parameters and launch appropriate actions
	 *
	 * @param args parameters from the command line
	 */
	private void handleCLI(String[] args) {
		CommandLineParser cliParser = new BasicParser();

		Options opts = new Options();
		opts.addOption("h", "help", false, "show command line usage");
		opts.addOption("s", "svnrepo", true, "URL of the repository (svn://, http:// or file://)");
		opts.addOption("i", "index", false, "produce an index");
		opts.addOption("o", null, true, "name of the file to save index");
		opts.addOption("d", "db", true, "name of the database to save index");
		opts.addOption("f", "folder", true, "path to a directory to index");
		opts.addOption(null, "gui", false, "Use experimental graphical user interface");

		try {
			CommandLine cli = cliParser.parse(opts, args);

			if (args.length == 0 || cli.hasOption("h")) {
				printUsage(opts, null);
				System.exit(0);
			}

			if (cli.hasOption("gui")) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						MainFrame mf = new MainFrame();
						//mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						mf.setVisible(true);
					}
				});
				
			} else {
				if (cli.hasOption("i")) {
					Indexer indexer;
					if (cli.hasOption("s")) {
						setupSvnLib();
						String repoUrl = cli.getOptionValue("r");
						indexer = new SVNIndexer(repoUrl);
						if (cli.hasOption("o")) {
							indexer.setOutput(OutputKind.FILE, cli.getOptionValue("o"));
						} else if (cli.hasOption("d")) {
							indexer.setOutput(OutputKind.DB, cli.getOptionValue("d"));
						}
						indexer.process();
					} else if (cli.hasOption("f")) {
//					if(cli.hasOption(repoUrl))
						String path = cli.getOptionValue("f");
						indexer = new FSIndexer(path);
						if (cli.hasOption("o")) {
							indexer.setOutput(OutputKind.FILE, cli.getOptionValue("o"));
						} else if (cli.hasOption("d")) {
							indexer.setOutput(OutputKind.DB, cli.getOptionValue("d"));
						}
						indexer.process();
					}

				}
			}


		} catch (ParseException ex) {
//			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//			System.err.println(ex.getMessage());
			printUsage(opts, ex.getMessage());
		}
	}

	/**
	 * initialise the different supported schemes
	 */
	private static void setupSvnLib() {
		//support for http/https
		DAVRepositoryFactory.setup();

		//support for svn/svn+xxx
		SVNRepositoryFactoryImpl.setup();

		//support for file://
		FSRepositoryFactory.setup();
	}
}
