package fr.seki.duphunter;

import java.util.List;
import java.sql.*;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author Sebastien
 */
public class SqliteDbDumper implements IndexDumper {

	Connection cnx = null;
	String db;

	@Override
	public void dump(List<IndexNode> index, String output) {
		db = output;
		connect();
		createStruct();
		if (index.size() > 0) {
			purgeRepo(index.get(0).getRepoRoot());
		}
		dumpdata(index);
		disconnect();
	}

	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
//			new org.sqlite.JDBC();

			org.sqlite.SQLiteConfig cfg = new SQLiteConfig();
			cfg.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
//			cfg.setJournalMode(SQLiteConfig.JournalMode.PERSIST);
			cnx = DriverManager.getConnection("jdbc:sqlite:" + db.replace("\\", "/"), cfg.toProperties());
			cnx.setAutoCommit(false);

		} catch (ClassNotFoundException ex) {
//			Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while instanciating SQLite driver: " + ex.getMessage());
			System.exit(1);
		} catch (SQLException ex) {
			//Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while connecting SQLite database " + db + " : " + ex.getMessage());
			System.exit(1);
		}
	}

	private void disconnect() {
		try {
			cnx.close();
		} catch (SQLException ex) {
			//Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while closing the SQLite connection: " + ex.getMessage());
			System.exit(1);
		}
	}

	private void createStruct() {
		try {
			Statement stmt;
			String sql;

			stmt = cnx.createStatement();
			sql = "create table if not exists FileIndex "
					+ "(path   text primary key not null,"
					+ " repo   text             not null,"
					+ " name   text             not null,"
					+ " hash   text             not null,"
					+ " lastup datetime         not null,"
					+ " author text,"
					+ " size   int              not null);";
			stmt.executeUpdate(sql);
			sql = "create index if not exists repo_idx on FileIndex (repo)";
			stmt.executeUpdate(sql);
			//TODO: table for schema version
			stmt.close();

		} catch (SQLException ex) {
			//Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while creating DB (" + db + ") structure: " + ex.getMessage());
			System.exit(1);
		}
	}

	private void dumpdata(List<IndexNode> index) {
		String path = "";
		try {
			PreparedStatement stmt;
			stmt = cnx.prepareStatement("insert or replace into FileIndex (path, repo, name, hash, lastup, author, size) values (?, ?, ?, ?, ?, ?, ?);");
			DefinedConsoleProgressor bar = new DefinedConsoleProgressor(index.size());
			long c=0;
			System.out.println("Saving into " + db + "... ");
			for (IndexNode node : index) {
				c++;
				if(c % 20 == 0)
					bar.updateProgress(c);
				path = node.getCanonicalPath();
				stmt.setString(1, path);
				stmt.setString(2, node.getRepoRoot());
				stmt.setString(3, node.getName());
				stmt.setString(4, node.getChecksum());
				stmt.setDate(5, new Date(node.getDate().getTime()));
				stmt.setString(6, node.getAuthor());
				stmt.setLong(7, node.getSize());
				stmt.executeUpdate();
			}
			bar.updateProgress(c);
			System.out.println(" Done.");
			stmt.close();
			cnx.commit();
		} catch (SQLException ex) {
			//Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while inserting into SQLite db (" + db + ") for path " + path + ": " + ex.getMessage());
			System.exit(1);
		}
	}

	private void purgeRepo(String repoRoot) {
		try {
			Statement stmt;
			String sql = "delete from FileIndex where repo = '" + repoRoot + "';";
			stmt = cnx.createStatement();
			stmt.execute(sql);
		} catch (SQLException ex) {
			//Logger.getLogger(SqliteDbDumper.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Error while purging repo " + repoRoot + " from db " + db + ": " + ex.getMessage());
			System.exit(1);
		}

	}
}
