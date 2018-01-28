package routes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
			ret = HTMLBuilder.buildBoardsPage(userID, boardID);
			break;
		case User:
			ret = HTMLBuilder.buildBoardsPage(userID, boardID);
			break;
		case None:
			String side = "<h1>You dont have the rights to acces this site!<h1>";
			ret = new ByteArrayInputStream(side.getBytes(StandardCharsets.UTF_8.name()));
			break;
		}

		return ret;
	}
}