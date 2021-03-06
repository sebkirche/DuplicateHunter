package fr.seki.duphunter;

import fr.seki.dbupdater.DbUpdater;
import fr.seki.duphunter.gui.OptionsPanel;
import fr.seki.duphunter.gui.SourceData;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Stores the data model for file index
 *
 * @author Sebastien
 */
public class IndexModel extends Observable {

	public static final String configFile = "duplicate.properties";

	private File dbFile;
	private Connection c = null;
	private PropertiesConfiguration cfg;
	private static final String dbExt = "db";

	public IndexModel() {

		loadConfig();

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * get the settings from config file
	 */
	private void loadConfig() {
		try {
			cfg = new PropertiesConfiguration(configFile);
		} catch (ConfigurationException ex) {
			if (new File(configFile).exists()) {
				Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
			} else {
				createDefaultConfig();
			}
		}
	}

	/**
	 * Open the connection to a SQLite database
	 *
	 * @param dbfile the file database
	 */
	private void connectToDBFile(File dbfile, boolean silentUpgrade) {
		try {
			c = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getCanonicalPath().replace("\\", "/"));
			c.setAutoCommit(false);
		} catch (SQLException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		}

		DbUpdater dbu = new DbUpdater(c);
		if (!dbu.checkAndUpgrade(silentUpgrade)) {
			return;
		}

		dbFile = dbfile;
		setChanged();
		notifyObservers();
	}

	/**
	 * Close the current SQLite connection
	 */
	public void disconnect() {
		if (c == null) {
			return;
		}
		try {
			if (!c.isClosed()) {
				c.close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * getter for the database file
	 *
	 * @return
	 */
	public File getDbFile() {
		return dbFile;
	}

        /**
         * Execute an SQL query on the connection
         * @param sql the query
         * @return most often a {@link ResultSet} but it could be the text of an error message
         */
	public Object executeQuery(String sql) {
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			//stmt.close(); //TODO : crash with "java.sql.SQLException: SQLite JDBC: inconsistent internal state"
			//reason : cf close() javadoc : When a Statement object is closed, its current ResultSet object, if one exists, is also closed.
			return rs;
		} catch (SQLException ex) {
			//Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
                        return ex.getMessage();
		}
	}

	/**
	 * getter for the PropertiesConfiguration NOTE : this could be abstracted ...
	 *
	 * @return
	 */
	public PropertiesConfiguration getConfig() {
		return cfg;
	}

	/**
	 * Create a new configuration with default values
	 */
	private void createDefaultConfig() {
		cfg = new PropertiesConfiguration();
		cfg.setPath(configFile);
		cfg.addProperty("ignoreEmpty", true);
		cfg.addProperty("repo", Arrays.asList(new String[]{"svn://subversion.conceptware.org/TestData", "c:\\temp\\edifact"}));
	}

	/**
	 * Save the configuration to disk. The file name is stored in {@link #configFile}
	 */
	public void saveConfig() {
		try {
			cfg.setHeader("Duplicate Hunter configuration file");
			cfg.save();
		} catch (ConfigurationException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Return the extension used for index databases
	 *
	 * @return the extension (without the '.')
	 */
	public String getDbExtension() {
		return dbExt;
	}

	/**
	 * Initialize a new database : create the file and fill some objects in the structure
	 *
	 * @param f
	 */
	void initDBFile(File f) {
		connectToDBFile(f, true);
		//SqliteDbDumper.createStruct(c);
	}

	void connectDBFile(File f) {
		connectToDBFile(f, false);
	}

	/**
	 * Reconnect to the last used database file
	 */
	void reconnect() {
		connectToDBFile(dbFile, false);
	}

	public ArrayList<SourceData> getSources() {
		final String qry = "select path, active from sources;";
		final ArrayList<SourceData> list = new ArrayList<SourceData>();

		Thread runner = new Thread() {
			@Override
			public void run() {
				try {
					Statement stmt = c.createStatement();
					ResultSet rs = stmt.executeQuery(qry);

					while (rs.next()) {
						String src = rs.getString(1);
						boolean active = rs.getBoolean(2);
						list.add(new SourceData(src, active));
					}
					rs.close();
					stmt.close();

				} catch (SQLException ex) {
					Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		};
		runner.run();
		return list;
	}

	public void setSources(final ArrayList<SourceData> l) {
		final String del = "delete from Sources;"; //truncate
		final String ins = "insert or replace into Sources (path, active) values (?, ?);";
		Thread runner = new Thread() {
			public void run() {
				try {
					Statement dstmt = c.createStatement();
					dstmt.executeUpdate(del);
					dstmt.close();
					
					PreparedStatement istmt = c.prepareStatement(ins);
					for(int i = 0; i < l.size(); i++){
						SourceData src = l.get(i);
						istmt.setString(1, src.path);
						istmt.setBoolean(2, src.active);
						istmt.addBatch();
					}
					istmt.executeBatch();
					istmt.close();
					c.commit();
					
				} catch (SQLException ex) {
					Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		};
		runner.run();
	}
}
