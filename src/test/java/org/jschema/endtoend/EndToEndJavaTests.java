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
    Assert.assertEquals(29, (int) b.getAge());
    b.setAge(30);
    Assert.assertEquals(30,  (int)b.getAge());

  }

  @Test
  public void BasicArrayTest() throws IOException{
    BasicArray b = BasicArray.parse(loadFile("/samples/basicArray.json"));
    Assert.assertEquals("Bill", b.getName(0));
    b.setName(1, "NOT Charlie");
    Assert.assertEquals("NOT Charlie", b.getName(1));
  }

  @Test
  public void NestedArrayTest() throws IOException{
    NestedArray n = NestedArray.parse(loadFile("/samples/NestedArray.json"));
    Assert.assertEquals("Michael", n.getName(0));
    Assert.assertEquals(214, (int) n.getAddress(0).getNumber());
    Assert.assertEquals("Peach St.", n.getAddress(1).getStreet());
    n.getAddress(1).setNumber(8000);
    Assert.assertEquals(8000, (int) n.getAddress(1).getNumber());
  }

  @Test
  public void ContactTest() throws IOException{

    Contact c = Contact.parse(loadFile("/samples/Contact.json"));
    Assert.assertEquals("Bill", c.getFirst_name());
    Assert.assertEquals("[friend, customer, supplier]", c.getType());
    Assert.assertEquals("Bill@2.com", c.getEmails().get(1));
    Assert.assertEquals("NOTBill", c.getCustomer().getName());

  }

  @Test
  public void NestTest() throws IOException{

    Nest n = Nest.parse(loadFile("/samples/Nest.json"));
    Assert.assertEquals("College Park High", n.getName());
    Assert.assertEquals("Pleasant Hill", n.getSchool().getCity());
    Assert.assertEquals("High School", n.getStudents().getType());
    Assert.assertEquals(1, (int) n.getStudents().getStudent_Facts().getLevel().getFreshmen());

  }

  @Test
  public void listsTest() throws IOException{

    Lists l = (Lists) Lists.parse(loadFile("/samples/lists.json"));
    Assert.assertEquals("father", l.getFamily().get(0));
    Assert.assertEquals("sucks", l.getScience().get(0).getProgramming().getJava());
    Assert.assertEquals("Statistical",l.getScience().get(0).getPhysics().getMechanics().get(1));
    Assert.assertEquals(105, (int) l.getScience().get(0).getMath().getCalculus());

  }
  @Test
  public void Invoice2Test() throws IOException
  {

    Invoice2 i = Invoice2.parse( loadFile( "/samples/invoice-1.json" ) );
    Assert.assertEquals("1234", i.getId());
    Assert.assertEquals("joe@test.com", i.getEmail());
    Assert.assertEquals(105.00, i.getTotal());
    Assert.assertEquals("joe@test.com", i.getCustomer().getEmail());
    Assert.assertEquals("S12T-Wid-GG", i.getLine_items().get( 0 ).getSku());
    Assert.assertEquals("fred", i.getList().get(0));
    Assert.assertEquals("PLEASE", i.getNest().getNonNested());
    Assert.assertEquals(16, (int) i.getNest().getNested().getInnerVal());

  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }


}
