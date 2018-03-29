package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import database.Database;

/**
 * 
 * @author Tristan Glaes, Meris Krupic, Vadim Khablov, Iurie Golovencic
 * @version 09.03.2018
 */
public final class Permissions {

	// Hashmap, die IP,ID Paare beinhaltet (Eingeloggten Benutzern)
	private static HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();

	private Permissions() {
	}

	/**
	 * Erstellt eine Session fuer einen Benutzer.
	 * 
	 * @param ip
	 *            Die IP des Benutzers.
	 * @param ID
	 *            DIe userID des Benutzers.
	 */
	public static void createSession(String ip, Integer ID) {
		sessionMap.put(ip, ID);
	}

	/**
	 * Loest eine Session auf.
	 * 
	 * @param ip
	 *            Die IP des Benutzers.
	 */
	public static void destroySession(String ip) {
		sessionMap.remove(ip);
	}

	/**
	 * 
	 * @param ip
	 *            Die IP des Benutzers
	 * @return Die userID des Benutzers
	 */
	public static Integer hasSession(String ip) {
		return sessionMap.get(ip);
	}

	/**
	 * 
	 * @param userID
	 *            Die ID des Benutzers.
	 * @param boardID
	 *            Die boardID Boards.
	 * @return Die Berechtigung, die der Benutzer auf dem Board hat.
	 */
	public static Permission isAuthorized(Integer userID, Integer boardID) {
		try {
			String sqlCommand = "";
			ResultSet rs = null;

			// ID is null, so the user has no current session.
			if (userID == null) {
				return Permission.None;
			} else {
				// Check if the user is an admin.
				sqlCommand = "SELECT * FROM Boards WHERE ID=" + boardID + " AND Admin=" + userID;
				rs = Database.executeSql(sqlCommand);

				if (rs.next()) {
					return Permission.Admin;
				} else {

					// Check if the user is a normal user.
					sqlCommand = "SELECT * FROM UserBoards WHERE Board=" + boardID + " AND User=" + userID;
					rs = Database.executeSql(sqlCommand);
					if (rs.next()) {
						return Permission.User;
					} else {
						// No entry in the database, so the user is not permitted.
						return Permission.None;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Permission.None;
		}
	}
}