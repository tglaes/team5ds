package routes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import database.Database;
import htmlBuilder.HTMLBuilder;
import util.Permissions;
import util.Permission;

/**
 * 
 * @author Tristan Glaes
 * @version 09.03.2018
 */
@Path("/Boards")
public class Boards {

	/***
	 * 
	 * @param boardID
	 *            Die ID des angefragten Boards.
	 * @param request
	 *            HTTP Request
	 * @return Die Login Seite, wenn der Benutzer nicht eingeloggt ist, sonst die
	 *         Boardseite mit id boardID.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public static InputStream sendBoardsPage(@DefaultValue("0") @QueryParam("board") int boardID,
			@Context HttpServletRequest request) throws IOException, SQLException {
		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {
			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		}
	}

	/***
	 * 
	 * @param p
	 *            Die Berechtigung, die der Benutzer auf dem Board hat.
	 * @param userID
	 *            Die ID des Benutzers.
	 * @param boardID
	 *            Die ID des Boards.
	 * @return Die Board Seite für Admin oder User, wenn der Benutzer eingeloggt
	 *         ist, sonst 403.
	 * @throws IOException
	 * @throws SQLException
	 */
	private static InputStream createPage(Permission p, int userID, int boardID) throws IOException, SQLException {

		InputStream ret = null;
		switch (p) {
		case Admin:
			ret = HTMLBuilder.buildBoardsPage(userID, boardID, p);
			break;
		case User:
			ret = HTMLBuilder.buildBoardsPage(userID, boardID, p);
			break;
		case None:
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			ret = new ByteArrayInputStream(pageBytes);
			break;
		}

		return ret;
	}

