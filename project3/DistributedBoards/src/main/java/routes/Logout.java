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

@Path("Logout")
public class Logout {

	
	@GET
	public InputStream logout(@Context HttpServletRequest request) throws FileNotFoundException {
		
		String ip = request.getRemoteAddr();
		Permissions.destroySession(ip);
		return new FileInputStream(new File("WebContent\\HTML\\Login.html"));
		
	}
	
}
