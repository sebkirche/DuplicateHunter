
package fr.seki.duphunter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastien
 */
public class IndexModel extends Observable {
	
	private File dbFile;
	Connection c = null;

	public IndexModel() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void setDBFile(File f){
		if(f.exists()){
			dbFile = f;
			try {
				c = DriverManager.getConnection("jdbc:sqlite:" + f.getCanonicalPath().replace("\\", "/"));
				c.setAutoCommit(false);
			} catch (SQLException ex) {
				Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
			}
			setChanged();
			notifyObservers();
		}
	}
	
	public File getDbFile(){
		return dbFile;
	}
	
	public ResultSet queryIndex(String sql){
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			//stmt.close(); //TODO : crash with "java.sql.SQLException: SQLite JDBC: inconsistent internal state"
			return rs;
		} catch (SQLException ex) {
			Logger.getLogger(IndexModel.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null; ///!!!
	}
}