	/***
	 * 
	 * @param boardName
	 *            Der Name des neuen Boards
	 * @param request
	 *            Der HTTP request
	 * @return Die Seite des neu erstellten Boards.
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Path("/newBoard")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream createNewBoard(@FormParam("boardName") String boardName,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {

			// Create the new board and send the Boards page showing the new board.
			int boardID = 0;

			// Board in DB einfügen.
			String sqlCommand = "INSERT INTO Boards (Admin,Name) VALUES('" + userID + "', '" + boardName + "')";
			Database.executeQuery(sqlCommand);

			// Letzte ID ausgeben
			sqlCommand = "SELECT ID FROM Boards ORDER BY ID DESC LIMIT 1";
			ResultSet rs = Database.executeSql(sqlCommand);

			if (rs.next()) {
				boardID = rs.getInt(1);
				rs.close();
				Database.closeConnection();
			}
			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		}
	}

	/***
	 * 
	 * @param boardID
	 *            Die Board Id des Boards, dass zu löschen ist.
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Zentrale Anzeigetafeln.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/deleteBoard")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deleteBoard(@QueryParam("board") int boardID, @Context HttpServletRequest request)
			throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {

			// Löschen des Boards.
			String sqlCommand = "DELETE FROM Boards WHERE ID=" + boardID;
			Database.executeQuery(sqlCommand);
			// User Board Abhängigkeiten löschen.
			sqlCommand = "DELETE FROM UserBoards WHERE board=" + boardID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();

			return createPage(Permissions.isAuthorized(userID, 0), userID, 0);
		} else {
			// Keine Berechtigung
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}

	/***
	 * 
	 * @param boardID
	 *            Die Board ID, von dem Board, von dem Der Benutzer entfernt werden
	 *            soll.
	 * @param removeUserID
	 *            Die ID des Benutzers der entfernt werden soll.
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/removeUser")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream removeUser(@QueryParam("board") int boardID, @QueryParam("user") int removeUserID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {

			// Lösche den Benutzer
			String sql = "DELETE FROM UserBoards WHERE User=" + removeUserID + " AND Board=" + boardID;
			Database.executeQuery(sql);
			Database.closeConnection();

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}

	/**
	 * 
	 * @param boardID
	 *            Die ID des Boards.
	 * @param postID
	 *            Die ID des Posts.
	 * @param request
	 *            Der Http Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/markPost")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream markPost(@QueryParam("board") int boardID, @QueryParam("post") int postID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin
				|| Permissions.isAuthorized(userID, boardID) == Permission.User) {

			// Erstelle Markierung für Post
			String sqlCommand = "INSERT INTO PostMarks (User,Post) VALUES(" + userID + "," + postID + ")";
			Database.executeQuery(sqlCommand);

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}

	}

	/**
	 * 
	 * @param push
	 *            1 wenn der Post auf die Zentrale Anzeigetafel soll, 0 für
	 *            ignorieren.
	 * @param boardID
	 *            Die BoardID
	 * @param postID
	 *            Die PostID
	 * @param request
	 *            Der HTTP Request.
	 * @return Das zentrale Board, wenn der Post gepusht wird, sonst wieder die
	 *         Boardseite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/pushPost")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream markPost(@QueryParam("push") int push, @QueryParam("board") int boardID,
			@QueryParam("post") int postID, @Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {

			// Post soll auf das Zentrale Board (PUSH)
			if (push == 1) {

				String sqlCommand = "INSERT INTO BoardPosts (Board,Post) VALUES(0," + postID + ")";
				Database.executeQuery(sqlCommand);
				// Gehe auf das Zentrale Board.
				return createPage(Permissions.isAuthorized(userID, 0), userID, 0);

				// Markierungen werden gelöscht/ignoriert (DISMISS)
			} else if (push == 0) {

				// Lösche alle Markierungen des Posts.
				String sqlCommand = "DELETE FROM PostMarks WHERE post=" + postID;
				Database.executeQuery(sqlCommand);
				return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
				// push Parameter ist falsch.
			} else {
				byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/404.html"));
				return new ByteArrayInputStream(pageBytes);
			}

		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}

	}

	/**
	 * 
	 * @param boardID
	 *            Die BoardID
	 * @param postID
	 *            Die PostID
	 * @param commentText
	 *            Der Inhalt des Kommentars
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Board Seite
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Path("/newComment")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream newComment(@QueryParam("board") int boardID, @QueryParam("post") int postID,
			@FormParam("commentText") String commentText, @Context HttpServletRequest request)
			throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin
				|| Permissions.isAuthorized(userID, boardID) == Permission.User) {

			// Begrenze den Inhalt des Kommentars auf 500 Zeichen
			if (commentText.length() > 250) {
				commentText = commentText.substring(0, 250);
			}

			// Erstelle das Kommentar
			String sqlCommand = "INSERT INTO Posts (Content, Date, Post, User) VALUES('" + commentText
					+ "', DATETIME('now')," + postID + " ," + userID + ")";
			Database.executeQuery(sqlCommand);

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}

	/**
	 * 
	 * @param boardID
	 *            Die BoardID.
	 * @param postText
	 *            Inhalt des Posts
	 * @param request
	 *            HTTP Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Path("/newPost")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream newPost(@QueryParam("board") int boardID, @FormParam("postText") String postText,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (boardID == 0) {

			// Niemand darf direkte Posts auf das zentrale Board posten.
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);

		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin
				|| Permissions.isAuthorized(userID, boardID) == Permission.User) {

			// Begrenze den Inhalt des Posts auf 500 Zeichen
			if (postText.length() > 500) {
				postText = postText.substring(0, 500);
			}

			// Post in die Datenbank eintragen.
			String sqlCommand = "INSERT INTO Posts (Content,Date,Post,User) VALUES('" + postText
					+ "', DATETIME('now'),0," + userID + ")";
			Database.executeQuery(sqlCommand);

			// Letzte hinzugefügte ID aus der Datenbank auslesen.
			sqlCommand = "SELECT ID FROM Posts ORDER BY ID DESC LIMIT 1";
			ResultSet rs = Database.executeSql(sqlCommand);
			rs.next();
			int postID = rs.getInt(1);
			// Post dem Board zuordnen.
			sqlCommand = "INSERT INTO BoardPosts (Board,Post) VALUES(" + boardID + "," + postID + ")";
			Database.executeQuery(sqlCommand);
			rs.close();
			Database.closeConnection();

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		} else {
			// User ist nicht berechtigt.
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}

	/**
	 * 
	 * @param boardID
	 *            Die BoardID.
	 * @param postID
	 *            Die PostID.
	 * @param postText
	 *            Der neue Text des Posts.
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Path("/editPost")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream editPost(@QueryParam("board") int boardID, @QueryParam("post") int postID,
			@FormParam("postText") String postText, @Context HttpServletRequest request)
			throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin
				|| Permissions.isAuthorized(userID, boardID) == Permission.User) {

			// Begrenze den Inhalt des Posts auf 500 Zeichen
			if (postText.length() > 500) {
				postText = postText.substring(0, 500);
			}

			String sqlCommand = "UPDATE Posts SET content='" + postText + "' WHERE ID=" + postID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		} else {
			// User ist nicht berechtigt.
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}

	/***
	 * 
	 * @param boardID
	 *            Die BoardID.
	 * @param postID
	 *            Die ID des Posts, der gelöscht werden soll.
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/deletePost")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deletePost(@QueryParam("board") int boardID, @QueryParam("post") int postID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		// Den Besitzer des Posts suchen.
		String sqlCommand1 = "SELECT User FROM Posts WHERE ID=" + postID;
		ResultSet rs = Database.executeSql(sqlCommand1);
		rs.next();
		int postOwner = rs.getInt(1);
		Database.closeConnection();

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {
			if (Permissions.isAuthorized(userID, boardID) == Permission.Admin || userID == postOwner) {
				// Löschen der Zuordnung zum Board.
				String sqlCommand = "DELETE FROM BoardPosts WHERE Post=" + postID;
				Database.executeQuery(sqlCommand);
				// Löschen des Posts
				sqlCommand = "DELETE FROM Posts WHERE ID=" + postID;
				Database.executeQuery(sqlCommand);
				// Löschen der Kommentare
				sqlCommand = "DELETE FROM Posts WHERE Post=" + postID;
				Database.executeQuery(sqlCommand);
				Database.closeConnection();

				return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
			} else {
				// User ist nicht berechtigt.
				byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
				return new ByteArrayInputStream(pageBytes);
			}
		}
	}

	/**
	 * 
	 * @param userNameOrEmail
	 *            Die Email oder der Username des Benutzers.
	 * @param boardID
	 *            Die BoardID.
	 * @param request
	 *            Der Http Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Path("/addUser")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream addUserToBoard(@FormParam("userName") String userNameOrEmail,
			@QueryParam("board") int boardID, @Context HttpServletRequest request) throws IOException, SQLException {

		String sqlCommand;
		ResultSet rs;

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID).equals(Permission.Admin)) {

			// Prüfen ob Email oder Benutzername.
			if (userNameOrEmail.contains("@")) {
				// Email
				// Prüfen ob die Email einem Benutzer zugeordnet werden kann.
				sqlCommand = "SELECT ID FROM Users WHERE EMail='" + userNameOrEmail + "'";
				Database.executeQuery(sqlCommand);
				rs = Database.executeSql(sqlCommand);

			} else {
				// Benutzername
				// Prüfen ob der Benutzer existiert.
				sqlCommand = "SELECT ID FROM Users WHERE Username='" + userNameOrEmail + "'";
				Database.executeQuery(sqlCommand);
				rs = Database.executeSql(sqlCommand);
			}

			if (rs.next()) {

				String newUserID = rs.getString(1);
				// Prüfen ob Benutzer schon Mitglied des Boardes ist.
				sqlCommand = "SELECT * FROM UserBoards WHERE User=" + newUserID + " AND Board=" + boardID;
				Database.executeQuery(sqlCommand);
				rs = Database.executeSql(sqlCommand);
				// Benutzer ist noch nicht Mitglied.
				if (!rs.next()) {
					// Füge den Benutzer dem Board hinzu.
					sqlCommand = "INSERT INTO UserBoards (User,Board) VALUES(" + newUserID + "," + boardID + ")";
					Database.executeQuery(sqlCommand);
					Database.closeConnection();
				}
			}

			// Dem User das neue Board schicken.
			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		}
		byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
		return new ByteArrayInputStream(pageBytes);
	}

	/**
	 * 
	 * @param searchInput
	 *            Email oder Benutzername, nachdem gesucht werden soll.
	 * @param request
	 *            Der HTTP Request.
	 * @return Das Profil des Benutzers, wenn er existiert, sonst Benutzer nicht
	 *         gefunden Seite.
	 * @throws SQLException
	 * @throws IOException
	 */
	@POST
	@Path("/search")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream search(@FormParam("searchInput") String searchInput, @Context HttpServletRequest request)
			throws SQLException, IOException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {

			int profileID = 0;
			String sqlCommand;
			ResultSet rs;

			// Input ist Email
			if (searchInput.contains("@")) {

				sqlCommand = "SELECT ID FROM Users WHERE EMail='" + searchInput + "'";
				Database.executeQuery(sqlCommand);
				rs = Database.executeSql(sqlCommand);

				// Input ist Benutzername
			} else {
				sqlCommand = "SELECT ID FROM Users WHERE Username='" + searchInput + "'";
				Database.executeQuery(sqlCommand);
				rs = Database.executeSql(sqlCommand);
			}

			// Benutzer gefunden.
			if (rs.next()) {
				profileID = rs.getInt(1);
				Database.closeConnection();
				return HTMLBuilder.buildProfilePage(userID, profileID);

				// Benutzer nicht gefunden.
			} else {
				Database.closeConnection();
				byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/userNotFound.html"));
				return new ByteArrayInputStream(pageBytes);
			}
		}
	}
	
	/**
	 * 
	 * @param boardID Die Id des Board was verlassen werden soll.
	 * @param request Der HTTP Request
	 * @return Das Zentrale Board
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/leaveBoard")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream leaveBoard(@QueryParam("board") int boardID,@Context HttpServletRequest request) throws IOException, SQLException {
		
		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.User) {
			
			String sqlCommand = "DELETE FROM UserBoards WHERE User=" + userID + " AND Board=" + boardID;
			Database.executeQuery(sqlCommand);
			
			return createPage(Permissions.isAuthorized(userID, 0), userID, 0);
			
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/404.html"));
			return new ByteArrayInputStream(pageBytes);
		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}
	

	/**
	 * 
	 * @param commentID
	 *            Die ID des Kommentars, das gelöscht werden soll.
	 * @param boardID
	 *            Die ID des Boards.
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Board Seite.
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/deleteComment")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deleteComment(@QueryParam("comment") int commentID, @QueryParam("board") int boardID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {

			// Lösche Kommentar
			String sqlCommand = "DELETE FROM Posts WHERE ID=" + commentID;
			Database.executeQuery(sqlCommand);

			// Dem Benutzer wieder das Board schicken.
			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);

		} else if (Permissions.isAuthorized(userID, boardID) == Permission.User) {

			// Ist der Kommentar dem Benutzer
			String sqlCommand = "SELECT User FROM Posts WHERE ID=" + commentID;
			ResultSet rs = Database.executeSql(sqlCommand);
			rs.next();
			if (rs.getInt(1) == userID) {

				// Lösche Kommentar
				String sqlCommand2 = "DELETE FROM Posts WHERE ID=" + commentID;
				Database.executeQuery(sqlCommand2);

				// Dem Benutzer wieder das Board schicken.
				return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
			} else {
				byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
				return new ByteArrayInputStream(pageBytes);
			}

		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}
}