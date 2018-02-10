package routes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {
			return createPage(Permissions.isAuthorized(userID, boardID), userID, boardID);
		}
	}

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
			String side = "<h1>You dont have the rights to acces this site!<h1>";
			ret = new ByteArrayInputStream(side.getBytes(StandardCharsets.UTF_8.name()));
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
			
			//Create the new board and send the Boards page showing the new board.
			int boardID = 0;
			
			// Board in DB einfügen.
			String sqlCommand = "INSERT INTO Boards (Admin,Name) VALUES('" + userID + "', '" + boardName + "')";
			Database.executeQuery(sqlCommand);
			
			// Letzte ID ausgeben
			sqlCommand = "SELECT ID FROM Boards ORDER BY ID DESC LIMIT 1";
			ResultSet rs = Database.executeSql(sqlCommand);
			
			if(rs.next()) {
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
	public static InputStream deleteBoard(@QueryParam("board") int boardID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {
			
			//Löschen des Boards.
			System.out.println("Lösche Board mit ID: " + boardID);
			String sqlCommand = "DELETE FROM Boards WHERE ID=" + boardID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();
			
			return createPage(Permissions.isAuthorized(userID, 0), userID, 0);
		}
	}
}