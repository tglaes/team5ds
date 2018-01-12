package routes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.Database;

/**
 * The class is used to check if a user has a current session.
 * 
 * @author Tristan Glaes
 *
 */
@Path("/Login")
public class Login {
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sendLoginPage() {
		Database.executeQuery("test");
		return "test";
	}

}
