package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Tristan Glaes, Tom Scholer, Peter Hennen
 * @version 09.03.2018
 */
public final class Database {

	private static Connection connection;

	private Database() {
	}

	/**
	 * Baut eine Verbindung zur Datenbank auf, wenn noch keine besteht. Benutzt die
	 * statische Variable connection.
	 */
	private static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:database.db";
			if (connection == null) {
				connection = DriverManager.getConnection(url);
			} else if (connection.isClosed()) {
				connection = DriverManager.getConnection(url);
			}

		} catch (Exception e) {
			e.printStackTrace();
			connection = null;
		}
	}

	/**
	 * Faehrt das SQL Statement aus und gibt true bei erfolg zurueck, sonst false.
	 * 
	 * @param sqlCommand
	 *            Das SQL Statement
	 * @return true, wenn erfolgreich, false sonst
	 * @throws SQLException
	 */
	public static boolean executeQuery(String sqlCommand) throws SQLException {
		// System.out.println(sqlCommand);
		connect();

		try {
			Statement stmt = connection.createStatement();
			stmt.execute(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			connection.close();
		}
		return true;
	}
	
	/**
	 * Wie executeQuery schliesst die Verbindung zur Datenbank aber nicht.
	 * @param sqlCommand
	 * @return true, wenn erfolgreich, false sonst
	 * @throws SQLException
	 */
	public static boolean executeQueryWithOutClose(String sqlCommand)throws SQLException {
		connect();

		try {
			Statement stmt = connection.createStatement();
			stmt.execute(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	/**
	 * Faehrt das SQL Statement aus und gibt das Ergebnis zurueck
	 * 
	 * @param sqlCommand
	 *            Das SQL Statement
	 * @return Das ResultSet mit den Ergebnissen.
	 * @throws SQLException
	 */
	public static ResultSet executeSql(String sqlCommand) throws SQLException {

		// System.out.println(sqlCommand);
		connect();
		ResultSet rs = null;

		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			connection.close();
		}

		return rs;
	}

	/**
	 * 
	 * @return Die letzte eingefuegte ID der Datenbank.
	 * @throws SQLException
	 */
	public static int getlastID() throws SQLException {
		connect();

		String sql = "SELECT last_insert_rowid()";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		if (rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}

	/**
	 * Schliesst die Datenbankverbindung.
	 * 
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException {
		if (!(connection == null)) {
			if (!connection.isClosed()) {
				connection.close();
			}
		}
	}
}
