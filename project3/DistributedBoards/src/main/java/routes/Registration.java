package routes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import database.Database;
import htmlBuilder.HTMLBuilder;
import util.Permission;
import util.Permissions;

/**
 * 
 * @author Tristan Glaes, Meris Krupic, Iurie Golovencic, Vadim Khablov
 * @version 09.03.2018
 */
@Path("/Registration")
public class Registration {

	/**
	 * 
	 * @return Die Registrieren Seite.
	 * @throws FileNotFoundException
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public InputStream sendRegistrationPage() throws FileNotFoundException {
		return Resources.getResource("Registrieren.html", "html");
	}

	/**
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param username
	 * @param profession
	 * @param age
	 * @param request
	 * @return Die Seite mit dem Zentralen Board bei Erfolg, FailedRegistration
	 *         sonst.
	 * @throws SQLException
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public InputStream getLoginData(@FormParam("firstname") String firstname, @FormParam("lastname") String lastname,
			@FormParam("email") String email, @FormParam("password") String password,
			@FormParam("username") String username, @FormParam("profession") String profession,
			@FormParam("age") String age, @Context HttpServletRequest request) throws SQLException, IOException {

		// ErrorCode 0->kein Fehler, 1->Benutzername falsch, 2->Email falsch, 3->Email
		// und Username falsch.
		int errorCode = 0;

		if (!checkUserName(username)) {
			errorCode = 1;
		}

		if (!checkEmail(email)) {

			if (errorCode == 1) {
				errorCode = 3;
			} else {
				errorCode = 2;
			}
		}

		// Benutzer in Datenbank eintragen.
		if (errorCode == 0) {
			String sqlCommand = "INSERT INTO Users (Password,Email,Username,Vorname,Nachname,[Alter],Beruf)"
					+ " VALUES('" + password + "', '" + email + "','" + username + "','" + firstname + "', '" + lastname
					+ "'" + "," + age + ", '" + profession + "')";
			Database.executeQuery(sqlCommand);

			// Neue ID des Benutzers �ber die Email.
			sqlCommand = "SELECT id FROM Users WHERE EMail='" + email + "'";
			ResultSet rs = Database.executeSql(sqlCommand);

			if (rs.next()) {
				int id = rs.getInt(1);

				// Eintrag in SessionMap
				Permissions.createSession(request.getRemoteAddr(), id);
				// Eintrag f�r die zentrale Anzeigetafel.
				sqlCommand = "INSERT INTO UserBoards (User,Board) VALUES(" + id + ",0)";
				Database.executeQuery(sqlCommand);
				rs.close();

				// Willkommens-Post auf die Zentrale Anzeige
				sqlCommand = "Insert INTO Posts (Content,Date,User,Post) VALUES('" + firstname + ", " + lastname
						+ " just joined!',DATETIME('now')," + id + ",0)";
				Database.executeQuery(sqlCommand);

				// ID des Posts aus der Datenbank auslesen.
				sqlCommand = "SELECT ID FROM Posts ORDER BY ID DESC LIMIT 1";
				rs = Database.executeSql(sqlCommand);
				rs.next();
				int postID = rs.getInt(1);
				// Post in die zentrale Anzeigetafel schreiben.
				sqlCommand = "INSERT INTO BoardPosts (Board,Post) VALUES(0," + postID + ")";
				Database.executeQuery(sqlCommand);

				rs.close();
				Database.closeConnection();
				// Senden der BoardsPage.
				return HTMLBuilder.buildBoardsPage(id, 0, Permission.User);
			}
		}
		return HTMLBuilder.buildFailedRegistration(errorCode);
	}

	/**
	 * 
	 * @param u
	 *            Benutzername
	 * @return True falls der benutzername noch nicht vergeben ist, false sonst.
	 * @throws SQLException
	 */
	private boolean checkUserName(String u) throws SQLException {

		String sqlCommand = "SELECT Username FROM Users";
		ResultSet rs = Database.executeSql(sqlCommand);

		while (rs.next()) {

			if (u.equals(rs.getString(1))) {
				rs.close();
				Database.closeConnection();
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param e
	 *            Email
	 * @return True, wenn die Email noch nicht vergeben ist, false sonst.
	 * @throws SQLException
	 */
	private boolean checkEmail(String e) throws SQLException {
		String sqlCommand = "SELECT EMail FROM Users";
		ResultSet rs = Database.executeSql(sqlCommand);

		while (rs.next()) {

			if (e.equals(rs.getString(1))) {
				rs.close();
				Database.closeConnection();
				return false;
			}
		}
		return true;
	}
}