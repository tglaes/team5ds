package routes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

@Path("/Boards")
public class Boards {

	/**
	 * Route is requested when DistributedBoards/Boards?board=xxx is called.
	 * 
	 * @param user
	 * @param request
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public static InputStream sendBoardsPage(@DefaultValue("0") @QueryParam("board") int boardID,
			@Context HttpServletRequest request) throws IOException, SQLException {
		// System.out.println(boardID);
		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {
			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		}
	}

	private static InputStream createPage(Permission p, int userID, int boardID) throws IOException, SQLException {

		// TODO: gibt es das Board, falls nicht Page not found anzeigen.

		// System.out.println(boardID);
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

	@GET
	@Path("/deleteBoard")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deleteBoard(@QueryParam("board") int boardID, @Context HttpServletRequest request)
			throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {

			// TODO: Überprüfen ob User Admin ist.

			// Löschen des Boards.
			// System.out.println("Lösche Board mit ID: " + boardID);
			String sqlCommand = "DELETE FROM Boards WHERE ID=" + boardID;
			Database.executeQuery(sqlCommand);
			// User Board Abhängigkeiten löschen.
			sqlCommand = "DELETE FROM UserBoards WHERE board=" + boardID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();

			return createPage(Permissions.isAuthorized(userID, 0), userID, 0);
		}
	}

	@GET
	@Path("/removeUser")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deleteBoard(@QueryParam("board") int boardID, @QueryParam("user") int removeUserID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {

			// TODO: Überprüfen ob User Admin ist.

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

			// Erstelle das Kommentar
			String sqlCommand = "INSERT INTO Posts (Content, Date, Post, User) VALUES('" + commentText + "', DATETIME('now')," + postID + " ," + userID + ")";
			Database.executeQuery(sqlCommand);

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}

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

		} else {

			// TODO: Überprufen ob der User im Board ist.

			// Post in die Datenbank eintragen.
			String sqlCommand = "INSERT INTO Posts (Content,Date,Post,User) VALUES('" + postText + "', DATETIME('now'),0," + userID + ")";
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
		}
	}

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
		} else {

			String sqlCommand = "UPDATE Posts SET content='" + postText + "' WHERE ID=" + postID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();

			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		}
	}

	/***
	 * 
	 * @param boardID
	 * @param postID
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@GET
	@Path("/deletePost")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deletePost(@QueryParam("board") int boardID, @QueryParam("post") int postID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {

			if (Permissions.isAuthorized(userID, boardID) == Permission.Admin) {
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
}