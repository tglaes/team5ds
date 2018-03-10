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
 * 
 * @author Tristan Glaes
 * @version 09.03.2018
 */
public class HTMLBuilder {

	private static final String boardListMarker = "###BoardsList###";
	private static final String boardNameMarker = "###BoardName###";
	private static final String boardPostsMarker = "###BoardPosts###";
	// private static final String userNameMarker = "###UserName###";
	private static final String boardAdminMarker = "###BoardAdmin###";
	private static final String boardUserListMarker = "###BoardUsers###";
	private static final String loginFailedMarker = "###LoginFailed###";
	private static final String profileLinkMarker = "###Profile###";
	private static final String numberBoardsMarker = "###NumberBoards###";
	private static final String numberPostsMarker = "###NumberPosts###";
	private static final String numberCommentsMarker = "###NumberComments###";
	private static final String boardDeleteButtonNewUserMarker = "###deleteButtonNewUserBoard###";
	private static final String boardIDMarker = "###boardID###";
	private static final String profileFirstnameMarker = "###firstname###";
	private static final String profileLastnameMarker = "###lastname###";
	private static final String profileEmailMarker = "###email###";
	private static final String profileUsernameMarker = "###username###";
	private static final String profilePasswordMarker = "###password###";
	private static final String profileProfessionMarker = "###profession###";
	private static final String profileAgeMarker = "###age###";
	private static final String profileFullnameMarker = "###fullname###";
	private static final String profileIDMarker = "###profileID###";
	private static final String profileEditButtonMarker = "###profileEditButton###";
	private static final String postButtonMarker = "###newPostButton###";
	private static final String profileNumberLikes = "###NumberLikes###";
	private static final String profilePictureMarker = "###profilePicture###";
	private static final String charset = StandardCharsets.UTF_8.name();

