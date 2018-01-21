package routes;

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
	public InputStream returnResource(@QueryParam("resourceName") String name, @QueryParam("resourceType") String type) throws FileNotFoundException {
		switch(type) {
		
		case "css": return returnCSS(name); 
		case "img": return returnImage(name);
		case "javascript": return returnJavascript(name);
		default: return null;
		}
	}
	
	@Produces("text/css")
	private InputStream returnCSS(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\CSS\\" + resource)); 	
	}
	
	@Produces("image")
	private InputStream returnImage(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\Images\\" + resource)); 	
	}
	
	@Produces("text/javascript")
	private InputStream returnJavascript(String resource) throws FileNotFoundException {		
		return new FileInputStream(new File("WebContent\\Javascript\\" + resource)); 	
	}
	
}
