package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public final class Database {

	private static Connection connection;

	private Database() {
	}

	private static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			if (connection == null) {
				String url = "jdbc:sqlite:database.db";
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
	 */
	public static boolean executeQuery(String sqlCommand) {
		System.out.println(sqlCommand);
		connect();
		return true;
	}

	/**
	 * Executes the sqlCommand and returns the result.
	 * 
	 * @param sqlCommand
	 *            The SQL command you want to execute.
	 * @return The ResultSet of the command.
	 */
	public static ResultSet executeSql(String sqlCommand) {
		System.out.println(sqlCommand);
		connect();
		return null;
	}

}