	/**
	 * Erstellt, die Boardseite für den Benutzer.
	 * 
	 * @param userID Die ID des Benutzers.
	 * @param boardID Die ID des Boards.
	 * @param p Die Berechtigung
	 * @return Die Boardseite für den Benutzer( mit den entsprechenden Berechtigungen.)
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

		// Einfügen des Postbuttons
		page = splitStringPageAtMarker(postButtonMarker, newPage);
		String postButtonHTML = "";
		if (boardID != 0) {
			postButtonHTML = "<a href='#' data-toggle='modal' data-target='#post-modal' class='btn btn-lg' style='height: 69px; background-color: #F1F1F1; color: black; float: right;'>"
					+ "<span class='glyphicon' style='float: right; margin-top: 15px;'>&#x2b; Post</span>" + "</a>";
		}
		newPage = page[0] + postButtonHTML + page[1];

		// Einfügen der Posts und Kommentare
		page = splitStringPageAtMarker(boardPostsMarker, newPage);
		String postListHTML = getPosts(boardID, p, userID);
		newPage = page[0] + postListHTML + page[1];

		// Einfügen des Admins des Boards in Sidebar
		page = splitStringPageAtMarker(boardAdminMarker, newPage);
		String adminHTML = getAdminForBoard(boardID);
		newPage = page[0] + adminHTML + page[1];

		// Einfügen der Benutzer
		page = splitStringPageAtMarker(boardUserListMarker, newPage);
		String userListHTML = getUserListForBoard(boardID, p);
		newPage = page[0] + userListHTML + page[1];

		// Einfügen des Links zum eigenden Profil.
		page = splitStringPageAtMarker(profileLinkMarker, newPage);
		String linkToUserProfile = "/DistributedBoards/Profile?profile=" + userID;
		newPage = page[0] + linkToUserProfile + page[1];

		// Einfügen der boardID
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];
		page = splitStringPageAtMarker(boardIDMarker, newPage);
		newPage = page[0] + boardID + page[1];


		page = splitStringPageAtMarker(boardDeleteButtonNewUserMarker, newPage);
		if (p == Permission.Admin) {
			newPage = page[0]
					+ "<div><button href='#' data-toggle='modal' data-target='#add-user-board-modal' class='btn btn-primary'>Add User</button><br>"
					+ "<button href='#' data-toggle='modal' data-target='#delete-board-board-modal' class='btn btn-danger'> Board Löschen</button></div>" + page[1];
		} else {
			newPage = page[0] + page[1];
		}

		return new ByteArrayInputStream(newPage.getBytes(charset));
	}

	/**
	 * Erstellt die Profilseite.
	 * 
	 * @param userID Die ID des Benutzers.
	 * @param profileID Die ID des Profils
	 * @return Die Profilseite.
	 * @throws IOException
	 * @throws SQLException
	 */
	public static InputStream buildProfilePage(Integer userID, Integer profileID) throws IOException, SQLException {

		// Der Benutzer kann über die profileID 0 auf sein eigenes Profil kommen.
		if (profileID == 0) {
			profileID = userID;
		}

		// String Array der Größe 2, die die 2 Hälften der Seite beinhaltet.
		String[] page;
		// Die neue Seite.
		String newPage;

		// Einfügen der Boardliste
		page = splitHTMLPageAtMarker(boardListMarker, "WebContent\\HTML\\Profile.html");
		String boardListHTML = getBoardsListForUser(userID);
		newPage = page[0] + boardListHTML + page[1];

		// Einfügen des Links zum eigenen Profil.
		page = splitStringPageAtMarker(profileLinkMarker, newPage);
		String linkToUserProfile = "/DistributedBoards/Profile?profile=" + userID;
		newPage = page[0] + linkToUserProfile + page[1];

		// Einfügen des Links zum Profilbild
		// Konvention des Namens des Profilbildes ist UserID
		page = splitStringPageAtMarker(profilePictureMarker, newPage);
		newPage = page[0] + profileID + page[1];

		// ProfilId des Benutzers einfügen.
		page = splitStringPageAtMarker(profileIDMarker, newPage);
		newPage = page[0] + profileID + page[1];
		page = splitStringPageAtMarker(profileIDMarker, newPage);
		newPage = page[0] + profileID + page[1];

		// Information des Benutzers auslesen und einfügen.
		String sqlCommand = "SELECT * FROM Users WHERE ID=" + profileID;
		ResultSet rs = Database.executeSql(sqlCommand);

		if (rs.next()) {
			page = splitStringPageAtMarker(profileFirstnameMarker, newPage);
			String firstname = rs.getString(5);
			newPage = page[0] + firstname + page[1];

			page = splitStringPageAtMarker(profileFirstnameMarker, newPage);
			firstname = rs.getString(5);
			newPage = page[0] + firstname + page[1];

			page = splitStringPageAtMarker(profileLastnameMarker, newPage);
			String lastname = rs.getString(6);
			newPage = page[0] + lastname + page[1];

			page = splitStringPageAtMarker(profileLastnameMarker, newPage);
			lastname = rs.getString(6);
			newPage = page[0] + lastname + page[1];

			page = splitStringPageAtMarker(profileProfessionMarker, newPage);
			String profession = rs.getString(7);
			newPage = page[0] + profession + page[1];
			page = splitStringPageAtMarker(profileProfessionMarker, newPage);
			newPage = page[0] + profession + page[1];
			page = splitStringPageAtMarker(profileProfessionMarker, newPage);
			newPage = page[0] + profession + page[1];

			page = splitStringPageAtMarker(profilePasswordMarker, newPage);
			String pwd = rs.getString(2);
			newPage = page[0] + pwd + page[1];
			page = splitStringPageAtMarker(profilePasswordMarker, newPage);
			newPage = page[0] + pwd + page[1];

			page = splitStringPageAtMarker(profileUsernameMarker, newPage);
			String username = rs.getString(4);
			newPage = page[0] + username + page[1];
			page = splitStringPageAtMarker(profileUsernameMarker, newPage);
			newPage = page[0] + username + page[1];

			page = splitStringPageAtMarker(profileAgeMarker, newPage);
			String age = rs.getString(8);
			newPage = page[0] + age + page[1];
			page = splitStringPageAtMarker(profileAgeMarker, newPage);
			newPage = page[0] + age + page[1];

			page = splitStringPageAtMarker(profileEmailMarker, newPage);
			String email = rs.getString(3);
			newPage = page[0] + email + page[1];
			page = splitStringPageAtMarker(profileEmailMarker, newPage);
			newPage = page[0] + email + page[1];

			page = splitStringPageAtMarker(profileFullnameMarker, newPage);
			String fullname = rs.getString(5) + ", " + rs.getString(6);
			newPage = page[0] + fullname + page[1];

			rs.close();
			Database.closeConnection();

			String buttonHTML = "";
			page = splitStringPageAtMarker(profileEditButtonMarker, newPage);
			if (userID == profileID) {			
				// Delete Button
				buttonHTML = "<a href='#' data-toggle='modal' data-target='#delete-modal' class='btn btn-lg' style='background-color: #990000; color: black; float: right;'>"
						+ "<span class='glyphicon glyphicon-remove'></span> Delete </a>";				
				// Edit Button
				buttonHTML += "<a href='#' data-toggle='modal' data-target='#profile-modal' class='btn btn-lg' style='background-color: #F1F1F1; color: black; float: right;'>"
						+ "<span class='glyphicon glyphicon-pencil'></span> Edit </a>";
				
				
			}
			newPage = page[0] + buttonHTML + page[1];

		} else {
			byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent/HTML/404.html"));
			return new ByteArrayInputStream(pageBytes);
		}

		// Statistiken
		page = splitStringPageAtMarker(numberBoardsMarker, newPage);
		String numberBoards = getNumberOfBoards(profileID);
		newPage = page[0] + numberBoards + page[1];

		page = splitStringPageAtMarker(numberPostsMarker, newPage);
		String numberPosts = getNumberOfPosts(profileID);
		newPage = page[0] + numberPosts + page[1];

		page = splitStringPageAtMarker(numberCommentsMarker, newPage);
		String numberComments = getNumberOfComments(profileID);
		newPage = page[0] + numberComments + page[1];

		page = splitStringPageAtMarker(profileNumberLikes, newPage);
		String numberLikes = getNumberOfLikes(profileID);
		newPage = page[0] + numberLikes + page[1];

		return new ByteArrayInputStream(newPage.getBytes(charset));
	}

