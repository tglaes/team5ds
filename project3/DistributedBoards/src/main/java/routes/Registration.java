package routes;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import util.Resources;

@Path("/Registration")
public class Registration {

	@GET
	public InputStream sendRegistrationPage() throws FileNotFoundException {
		return Resources.getResource("Registrieren.html", "html");
	}
	
}