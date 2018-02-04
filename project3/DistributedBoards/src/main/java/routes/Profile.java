package routes;

import java.io.IOException;
import java.io.InputStream;
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

@Path("/Profile")
public class Profile {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public static InputStream getProfile(@DefaultValue("0") @QueryParam("profile") int profileID,
			@Context HttpServletRequest request) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			return Resources.getResource("Login.html", "html");
		} else {
			// TODO: HTMLBuilder aufrufen.
			return Resources.getResource("Profile.html", "html");
		}
	}
}
