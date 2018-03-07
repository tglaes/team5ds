package routes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
		} else {
			if(profileID == 0) {
				return HTMLBuilder.buildProfilePage(userID, userID);
			} else {
				return HTMLBuilder.buildProfilePage(userID, profileID);
			}
			
		}
	}
	
	@POST
	@Path("/editProfile")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream editProfile(@QueryParam("profileID") int profileID,
										  @FormParam("username") String username,
										  @FormParam("firstname") String firstname,
										  @FormParam("lastname") String lastname,
										  @FormParam("email") String email,
										  @FormParam("pwd") String pwd,
										  @FormParam("pwd-rep") String pwdRep,
										  @FormParam("age") String age,
										  @FormParam("profession") String profession,
										  @Context HttpServletRequest request
									) throws IOException, SQLException {
		
		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			// Benutzer ist nicht angemeldet.
			return Resources.getResource("Login.html", "html");
			//Benutzer bearbeitet eigenes Profil
		} else if(profileID == userID) {
		
			String sqlCommand = "UPDATE Users SET Username='" + username + "', Vorname='" + firstname + "',"
					+ "Nachname='" + lastname + "',EMail='" + email + "', Password='" + pwd + "',[Alter]=" + age +
					", Beruf='" + profession + "' WHERE ID=" + profileID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();
			
			return HTMLBuilder.buildProfilePage(userID,profileID);
		} else {
			// User ist nicht berechtigt.
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}	
	}
}