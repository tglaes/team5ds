package routes;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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


@Path("/Registration")
public class Registration {

	@GET
	public InputStream sendRegistrationPage() throws FileNotFoundException {
		return Resources.getResource("Registrieren.html", "html");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
    public InputStream getLoginData(@FormParam("username") String username, @FormParam("psw") String password, @FormParam("email") String email, @Context HttpServletRequest request) throws SQLException, IOException {
        
		String test = username + " " + password + "   " + email;
		//TODO: Benutzer in Datenbank eintragen.
		//TODO: Benutzer in SessionMap eintragen.
		//TODO: Benutzer auf die Board Seite weiterleiten.
		return new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8.name()));
		
    }
}