package org.jschema.endtoend;
import org.jschema.generated.java.*;

import junit.framework.Assert;
import org.jschema.generated.java.Invoice;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class EndToEndJavaTests
{
  @Test
  public void bootstrap(){
  }

  @Test
  public void BasicTest(){

  }

  @Test
  public void exampleSampleDataTest() throws IOException
  {

    Invoice2 i = Invoice2.parse( loadFile( "/samples/invoice-1.json" ) );
    Assert.assertEquals("1234", i.getId());
    Assert.assertEquals("joe@test.com", i.getEmail());
    Assert.assertEquals(105.00, i.getTotal());
    Assert.assertEquals("joe@test.com", i.getCustomer().getEmail());
    Assert.assertEquals("{last_name=Blow, first_name=Joe, email=joe@test.com}", i.getCustomer().toJSON());
    Assert.assertEquals("{zip=12345, country=USA, address1=123 Main Street, state=MS}", i.getTo_address().toJSON());
    Assert.assertEquals("{price=5.00, subtotal=50, count=10, description=The Best Darn Widgets Around, sku=S12T-Wid-GG}",i.getLine_items().get( 0 ).toJSON());
    Assert.assertEquals("S12T-Wid-GG", i.getLine_items().get( 0 ).getSku());

  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }
}
