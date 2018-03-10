package routes;

import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

//import org.glassfish.jersey.media.multipart.FormDataParam;
//import com.sun.jersey.core.header.FormDataContentDisposition;

import database.Database;
import htmlBuilder.HTMLBuilder;
import util.Permissions;

/**
 * 
 * @author Tristan Glaes
 * @version 09.03.2018
 */
@Path("/Profile")
public class Profile {

	//private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C:\\Users\\Tristan Glaes\\Documents\\team5ds\\project3\\DistributedBoards\\WebContent\\Images\\";

	/**
	 * 
	 * @param profileID
	 *            Die ID des Profils, das aufgerufen werden soll.
	 * @param request
	 *            Der HTTP Request.
	 * @return Die Profilseite
	 * @throws IOException
	 * @throws SQLException
	 */
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
			if (profileID == 0) {
				return HTMLBuilder.buildProfilePage(userID, userID);
			} else {
				return HTMLBuilder.buildProfilePage(userID, profileID);
			}
		}
	}

	/**
	 * 
	 * @param profileID
	 *            Die ProfilID, des Profils, das editiert werden soll.
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param pwd
	 * @param pwdRep
	 * @param age
	 * @param profession
	 * @param request
	 *            Der HTTP Request
	 * @return Die Profilseite
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Path("/editProfile")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream editProfile(@QueryParam("profileID") int profileID,
			@FormParam("username") String username, @FormParam("firstname") String firstname,
			@FormParam("lastname") String lastname, @FormParam("email") String email, @FormParam("pwd") String pwd,
			@FormParam("pwd-rep") String pwdRep, @FormParam("age") String age,
			@FormParam("profession") String profession, @Context HttpServletRequest request)
			throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			// Benutzer ist nicht angemeldet.
			return Resources.getResource("Login.html", "html");
			// Benutzer bearbeitet eigenes Profil
		} else if (profileID == userID) {

			String sqlCommand = "UPDATE Users SET Username='" + username + "', Vorname='" + firstname + "',"
					+ "Nachname='" + lastname + "',EMail='" + email + "', Password='" + pwd + "',[Alter]=" + age
					+ ", Beruf='" + profession + "' WHERE ID=" + profileID;
			Database.executeQuery(sqlCommand);
			Database.closeConnection();

			return HTMLBuilder.buildProfilePage(userID, profileID);
		} else {
			// User ist nicht berechtigt.
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}
	
	@GET
	@Path("/deleteProfile")
	@Produces(MediaType.TEXT_HTML)
	public static InputStream deleteProfile(@Context HttpServletRequest request)
			throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			// Benutzer ist nicht angemeldet.
			return Resources.getResource("Login.html", "html");
			// Benutzer bearbeitet eigenes Profil
		} else {
			
			String sqlCommand;
			ResultSet rs;
			
			// Löschen des Profils.
			sqlCommand = "DELETE FROM Users WHERE ID=" + userID;
			Database.executeQuery(sqlCommand);
			
			// Löschen aller Posts,Kommentare und Marks/Likes.
			sqlCommand = "SELECT ID FROM Posts WHERE User=" + userID;
			rs = Database.executeSql(sqlCommand);
			while(rs.next()) {
				
				// Löschen der Marks/Likes
				sqlCommand = "DELETE FROM PostMarks WHERE Post=" + rs.getInt(1);
				Database.executeQueryWithOutClose(sqlCommand);
				
				// Löschen der Zuordnung zum Board.
				sqlCommand = "DELETE FROM BoardPosts WHERE Post=" + rs.getInt(1);
				Database.executeQueryWithOutClose(sqlCommand);
				
				// Löschen der Kommentare des Posts
				sqlCommand = "DELETE FROM Posts WHERE Post=" + rs.getInt(1);
				Database.executeQueryWithOutClose(sqlCommand);
				
				// Löschen des Posts
				sqlCommand = "DELETE FROM Posts WHERE ID=" + rs.getInt(1);
				Database.executeQueryWithOutClose(sqlCommand);
			}
				
			// Löschen der Zugehörigkeiten zu den Boards.
			sqlCommand = "DELETE FROM UserBoards WHERE User=" + userID;
			Database.executeQuery(sqlCommand);
			
			// Alle Boards auf denen der Benutzer Admin ist.
			sqlCommand = "SELECT ID FROM Boards WHERE Admin=" + userID;
			rs = Database.executeSql(sqlCommand);
			
			while(rs.next()) {
				// Entferne alle Benutzer aus dem Board.
				sqlCommand = "DELETE FROM UserBoards WHERE Board=" + rs.getInt(1);
				Database.executeQueryWithOutClose(sqlCommand);
				
				// Entferne das Board.
				sqlCommand = "DELETE FROM Boards WHERE Admin=" + userID;
				Database.executeQueryWithOutClose(sqlCommand);
				
				//Lösche alle Posts von dem Board
				sqlCommand = "SELECT Post FROM BoardPosts WHERE Board=" + rs.getInt(1);
				ResultSet rs2 = Database.executeSql(sqlCommand);
				while(rs2.next()) {
					// Löschen der Marks/Likes
					sqlCommand = "DELETE FROM PostMarks WHERE Post=" + rs2.getInt(1);
					Database.executeQueryWithOutClose(sqlCommand);
					
					// Löschen der Zuordnung zum Board.
					sqlCommand = "DELETE FROM BoardPosts WHERE Post=" + rs2.getInt(1);
					Database.executeQueryWithOutClose(sqlCommand);
					
					// Löschen der Kommentare des Posts
					sqlCommand = "DELETE FROM Posts WHERE Post=" + rs2.getInt(1);
					Database.executeQueryWithOutClose(sqlCommand);
					
					// Löschen des Posts
					sqlCommand = "DELETE FROM Posts WHERE ID=" + rs2.getInt(1);
					Database.executeQueryWithOutClose(sqlCommand);
				}		
			}
					
			Database.closeConnection();
			
			// Benutzer wird auf die Loginseite umgeleitet.
			return Resources.getResource("Login.html", "html");
		}
	}
	

	/**
	 * NOT USED.
	 * 
	 * @param profileID
	 * @param request
	 * @param fileInputStream
	 * @param fileDetail
	 * @return
	 * @throws IOException
	 * @throws SQLException

	@POST
	@Path("/uploadProfilePicture")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public static InputStream uploadFile(@QueryParam("profile") int profileID, @Context HttpServletRequest request,
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException, SQLException {

		String ip = request.getRemoteAddr();
		Integer userID = Permissions.hasSession(ip);
		if (userID == null) {
			// Benutzer ist nicht angemeldet.
			return Resources.getResource("Login.html", "html");
			// Benutzer bearbeitet eigenes Profil
		} else if (profileID == userID) {

			String filePath = SERVER_UPLOAD_LOCATION_FOLDER + profileID;
			OutputStream outpuStream = new FileOutputStream(new File(filePath));
			int read = 0;
			byte[] bytes = new byte[2097152];

			outpuStream = new FileOutputStream(new File(filePath));
			while ((read = fileInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();

			return HTMLBuilder.buildProfilePage(userID, profileID);

		} else {
			// User ist nicht berechtigt.
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/403.html"));
			return new ByteArrayInputStream(pageBytes);
		}
	}	 */
}