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
//TODO  If in the .jschema we have an @int ... we accept as valid something like "11" in that field, instead of just 11 on the JSON, and just parse it as if it were ok
//It is saved as a string, that is why we have errors later
    Invoice2 i = Invoice2.parse( loadFile( "/samples/invoice-1.json" ) );
    Assert.assertEquals("1234", i.getId());
    Assert.assertEquals("joe@test.com", i.getEmail());
    Assert.assertEquals(105.00, i.getTotal());
    Assert.assertEquals("joe@test.com", i.getCustomer().getEmail());
    Assert.assertEquals("{last_name=Blow, first_name=Joe, email=joe@test.com}", i.getCustomer().toJSON());
    Assert.assertEquals("{zip=12345, country=USA, address1=123 Main Street, state=MS}", i.getTo_address().toJSON());
    Assert.assertEquals("{price=5.00, subtotal=50, count=10, description=The Best Darn Widgets Around, sku=S12T-Wid-GG}",i.getLine_items().get( 0 ).toJSON());
    Assert.assertEquals("S12T-Wid-GG", i.getLine_items().get( 0 ).getSku());
    Assert.assertEquals(10, i.getLine_items().get(1).getCount());

/*  //FAILING________________________________________
    EdgeCases_1 i2 = EdgeCases_1.parse( loadFile( "/samples/edgeCases_1.json" ) );
    Assert.assertEquals("1111",i2.getObj_1().getId_1());
    Assert.assertEquals("2222",i2.getObj_1().getObj_2().getId_2());
    Assert.assertEquals("3333",i2.getObj_1().getObj_2().getObj_3().getId_3());
*/

/*
    Basic i3 = Basic.parse((loadFile("/samples/basic.json" ) ) );
    Assert.assertEquals(("Nick"), i3.getName());
    Assert.assertEquals((100), i3.getAge());
    i3.setAge(50);
    Assert.assertEquals((50), i3.getAge());
*/

/*
    //some changes required
    Contact i4 = Contact.parse((loadFile("/samples/contract.json" ) ) );
    Assert.assertEquals("John", i4.getFirst_name());
    Assert.assertEquals("Smith", i4.getLast_name());
    Assert.assertEquals(34, i4.getAge());
    Assert.assertEquals(Contact.Type.FRIEND, i4.getType());
    Assert.assertEquals("email_ONE@mail.com", i4.getEmails().get(0));
    Assert.assertEquals("email_TWO@mail.com", i4.getEmails().get(1));
    Assert.assertEquals("John Smith CUSTOMER", i4.getCustomer().getName());
*/
      //edgeCases_2 i5 = edgeCases_2.parse( loadFile( "/samples/edgeCases_2.json" ) );




  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }
}
