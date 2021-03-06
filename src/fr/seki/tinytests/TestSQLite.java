package fr.seki.tinytests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Sebastien
 */
public class TestSQLite {
	
	public static void main(String args[]) throws SQLException, ClassNotFoundException {
		Connection c = null;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:test.db");
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");

		Statement stmt = c.createStatement();
		
		String sql = "drop table if exists company";
		stmt.executeUpdate(sql);
		
		sql = "CREATE TABLE COMPANY "
				+ "(ID INT PRIMARY KEY     NOT NULL,"
				+ " NAME           TEXT    NOT NULL, "
				+ " AGE            INT     NOT NULL, "
				+ " ADDRESS        CHAR(50), "
				+ " SALARY         REAL,"
				+ " entry datetime)";
		stmt.executeUpdate(sql);

		System.out.println("Table created successfully");
		sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
				+ "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
		stmt.executeUpdate(sql);

		sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
				+ "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
		stmt.executeUpdate(sql);

		sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
				+ "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
		stmt.executeUpdate(sql);

		sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
				+ "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
		stmt.executeUpdate(sql);
		
		PreparedStatement pstmt = c.prepareStatement("insert into company (id,name,age,address,salary, entry) values (?,?,?,?,?,datetime(?/1000,'unixepoch'))");
		pstmt.setInt(1, 5);
		pstmt.setString(2, "Seb");
		pstmt.setInt(3, 42);
		pstmt.setString(4,"rue du parc");
		pstmt.setFloat(5, 123.45f);
		pstmt.setDate(6, new Date(new java.util.Date().getTime()));
		pstmt.executeUpdate();

		ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int age = rs.getInt("age");
			String address = rs.getString("address");
			float salary = rs.getFloat("salary");
			System.out.println("ID = " + id);
			System.out.println("NAME = " + name);
			System.out.println("AGE = " + age);
			System.out.println("ADDRESS = " + address);
			System.out.println("SALARY = " + salary);
			System.out.println();
		}


		stmt.close();
		c.commit();
		c.close();
	}
}
