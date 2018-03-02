package htmlBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import util.Permission;

/**
 * Die Klasse fügt die benutzerspezifischen Daten in die jeweilige Seite ein.
 * 
 * @author Tristan Glaes
 *
 */
public class HTMLBuilder {

	private static final String boardListMarker = "###BoardsList###";
	private static final String boardNameMarker = "###BoardName###";
	private static final String boardPostsMarker = "###BoardPosts###";
	private static final String userNameMarker = "###UserName###";
	private static final String boardAdminMarker = "###BoardAdmin###";
	private static final String boardUserListMarker = "###BoardUsers###";
	private static final String loginFailedMarker = "###LoginFailed###";
	private static final String profileLinkMarker = "###Profile###";
	private static final String numberBoardsMarker = "###NumberBoards###";
	private static final String numberPostsMarker = "###NumberPosts###";
	private static final String numberCommentsMarker = "###NumberComments###";
	private static final String boardDeleteButtonMarker = "###deleteButtonBoard###";
	private static final String boardIDMarker = "###boardID###";
	private static final String charset = StandardCharsets.UTF_8.name();

	/**
	 * Baut die HTML Seite für eine Benutzer zusammen indem die definierten Marken
	 * im Template mit HTML Code ersetzt wird.
	 * 
	 * @param userID
	 *            Die ID des Benutzers der die Seite besucht.
	 * @return Einen InputStream, der die HTML Seite enthält.
	 * @throws IOException
	 * @throws SQLException
	 */
	public static InputStream buildBoardsPage(int userID, int boardID, Permission p) throws IOException, SQLException {

		// String Array der Größe 2, die die 2 Hälften der Seite beinhaltet.
		String[] page;
		// Die neue Seite.
		String newPage;

		// Einfügen der Boardliste
		page = splitHTMLPageAtMarker(boardListMarker, "WebContent\\HTML\\Boards.html");
		String boardListHTML = getBoardsListForUser(userID);
		newPage = page[0] + boardListHTML + page[1];

		// Einfügen des Board Names.
		page = splitStringPageAtMarker(boardNameMarker, newPage);
		String boardNameHTML = getBoardName(boardID);
		newPage = page[0] + boardNameHTML + page[1];

		// Einfügen der Posts und Kommentare
		page = splitStringPageAtMarker(boardPostsMarker, newPage);
		String postListHTML = getPosts(boardID);
		newPage = page[0] + postListHTML + page[1];
	
		
		//TODO: Anderes Zeug anzeigen wenn Tafel Zentrale Tafel.
		//Einfügen des Admins des Boards in Sidebar
		page = splitStringPageAtMarker(boardAdminMarker, newPage);
		String adminHTML = getAdminForBoard(boardID);
		newPage = page[0] + adminHTML + page[1];
		
		//Einfügen der Benutzer
		page = splitStringPageAtMarker(boardUserListMarker, newPage);
		String userListHTML = getUserListForBoard(boardID);
		newPage = page[0] + userListHTML + page[1];
		
		//Einfügen des Links zum eigenden Profil.
		page = splitStringPageAtMarker(profileLinkMarker, newPage);
		String linkToUserProfile = "/DistributedBoards/Profile?profile=" + userID;
		newPage = page[0] + linkToUserProfile + page[1];		
		
		// Einfügen der boardID
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];	
		
