package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class Test {
		

	  // This method is called if HTML is request
	  @GET
	  @Produces(MediaType.TEXT_HTML)
	  public String sayHtmlHello() throws IOException {
		 
		 return new String(Files.readAllBytes(Paths.get("C:\\Users\\Tristan Glaes\\Documents\\team5ds\\project3\\DistributedBoards\\WebContent\\Test\\test.html")));
	  }
	  // 2 + 2 is four Minus 1 is 3 QUICK MATHS -> Man is never hot skraaaa pop pop YOU DONE NOW!
	  @GET
	  @Path("ajaxTest")
	  @Produces(MediaType.TEXT_HTML)
	  public String AjaxTest() {
		  
		  return "test";
	  }
	  
}
