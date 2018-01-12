package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import database.Database;

public final class Permissions {

	// Put a default value for a user and localhost. So no login is required.
	private static HashMap<String, Integer> sessionMap = new HashMap<String, Integer>() {
		{
			put("127.0.0.1", 1);
		}
	};

	private Permissions() {
	}

	/**
	 * 
	 * @param ip
	 *            The ip adress of the client.
	 * @return The userID if a session is present, null otherwise.
	 */
	public static Integer hasSession(String ip) {
		return sessionMap.get(ip);
	}

	/**
	 * 
	 * @param userID
	 *            The ID of the user that wants to access the board.
	 * @param boardID
	 *            The ID of the board that the user wants to access.
	 * @return The enum type that represents his access rights to this board.
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