		page = splitStringPageAtMarker(boardDeleteButtonMarker, newPage);
		if(p == Permission.Admin) {		
			newPage = page[0]+ "<button onclick=\"window.location.href='/DistributedBoards/Boards/deleteBoard?board=" + boardID + "'\" class='btn btn-danger'> Board Löschen</button>"+ page[1];
		} else {
			newPage = page[0] + page[1];
		}
			
			
		return new ByteArrayInputStream(newPage.getBytes(charset));
	}
	
	public static InputStream buildProfilePage(int userID, boolean editable) throws IOException, SQLException {
		
		// String Array der Größe 2, die die 2 Hälften der Seite beinhaltet.
		String[] page;
		// Die neue Seite.
		String newPage;

		// Einfügen der Boardliste
	    page = splitHTMLPageAtMarker(boardListMarker, "WebContent\\HTML\\Profile.html");
		String boardListHTML = getBoardsListForUser(userID);
		newPage = page[0] + boardListHTML + page[1];
		
		// TODO: Benutzerinformationen eintragen
		
		//Statistiken
		page = splitStringPageAtMarker(numberBoardsMarker, newPage);
		String numberBoards = getNumberOfBoards(userID);
		newPage = page[0] + numberBoards + page[1];
		
		page = splitStringPageAtMarker(numberPostsMarker, newPage);
		String numberPosts = getNumberOfPosts(userID);
		newPage = page[0] + numberPosts + page[1];
		
		page = splitStringPageAtMarker(numberCommentsMarker, newPage);
		String numberComments = getNumberOfComments(userID);
		newPage = page[0] + numberComments + page[1];
		
		return new ByteArrayInputStream(newPage.getBytes(charset));
	}
	
	

	private static String getNumberOfComments(int userID) throws SQLException {
		String sqlCommand = "SELECT COUNT(*) FROM Posts WHERE User=" + userID + " AND Post=0";
		ResultSet rs = Database.executeSql(sqlCommand);
		if(rs.next()) {
			String numberOfComments = String.valueOf(rs.getInt(1));
			Database.closeConnection();
			return numberOfComments;
		} else {
			return "0";
		}
	}

	private static String getNumberOfPosts(int userID) throws SQLException {
		String sqlCommand = "SELECT COUNT(*) FROM Posts WHERE User=" + userID + " AND Post>0";
		ResultSet rs = Database.executeSql(sqlCommand);
		if(rs.next()) {
			String numberOfPosts = String.valueOf(rs.getInt(1));
			Database.closeConnection();		
			return numberOfPosts;
		} else {
			return "0";
		}
		
	}

	private static String getNumberOfBoards(int userID) throws SQLException {
		String sqlCommand = "SELECT COUNT(*) FROM UserBoards WHERE User=" + userID;
		ResultSet rs = Database.executeSql(sqlCommand);
		if(rs.next()) {
			String numberOfBoards = String.valueOf(rs.getInt(1));
			Database.closeConnection();				
			return numberOfBoards;
		} else {
			return "0";
		}
	}

	private static String getUserListForBoard(int boardID) throws SQLException {
		String sqlCommand = "SELECT u.Username,u.ID FROM UserBoards as ub JOIN Users as u ON ub.User=u.ID  WHERE Board=" + boardID;
		ResultSet rs = Database.executeSql(sqlCommand);
		String userListForBoardHTML = "";
		
		while(rs.next()) {
			userListForBoardHTML += "<p align='left' class='list-group-item'><a href='Profile?profile=" + rs.getString(2) + "'>" + rs.getString(1) + "</a>"
					+ "<a href='#' data-toggle='modal' data-target='#delete-user-modal' class='btn btn-lg' style='background-color: white; color: black; float: right;'>"
					+ "<span class='glyphicon glyphicon-trash' style='float: right; height: 2px;'></span> " + "</a></p>";
		}
		
		return userListForBoardHTML;
	}

	private static String getAdminForBoard(int boardID) throws SQLException {
		
		String sqlCommand = "SELECT u.Username,u.ID FROM Boards as b JOIN Users as u ON b.Admin=u.ID WHERE b.ID=" + boardID;
		ResultSet rs = Database.executeSql(sqlCommand);
		
		if(rs.next()) {
			return "<p class='list-group-item'><a href='Profile?profile=" + rs.getString(2) + "'>" + rs.getString(1) + "</a></p>";
		}
		
		return "";
	}

	private static String getPosts(int boardID) throws SQLException {

		String noPosts = "<br><br><h1>No Posts yet! <h1>";
		String posts = "";
		boolean postsFound = false;
		String sqlCommand = "SELECT u.Username,p.Date,p.ID,p.Content,u.ID FROM Posts as p JOIN BoardPosts as bp JOIN Users as u ON bp.Post=p.ID AND u.ID=p.User WHERE bp.Board="
				+ boardID + " ORDER BY Date DESC";
		ResultSet rs = Database.executeSql(sqlCommand);

		while (rs.next()) {
			postsFound = true;
			posts += "<div class='post'>" + 
					"<hr>" + 
					"<div class='media'><div class='media-left'>" + 
					"<img src='/DistributedBoards/Resources?resourceName=Meris.jpg&resourceType=img' class='media-object' style='width:45px'>" + 
					"</div>" + 
					"<div class='media-body'>" +
					"<h4 class='media-heading'>" + rs.getString(1) + "<small><i>" + rs.getString(2) + "</i>" +
					"<a href='#' data-toggle='modal' data-target='#editPost-modal' class='btn btn-lg' style='background-color: #F1F1F1; color: black; float: right;'>" + 
					"<span class='glyphicon glyphicon-cog' style='margin-top: 15px;'></span>" + 
					"</a>"+
				    "</small></h4><p>" + rs.getString(4) + "</p>" +
					"<div class='btn-group'>" + 
					"<a href='#' data-toggle='modal' data-target='#???????-modal' class='btn btn-md'>" + 
					"<span class='glyphicon glyphicon-thumbs-up'></span> Like" + 
					"</a>" + 
					"<a href='#' data-toggle='' data-target='#comment-modal' class='btn btn-md'>" + 
					"<span class='glyphicon glyphicon-edit'></span> Comment" + 
					"</a>" + 
					"<a href='' data-toggle='modal' data-target='#???????-modal' class='btn btn-md'>" + 
					"<span class='glyphicon glyphicon-bookmark'></span> Mark" + 
					"</a>" + 
					"</div>";
							
			String sqlCommand2 = "SELECT p.ID,p.Content,p.Date,u.Username FROM Posts AS p JOIN Users AS u ON p.User=u.ID WHERE Post=" + rs.getInt(3) + " ORDER BY Date";
			ResultSet rs2 = Database.executeSql(sqlCommand2);
			while(rs2.next()) {
				
				posts += "<div class='media'>" + 
						"<div class='media-left'>" + 
						"<img src='/DistributedBoards/Resources?resourceName=Boyka.jpg&resourceType=img' class='media-object' style='width:45px'></div>" + 
						"<div class='media-body'>" + 
						"<h4 class='media-heading'>" + rs2.getString(4) + "<small><i>" + rs2.getString(3) + "</i></small></h4>" + 
						"<p>" + rs2.getString(2) + "</p></div></div>";
				
			}
							
			posts += "</div></div></div>";
			
		}

		if (postsFound) {
			return posts;
		} else {
			return noPosts;
		}
	}

	private static String getBoardName(int boardID) throws SQLException {

		String sqlCommand = "SELECT name FROM Boards WHERE ID=" + boardID;
		ResultSet rs = Database.executeSql(sqlCommand);

		if (rs.next()) {
			return "<h1 style='margin-left: 10px;' class='tafel-name'>" + rs.getString(1) + "</h1>";
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param userID
	 *            Die ID des Benutzers der die Seite besucht.
	 * @return Gibt die HTML Repräsentation der Tafel, die dem Benutzer zugeordnet
	 *         sind, zurück.
	 * @throws SQLException
	 */
	private static String getBoardsListForUser(int userID) throws SQLException {

		String boardsListHTML = "";
		// Suche alle Tafeln auf denen der Benutzer ein normaler User ist.
		String sqlCommand = "SELECT ID,name FROM Boards as b,UserBoards AS ub WHERE ub.Board=b.ID AND ub.User=" + userID
				+ " ORDER BY ID ASC";
		ResultSet rs = Database.executeSql(sqlCommand);

		while (rs.next()) {
			boardsListHTML += "<p><a href='/DistributedBoards/Boards?board=" + rs.getInt(1) + "'>" + rs.getString(2) + "</a></p>";

			// Nach der Zentralen Anzeigetafeln wird ein Seperator eingefügt.
			if (rs.getInt(1) == 0) {
				boardsListHTML += "<hr noshade style=\"width:100%; height:0px; text-align:left; border:3px solid rgb(34, 34, 34);\">";
			}
		}

		// SUche alle Tafeln auf denen der Benutuzer Admin ist.
		sqlCommand = "SELECT ID,name FROM Boards WHERE Admin=" + userID + " ORDER BY ID ASC";
		rs = Database.executeSql(sqlCommand);

		while (rs.next()) {
			boardsListHTML += "<p><a href='/DistributedBoards/Boards?board=" + rs.getInt(1) + "'>" + rs.getString(2) + "</a></p>";
		}
		// Schließen der Datenbankverbindung
		Database.closeConnection();

		return boardsListHTML;
	}

	/**
	 * Entfernt die Markierung aus der Seite und gibt die 2 Hälften zurück.
	 * 
	 * @param marker
	 *            Die Markierung an der der neue Inhalt eingefügt werden soll.
	 * @param path
	 *            Der relative Pfad zur Resource.
	 * @return Ein String Array mit den zwei Seiten.
	 * @throws IOException
	 */
	private static String[] splitHTMLPageAtMarker(String marker, String path) throws IOException {

		// Wandle die Seite in einen String um.
		byte[] pageBytes = Files.readAllBytes(Paths.get(path));
		String pageTemplate = new String(pageBytes, charset);

		// Index der Marke
		int index = pageTemplate.indexOf(marker);
		String[] pageParts = new String[2];
		// Spalte die Seite in zwei Teile (alles vor und alles nach der Marke).
		pageParts[0] = pageTemplate.substring(0, index);
		pageParts[1] = pageTemplate.substring(index + marker.length());
		return pageParts;

	}

	private static String[] splitStringPageAtMarker(String marker, String page) {

		// Index der Marke
		int index = page.indexOf(marker);
		String[] pageParts = new String[2];
		// Spalte die Seite in zwei Teile (alles vor und alles nach der Marke).
		pageParts[0] = page.substring(0, index);
		pageParts[1] = page.substring(index + marker.length());
		return pageParts;

	}

	public static InputStream buildFailedLogin() throws IOException {
		
		String[] page = splitHTMLPageAtMarker(loginFailedMarker, "WebContent\\HTML\\Login.html");
		String loginFailedAlert = "<div class='alert alert-danger' role='alert'>" + 
				"<strong>LOGIN FAILED!</strong></div>"; 
		String newPage = page[0] + loginFailedAlert + page[1];
		newPage = newPage.replace("hidden", "");
				
		return new ByteArrayInputStream(newPage.getBytes(charset));
	}
	
	
	public static InputStream buildFailedRegistration(int errorCode) throws IOException {
		
		//Remove the hidden attribute form the error display.
		String[] page = splitHTMLPageAtMarker("hidden", "WebContent\\HTML\\Registrieren_new.html");		
		String newPage = page[0] + "" + page[1];
		
		
		//Mark the fields that are not correct.
		
		return new ByteArrayInputStream(newPage.getBytes(charset));
	}
}
