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

import database.Database;
import htmlBuilder.HTMLBuilder;
import util.Permissions;

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
	 * @throws FileNotFoundException 
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public InputStream sendLoginPage() throws FileNotFoundException {
		
		return Resources.getResource("Login.html", "html");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
    public InputStream getLoginData(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletRequest request) throws SQLException, IOException {
        
		String sqlCommand = "SELECT * FROM Users WHERE Username='" + username + "' AND Password='" + password +"'";
		ResultSet rs = Database.executeSql(sqlCommand);
		
		if(rs.next()) {
			
			Integer userID = rs.getInt(1);
			String ip = request.getRemoteAddr();
			
			Permissions.createSession(ip, userID);
			Database.closeConnection();
			return Boards.sendBoardsPage(0, request);
			
		} else {
			return HTMLBuilder.buildFailedLogin();
		}
    }
}
