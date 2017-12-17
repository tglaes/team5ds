package test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/test")
public class Test {

	
	// This method is called if TEXT_PLAIN is request
	  @GET
	  @Produces(MediaType.TEXT_PLAIN)
	  public String sayPlainTextHello() {
	    return "Test";
	  }

	  // This method is called if XML is request
	  @GET
	  @Produces(MediaType.TEXT_XML)
	  public String sayXMLHello() {
	    return "<?xml version=\"1.0\"?>" + "<hello> Test" + "</hello>";
	  }

	  // This method is called if HTML is request
	  @GET
	  @Path("{id}")
	  @Produces(MediaType.TEXT_HTML)
	  public String sayHtmlHello(@PathParam("id") String id) {	  
	    return "<html> " + "<title>" + "Test" + "</title>"
	        + "<body><h1>" + "Die Id ist: " + id + "</body></h1>" + "</html> ";
	  }
}
