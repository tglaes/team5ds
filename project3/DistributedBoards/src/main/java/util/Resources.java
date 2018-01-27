package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/Resources/")
public class Resources {
	
	@GET
	public static InputStream getResource(@QueryParam("resourceName") String name, @QueryParam("resourceType") String type) throws FileNotFoundException {
		switch(type) {
		
		case "css": return getCSS(name); 
		case "img": return getImage(name);
		case "javascript": return getJavascript(name);
		case "html": return getHTML(name);
		default: return null;
		}
	}
	
	@Produces("text/css")
	private static InputStream getCSS(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\CSS\\" + resource));
		//return new FileInputStream(new File("WebContent/CSS/" + resource));
	}
	
	@Produces("image")
	private static InputStream getImage(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\Images\\" + resource));
		//return new FileInputStream(new File("WebContent/Images/" + resource));
	}
	
	@Produces("text/javascript")
	private static InputStream getJavascript(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\Javascript\\" + resource));
		//return new FileInputStream(new File("WebContent/JavaScript/" + resource));
	}
	
	@Produces("text/html")
	private static InputStream getHTML(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\HTML\\" + resource));
		//return new FileInputStream(new File("WebContent/HTML/" + resource));
	}
}