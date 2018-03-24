package routes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import util.Permissions;

/**
 * 
 * @author Tristan Glaes
 * @version 09.03.2018
 */
@Path("Logout")
public class Logout {

	/**
	 * L�st die Session auf.
	 * 
	 * @param request Der HTTP Request.
	 * @return Die Loginseite
	 * @throws FileNotFoundException
	 */
	@GET
	public InputStream logout(@Context HttpServletRequest request) throws FileNotFoundException {
		
		String ip = request.getRemoteAddr();
		Permissions.destroySession(ip);
//		return new FileInputStream(new File("WebContent\\HTML\\Login.html"));
		return new FileInputStream(new File("WebContent/HTML/Login.html"));
	}
}