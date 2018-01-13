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
	public InputStream getData(@DefaultValue("0") @QueryParam("board") int boardID, @Context HttpServletRequest request) throws FileNotFoundException, UnsupportedEncodingException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return new FileInputStream(new File("WebContent\\HTML\\Login.html"));	 
		} else {
			return createPage(Permissions.isAuthorized(userID, boardID));
		}
	}

	private InputStream createPage(Permission p) throws UnsupportedEncodingException {

		String ret = "";
		switch (p) {
		case Admin:
			ret = "<h1>Board page: Permission Admin <h1>";
			break;
		case User:
			ret = "<h1>Board page: Permission User<h1>";
			break;
		case None:
			ret = "<h1>You dont have the rights to acces this site!<h1>";
			break;
		}

		return new ByteArrayInputStream(ret.getBytes(StandardCharsets.UTF_8.name()));
	}

}
