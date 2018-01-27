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

/**
 * Die Klasse fügt die benutzerspezifischen Daten in die jeweilige Seite ein.
 * @author Tristan Glaes
 *
 */
public class HTMLBuilder {
	
	private static final String boardListMarker = "###BoardsList###";
	private static final String charset = StandardCharsets.UTF_8.name();
	
	/**
	 * Baut die HTML Seite für eine Benutzer zusammen indem die definierten Marken im Template mit
	 * HTML Code ersetzt wird.
	 * 
	 * @param userID Die ID des Benutzers der die Seite besucht.
	 * @return Einen InputStream, der die HTML Seite enthält.
	 * @throws IOException
	 * @throws SQLException
	 */
	public static InputStream buildBoardsPage(int userID) throws IOException, SQLException {
		
		// Wandle die Seite in einen String um.
		byte[] pageBytes = Files.readAllBytes(Paths.get("WebContent\\HTML\\Boards.html"));
		String pageTemplate = new String(pageBytes,charset);
		
		// Index der Marke
		int index = pageTemplate.indexOf(boardListMarker);
		// Spalte die Seite in zwei Teile (alles vor und alles nach der Marke).
		String newPage1 = pageTemplate.substring(0,index);
		String newPage3 = pageTemplate.substring(index + boardListMarker.length());
		// Generiere den HTML Inhalt für den Benutzer.
		String newPage2 = getBoardsListForUser(userID);
		// Baue die Seiten zusammen.
		String newPage = newPage1 + newPage2 + newPage3;
		
		return new ByteArrayInputStream(newPage.getBytes(charset));
	}

	/**
	 * 
	 * @param userID Die ID des Benutzers der die Seite besucht.
	 * @return Gibt die HTML Repräsentation der Tafel, die dem Benutzer zugeordnet sind, zurück.
	 * @throws SQLException
	 */
	private static String getBoardsListForUser(int userID) throws SQLException {
		
		String boardsListHTML = "";
		// Suche alle Tafeln auf denen der Benutzer ein normaler User ist.
		String sqlCommand = "SELECT ID,name FROM Boards as b,UserBoards AS ub WHERE ub.Board=b.ID AND ub.User=" + userID + " ORDER BY ID ASC";
		ResultSet rs = Database.executeSql(sqlCommand);
		while(rs.next()) {
			boardsListHTML += "<p><a href='Boards?board=" + rs.getInt(1) + "'>" + rs.getString(2) + "</a></p>";
			
			// Nach der Zentralen Anzeigetafeln wird ein Seperator eingefügt.
			if(rs.getInt(1) == 0) {
				boardsListHTML += "<hr noshade style=\"width:100%; height:0px; text-align:left; border:3px solid rgb(34, 34, 34);\">";
			}
		}
		
		// SUche alle Tafeln auf denen der Benutuzer Admin ist.
		sqlCommand = "SELECT ID,name FROM Boards WHERE Admin=" + userID + " ORDER BY ID ASC";
		rs = Database.executeSql(sqlCommand);
		while(rs.next()) {
			boardsListHTML += "<p><a href='Boards?board=" + rs.getInt(1) + "'>" + rs.getString(2) + "</a></p>";
		}
		// Schließen der Datenbankverbindung
		Database.closeConnection();
		
		return boardsListHTML;
	}	
}