	/**
	 * 
	 * @param userID
	 * @return Die Anzahl der Likes (Marks)
	 * @throws SQLException
	 */
	private static String getNumberOfLikes(int userID) throws SQLException {

		String sqlCommand = "SELECT COUNT(*) FROM PostMarks WHERE User=" + userID;
		ResultSet rs = Database.executeSql(sqlCommand);
		if (rs.next()) {
			String numberOfLikes = String.valueOf(rs.getInt(1));
			Database.closeConnection();
			return numberOfLikes;
		} else {
			return "0";
		}
	}

	/**
	 * 
	 * @param userID
	 * @return Die Anzahl der Kommentare des Benutzers.
	 * @throws SQLException
	 */
	private static String getNumberOfComments(int userID) throws SQLException {
		String sqlCommand = "SELECT COUNT(*) FROM Posts WHERE User=" + userID + " AND Post>0";
		ResultSet rs = Database.executeSql(sqlCommand);
		if (rs.next()) {
			String numberOfComments = String.valueOf(rs.getInt(1));
			Database.closeConnection();
			return numberOfComments;
		} else {
			return "0";
		}
	}

	/**
	 * 
	 * @param userID
	 * @return Die Anzahl der Posts.
	 * @throws SQLException
	 */
	private static String getNumberOfPosts(int userID) throws SQLException {
		String sqlCommand = "SELECT COUNT(*) FROM Posts WHERE User=" + userID + " AND Post=0";
		ResultSet rs = Database.executeSql(sqlCommand);
		if (rs.next()) {
			String numberOfPosts = String.valueOf(rs.getInt(1));
			Database.closeConnection();
			return numberOfPosts;
		} else {
			return "0";
		}

	}

