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
			// Benutzer ist nicht angemeldet.
			return Resources.getResource("Login.html", "html");
		} else if(profileID == userID) {
			// Benutzer schaut sein eigenes Profil an.
			return HTMLBuilder.buildProfilePage(userID,true);
		} else {
			// Eine Benutzer schaut das Profil eines anderen Benutzers an.
			return HTMLBuilder.buildProfilePage(userID, false);
		}
	}
}