package routes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public static InputStream sendBoardsPage(@DefaultValue("0") @QueryParam("board") int boardID, @Context HttpServletRequest request) throws FileNotFoundException, UnsupportedEncodingException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return new FileInputStream(new File("WebContent\\HTML\\Login.html"));	 
		} else {
			return createPage(Permissions.isAuthorized(userID, boardID));
		}
	}

	private static InputStream createPage(Permission p) throws UnsupportedEncodingException, FileNotFoundException {

		InputStream ret = null;
		switch (p) {
		case Admin:
			ret = new FileInputStream(new File("WebContent\\HTML\\Boards.html"));
			break;
		case User:
			ret = new FileInputStream(new File("WebContent\\HTML\\Boards.html"));
			break;
		case None:
			String side = "<h1>You dont have the rights to acces this site!<h1>";
			ret = new ByteArrayInputStream(side.getBytes(StandardCharsets.UTF_8.name()));
			break;
		}

		return ret;
	}

}
