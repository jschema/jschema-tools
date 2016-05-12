package org.jschema.endtoend;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.jschema.generated.java.*;

import junit.framework.Assert;
import org.jschema.generated.java.Invoice;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class EndToEndJavaTests
{

  @Test
  public void BasicTest() throws IOException{

    Basic b = Basic.parse(loadFile("/samples/basic.json"));
    Assert.assertEquals("Bill", b.getName());
    Assert.assertEquals(29, b.getAge());

    b.setAge(30);
    Assert.assertEquals(30, b.getAge());

  }

  @Test
  public void ContactTest() throws IOException{

    Contact c = Contact.parse(loadFile("/samples/Contact.json"));
    Assert.assertEquals("Bill", c.getFirst_name());
    Assert.assertEquals("Bill@2.com", c.getEmails().get(1));
    //System.out.print(c.toJSON());
    Assert.assertEquals( "FRIEND" , c.getType());
    Assert.assertEquals("{name=NOTBill, age=31}", c.getCustomer().toJSON());
    Assert.assertEquals("NOTBill", c.getCustomer().getName());

  }

  @Test
  public void NestTest() throws IOException{

    Nest n = (Nest) Nest.parse(loadFile("/samples/Nest.json"));
    Assert.assertEquals("College Park High", n.getName());
    Assert.assertEquals("Pleasant Hill", n.getSchool().getCity());
    Assert.assertEquals("High School", n.getStudents().getType());
    //Assert.assertEquals("Freshmen", n.getStudents().getStudent_Facts().getLevel());

  }


  @Test
  public void Invoice2Test() throws IOException
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
    Assert.assertEquals("fred", i.getList().get(0));
    Assert.assertEquals("PLEASE", i.getNest().getNonNested());
    Assert.assertEquals(16, i.getNest().getNested().getInnerVal());

  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }


}
