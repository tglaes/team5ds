package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class is used to access the database.
 * 
 * @author Tristan Glaes
 *
 */
public final class Database {

	private static Connection connection;

	private Database() {
	}

	/**
	 * Connects to the database when no connection was estabilshed yet.
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
	 * Executes the sqlCommand and only return true or false.
	 * 
	 * @param sqlCommand
	 *            The SQL command you want to execute.
	 * @return true when the execution was successful, false otherwise.
	 * @throws SQLException
	 */
	public static boolean executeQuery(String sqlCommand) throws SQLException {
		System.out.println(sqlCommand);
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
	 * Executes the sqlCommand and returns the result.
	 * 
	 * @param sqlCommand
	 *            The SQL command you want to execute.
	 * @return The ResultSet of the command.
	 * @throws SQLException
	 */
	public static ResultSet executeSql(String sqlCommand) throws SQLException {
		
		System.out.println(sqlCommand);
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
}