	/**
	 * 
	 * @param userID
	 * @return Die Anzahl der Boards, Admin und normaler User.
	 * @throws SQLException
	 */
	private static String getNumberOfBoards(int userID) throws SQLException {
		// Alle Boards des Benutzers (ohne Boards auf denen er Admin ist)
		int numberBoards = 0;
		String sqlCommand = "SELECT COUNT(*) FROM UserBoards WHERE User=" + userID;
		ResultSet rs = Database.executeSql(sqlCommand);
		if (rs.next()) {
			numberBoards = rs.getInt(1);
		} else {
			numberBoards = 0;
		}

		// Alle Boards auf denen der Benutzer Admin ist.
		sqlCommand = "SELECT COUNT(*) FROM Boards WHERE Admin=" + userID;
		rs = Database.executeSql(sqlCommand);
		if (rs.next()) {
			numberBoards += rs.getInt(1);
		}
		return String.valueOf(numberBoards);

	}

	/**
	 * 
	 * @param boardID
	 * @param p Die Berechtigung.
	 * @return Eine HTML Liste von Benutzern mit Remove, Add Button für Admin.
	 * @throws SQLException
	 */
	private static String getUserListForBoard(int boardID, Permission p) throws SQLException {
		String sqlCommand = "SELECT u.Username,u.ID FROM UserBoards as ub JOIN Users as u ON ub.User=u.ID  WHERE Board="
				+ boardID;
		ResultSet rs = Database.executeSql(sqlCommand);
		String userListForBoardHTML = "";

		while (rs.next()) {
			userListForBoardHTML += "<p class='list-group-item'><a href='/DistributedBoards/Profile?profile="
					+ rs.getString(2) + "'>" + rs.getString(1) + "</a>";

			// Füge den Benutzer entfernen Button hinzu.
			if (p == Permission.Admin) {
				userListForBoardHTML += "<button onclick=\"window.location.href='/DistributedBoards/Boards/removeUser?board="
						+ boardID + "&user=" + rs.getString(2)
						+ "'\" class='btn btn-xs btn-danger' style='float:right'>Remove</button>";
			}
			userListForBoardHTML += "</p>";
		}

		return userListForBoardHTML;
	}

	/**
	 * 
	 * @param boardID
	 * @return HTML Tag mit Link zum Profil des Admins.
	 * @throws SQLException
	 */
	private static String getAdminForBoard(int boardID) throws SQLException {

		String sqlCommand = "SELECT u.Username,u.ID FROM Boards as b JOIN Users as u ON b.Admin=u.ID WHERE b.ID="
				+ boardID;
		ResultSet rs = Database.executeSql(sqlCommand);

		if (rs.next()) {
			return "<p class='list-group-item'><a href='/DistributedBoards/Profile?profile=" + rs.getString(2) + "'>"
					+ rs.getString(1) + "</a></p>";
		}
		return "";
	}

