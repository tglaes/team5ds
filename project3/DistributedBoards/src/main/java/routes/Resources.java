package routes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * 
 * @author Tristan Glaes
 * @version 09.03.2018
 */
@Path("/Resources/")
public class Resources {

	/**
	 * 
	 * @param name
	 *            Name der Resource
	 * @param type
	 *            Typ der Resource
	 * @return Die Resource
	 * @throws FileNotFoundException
	 */
	@GET
	public static InputStream getResource(@QueryParam("resourceName") String name,
			@QueryParam("resourceType") String type) throws FileNotFoundException {
		switch (type) {

		case "css":
			return getCSS(name);
		case "img":
			return getImage(name);
		case "js":
			return getJavascript(name);
		case "html":
			return getHTML(name);
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param resource
	 *            Name der Resource
	 * @return Die Resource
	 * @throws FileNotFoundException
	 */
	@Produces("text/css")
	private static InputStream getCSS(String resource) throws FileNotFoundException {
		return new FileInputStream(new File("WebContent\\CSS\\" + resource));
		// return new FileInputStream(new File("WebContent/CSS/" + resource));
	}

	/**
	 * 
	 * @param resource
	 *            Name der Resource
	 * @return Die Resource
	 * @throws FileNotFoundException
	 */
	@Produces("image")
	private static InputStream getImage(String resource) throws FileNotFoundException {
		return new FileInputStream(new File("WebContent\\Images\\" + resource));
		// return new FileInputStream(new File("WebContent/Images/" + resource));
	}

	/**
	 * 
	 * @param resource
	 *            Name der Resource
	 * @return Die Resource
	 * @throws FileNotFoundException
	 */
	@Produces("text/javascript")
	private static InputStream getJavascript(String resource) throws FileNotFoundException {
		return new FileInputStream(new File("WebContent\\Javascript\\" + resource));
		// return new FileInputStream(new File("WebContent/JavaScript/" + resource));
	}

	/**
	 * 
	 * @param resource
	 *            Name der Resource
	 * @return Die Resource
	 * @throws FileNotFoundException
	 */
	@Produces("text/html")
	private static InputStream getHTML(String resource) throws FileNotFoundException {
		return new FileInputStream(new File("WebContent\\HTML\\" + resource));
		// return new FileInputStream(new File("WebContent/HTML/" + resource));
	}
}