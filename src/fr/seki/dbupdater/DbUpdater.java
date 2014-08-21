package fr.seki.dbupdater;

import fr.seki.duphunter.SqliteDbDumper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.callback.ConfirmationCallback;
import javax.swing.JOptionPane;

/**
 * Database schema migrator
 * @author Sebastien
 */
public class DbUpdater {

	Connection cnx;

	public DbUpdater(Connection c) {
		this.cnx = c;
	}

	/**
	 * Get the database schema version
	 *
	 * @return the last applied migration script (can be 0 if none) else -1 if versioning structure does not exist
	 *
	 */
	private int getActualVersion() {
		int v = -1;
		try {
			Statement stmt;
			ResultSet rs;
			String sql;

			sql = "select max(id) from Version;";
			stmt = cnx.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			v = rs.getInt(1);
		} catch (SQLException ex) {
			if (!ex.getMessage().contains("no such table: Version")) {
				Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return v;
	}

	/**
	 * Get the last available migration script
	 *
	 * @return
	 */
	private int getLastVersion() {
		FileInputStream fis = null;
		int lastVer = 0;
		try {
			File f = new File(DbUpdater.class.getResource("/db_updates/version.properties").toURI());
			fis = new FileInputStream(f);
			Properties props = new Properties();
			props.load(fis);
			lastVer = Integer.valueOf(props.getProperty("version"));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return lastVer;
	}

	/**
	 * Perform sanity tests on the database, add the versioning structure then execute upgrade scripts
	 *
	 * @return
	 */
	public boolean checkAndUpgrade(boolean silentUpgrade) {
		int lastAvail, current, i;

		lastAvail = getLastVersion();
		current = getActualVersion();

		/* check if at least we have the versioning structure */
		if (lastAvail > 0 && current < lastAvail) {

			String msg;
			int answer;
			if (current ==-1){
				msg = "This database schema needs to be updated.";
			} else {
				msg = String.format("This database schema is in version %d and needs to be updated to version %d.", current, lastAvail);
			}
			msg += "\nDo you want to perform the upgrade?";
			
			if(silentUpgrade)
				answer = 0;
			else
				answer = JOptionPane.showConfirmDialog(null, msg, "Database upgrade", ConfirmationCallback.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (answer == 1) {
				return false;
			}

			if (current == -1) {
				createVersioningStruct();
			}

			/* check if the ugrade is necessary */
			if (lastAvail > 0 && current < lastAvail) {
				for (i = 1; i <= lastAvail; i++) {
					try {
						if (!applyUpgradeStep(i)) {
							System.err.println("Step " + i + " failed.");
						}
					} catch (SQLException ex) {
						//Logger.getLogger(DbMigrator.class.getName()).log(Level.SEVERE, null, ex);
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Apply the migration script for given step
	 *
	 * @param step the migration script to execute
	 * @return true if OK or false if it failed
	 */
	private boolean applyUpgradeStep(int step) throws SQLException {

		try {
			Reader r = null;
			//PrintWriter logw = new PrintWriter(new File("upgrade.log"));
			PrintWriter errw = new PrintWriter(new File("upgrade_errors.log"));
			
			ScriptRunner sr = new ScriptRunner(cnx, false, true);
			sr.setLogWriter(null);
			sr.setErrorLogWriter(errw);
			
			File f = new File(DbUpdater.class.getResource("/db_updates/" + step + ".sql").toURI());
			r = new FileReader(f);

			sr.runScript(r);
			r.close();

			Statement stmt = cnx.createStatement();
			stmt.execute("insert or replace into Version (id) values (" + step + ");");
			cnx.commit();
			stmt.close();
			errw.close();
			
			return true;

		} catch (IOException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
			throw ex;
		} catch (URISyntaxException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	/**
	 * Create the database structure for versioning : the Version table
	 */
	private void createVersioningStruct() {
		try {
			Statement stmt;
			String sql;

			stmt = cnx.createStatement();
			sql = "create table if not exists Version ("
					+ " id int primary key not null,"
					+ " runat datetime default current_timestamp"
					+ ");";
			stmt.executeUpdate(sql);
			stmt.close();
			cnx.commit();
		} catch (SQLException ex) {
			Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while creating DB (" + cnx.toString() + ") structure: " + ex.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		Connection c;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			DbUpdater dbm = new DbUpdater(c);
			System.out.println("Last available version: " + dbm.getLastVersion());
			System.out.println("Last DB version: " + dbm.getActualVersion());
			dbm.checkAndUpgrade(true);

			c.close();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(DbUpdater.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