	/**
	 * 
	 * @param boardID
	 * @param p
	 * @param userID
	 * @return Die Posts mit Kommentaren des Boards, mit Remove, Edit Buttons
	 * @throws SQLException
	 */
	private static String getPosts(int boardID, Permission p, int userID) throws SQLException {

		String noPosts = "<br><br><h1>No Posts yet! <h1>";
		String posts = "";
		boolean postsFound = false;
		String sqlCommand = "SELECT u.Username,p.Date,p.ID,p.Content,u.ID FROM Posts as p JOIN BoardPosts as bp JOIN Users as u ON bp.Post=p.ID AND u.ID=p.User WHERE bp.Board="
				+ boardID + " ORDER BY Date DESC";
		ResultSet rs = Database.executeSql(sqlCommand);

		while (rs.next()) {
			String sqlCommand2 = "SELECT COUNT(*) FROM PostMarks WHERE post=" + rs.getInt(3);
			ResultSet rs2 = Database.executeSql(sqlCommand2);
			rs2.next();
			int postMarks = rs2.getInt(1);
			rs2.close();

			postsFound = true;
			posts += "<div class='post'>" + "<hr>" + "<div class='media'><div class='media-left'>"
					+ "<img src='/DistributedBoards/Resources?resourceName=Meris.jpg&resourceType=img' class='media-object' style='width:45px'>"
					+ "</div>" + "<div class='media-body'>" + "<h4 class='media-heading'>" + rs.getString(1)
					+ "<small><i> " + rs.getString(2) + "</i>";

			// Bearbeiten des Posts
			if (p == Permission.Admin || rs.getInt(5) == userID) {
				posts += "<a href='#' onclick='editPostModal(" + rs.getInt(3) + ",\"" + rs.getString(4)
						+ "\")' data-toggle='modal' data-target='#editPost-modal' class='btn btn-lg' style='background-color: #F1F1F1; color: black; float: right;'>"
						+ "<span class='glyphicon glyphicon-cog' style='margin-top: 15px;'></span>" + "</a>";
			}

			posts += "</small></h4><p>" + rs.getString(4) + "</p>" + "<div class='btn-group'>";
			// Push button
			if (p == Permission.Admin) {
				posts += "<a href='#' onclick='editPushPostModal(" + rs.getInt(3)
						+ ")' data-toggle='modal' data-target='#push-modal' class='btn btn-md'>"
						+ "<span class='glyphicon glyphicon-thumbs-up'></span> Push" + "</a>";
			}
			posts += "<a href='#' onclick='editCommentModal(" + rs.getInt(3) + ",\"" + rs.getString(4)
					+ "\")' data-toggle='modal' data-target='#comment-modal' class='btn btn-md'>"
					+ "<span class='glyphicon glyphicon-edit'></span> Comment" + "</a>";

			// Der Admin sieht nur die Anzahl der Markierungen.
			if (p == Permission.Admin) {
				posts += "<a class='btn btn-md'>" + "<span class='glyphicon glyphicon-bookmark'></span>" + postMarks
						+ "</a></div>";
				// Marks werden auf dem Zentralen Board als Likes angezeigt.
			} else if (boardID == 0) {
				posts += "<a href='/DistributedBoards/Boards/markPost?board=" + boardID + "&post=" + rs.getInt(3)
						+ "' class='btn btn-md'>" + "<span class='glyphicon glyphicon-thumbs-up'></span>" + postMarks
						+ "</a></div>";
				// Markierungen werden auf den normalen Boards angezeigt.
			} else {
				posts += "<a href='/DistributedBoards/Boards/markPost?board=" + boardID + "&post=" + rs.getInt(3)
						+ "' class='btn btn-md'>" + "<span class='glyphicon glyphicon-bookmark'></span> Mark Post("
						+ postMarks + ")" + "</a></div>";
			}

			// Kommentare
			String sqlCommand3 = "SELECT p.ID,p.Content,p.Date,u.Username,p.User FROM Posts AS p JOIN Users AS u ON p.User=u.ID WHERE Post="
					+ rs.getInt(3) + " ORDER BY Date";
			ResultSet rs3 = Database.executeSql(sqlCommand3);
			while (rs3.next()) {

				posts += "<div class='media'>" + "<div class='media-left'>"
						+ "<img src='/DistributedBoards/Resources?resourceName=Boyka.jpg&resourceType=img' class='media-object' style='width:45px'></div>"
						+ "<div class='media-body'>" + "<h4 class='media-heading'>" + rs3.getString(4) + "<small><i> "
						+ rs3.getString(3) + "</i></small></h4>";
				if (p == Permission.Admin || rs3.getInt(5) == userID) {
					posts += "<a href='/DistributedBoards/Boards/deleteComment?board="+ boardID +"&comment=" + rs3.getInt(1) + "' class='btn btn-md' style='color: red; float: right;'>"
							+ "<span class='glyphicon glyphicon-remove'></span>" + "</a>";
				}
				posts += "<p>" + rs3.getString(2) + "</p></div></div>";

			}
			posts += "</div></div></div>";

		}

		Database.closeConnection();
		if (postsFound) {
			return posts;
		} else {
			return noPosts;
		}
	}

