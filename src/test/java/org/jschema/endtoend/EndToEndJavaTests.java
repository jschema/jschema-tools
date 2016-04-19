package org.jschema.endtoend;
//import org.jschema.generated.java.*;

import junit.framework.Assert;
import org.jschema.generated.java.Invoice;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EndToEndJavaTests
{
  @Test
  public void bootstrap(){
  }

  @Test
  public void exampleSampleDataTest() throws IOException
  {
    Invoice i = Invoice.parse( loadFile( "/samples/invoice-1.json" ) );
    Assert.assertEquals("1234", i.getId());
    Assert.assertEquals("S12T-Wid-GG", i.getLineItems().get( 0 ).getSku());
  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }
}