	/**
	 * 
	 * @param boardID
	 * @return HTML Darstellung des Namens des Boards.
	 * @throws SQLException
	 */
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
	 * @return HTML Darstellung der Liste der Boards des Benutzers.
	 * @throws SQLException
	 */
	private static String getBoardsListForUser(int userID) throws SQLException {

		String boardsListHTML = "";
		// Suche alle Tafeln auf denen der Benutzer ein normaler User ist.
		String sqlCommand = "SELECT ID,name FROM Boards as b,UserBoards AS ub WHERE ub.Board=b.ID AND ub.User=" + userID
				+ " ORDER BY ID ASC";
		ResultSet rs = Database.executeSql(sqlCommand);

		while (rs.next()) {
			boardsListHTML += "<p><a href='/DistributedBoards/Boards?board=" + rs.getInt(1) + "'>" + rs.getString(2)
					+ "</a></p>";

			// Nach der Zentralen Anzeigetafeln wird ein Seperator eingefügt.
			if (rs.getInt(1) == 0) {
				boardsListHTML += "<hr noshade style=\"width:100%; height:0px; text-align:left; border:3px solid rgb(34, 34, 34);\">";
			}
		}

		// Suche alle Tafeln auf denen der Benutuzer Admin ist.
		sqlCommand = "SELECT ID,name FROM Boards WHERE Admin=" + userID + " ORDER BY ID ASC";
		rs = Database.executeSql(sqlCommand);

		while (rs.next()) {
			boardsListHTML += "<p><a href='/DistributedBoards/Boards?board=" + rs.getInt(1) + "'>" + rs.getString(2)
					+ "</a></p>";
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

	/**
	 * 
	 * @param marker Die Markierung an der der neue Inhalt eingefügt werden soll.
	 * @param page String der die HTML Seite darstellt.
	 * @return Ein Array mit den zwei Hälften.
	 */
	private static String[] splitStringPageAtMarker(String marker, String page) {

		// Index der Marke
		int index = page.indexOf(marker);
		String[] pageParts = new String[2];
		// Spalte die Seite in zwei Teile (alles vor und alles nach der Marke).
		pageParts[0] = page.substring(0, index);
		pageParts[1] = page.substring(index + marker.length());
		return pageParts;

	}

	/**
	 * 
	 * @return Die Loginseite, mit einer Fehleranzeige.
	 * @throws IOException
	 */
	public static InputStream buildFailedLogin() throws IOException {

		String[] page = splitHTMLPageAtMarker(loginFailedMarker, "WebContent\\HTML\\Login.html");
		String loginFailedAlert = "<div class='alert alert-danger' role='alert'>"
				+ "<strong>LOGIN FAILED!</strong></div>";
		String newPage = page[0] + loginFailedAlert + page[1];
		newPage = newPage.replace("hidden", "");

		return new ByteArrayInputStream(newPage.getBytes(charset));
	}

	/**
	 * 
	 * @param errorCode ErrorCode 0->kein Fehler, 1->Benutzername falsch, 2->Email falsch, 3->Email und Username falsch
	 * @return Die Registrierenseite mit einer Fehleranzeigen.
	 * @throws IOException
	 */
	public static InputStream buildFailedRegistration(int errorCode) throws IOException {

		// Remove the hidden attribute form the error display.
		String[] page = splitHTMLPageAtMarker("hidden", "WebContent\\HTML\\Registrieren.html");
		String newPage = page[0] + "" + page[1];
		
		return new ByteArrayInputStream(newPage.getBytes(charset));
	}
}
