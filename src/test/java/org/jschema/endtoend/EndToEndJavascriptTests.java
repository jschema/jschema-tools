package org.jschema.endtoend;

import org.jschema.generators.RunGenerators;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EndToEndJavascriptTests
{
  ScriptEngine _engine = null;
  @Before
  public void setEngine() {
     _engine = new ScriptEngineManager().getEngineByName("nashorn");
  }

  public void load( String file ) {
    try
    {
      _engine.eval( new FileReader( file ) );

    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public Object eval( String expr ) {
    try
    {
      return _engine.eval( expr );
    }
    catch( ScriptException e )
    {
      System.out.println("eval error " +e.getMessage());
      throw new RuntimeException( e );
    }
  }

  @Test
  public void bootstrap()
  {
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Contact.js" );
    //System.out.println(eval("p"));
    eval("var p=Contact.parse(\"{\\\"first_name\\\" : \\\"@string\\\"," +
            "\\\"last_name\\\" : \\\"@string\\\",\\\"age\\\" : \\\"@int\\\"," +
            "\\\"type\\\" : [\\\"friend\\\", \\\"customer\\\", \\\"supplier\\\"]," +
            "\\\"emails\\\" : [\\\"@string\\\"]}\");");
    Assert.assertEquals( "@string", eval( "p.first_name" ) );
    eval("p.first_name=\"jane\"");
    Assert.assertEquals( "jane", eval( "p.first_name" ) );
  }

  @Test
  public void coreTest() throws IOException {
    String contact1 = jsonString( loadFile("/samples/contact.json") );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Contact.js" );
    eval("var p=Contact.parse(\"" + contact1  + "\");");
    Assert.assertEquals( "jane", eval( "p[0].first_name" ) );
    //Check Valid Int
    eval("p[0].age=\"test\";");
    //Check Invalid Int
    Assert.assertEquals("age=test does not conform to @int\n",eval("p.validate()"));
    //Check Valid Int
    eval("p[0].age=40;");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check Invalid enum
    eval("p[0].type=\"enemy\";");
    Assert.assertEquals("type =enemy does not conform to [friend,customer,supplier]\n",eval("p.validate()"));
    //Check Valid enum
    eval("p[0].type=\"supplier\";");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check Invalid Array
    eval("p[0].info.emails[0]=5");
    eval("p[0].info.emails[1]=\"test\"");
    Assert.assertEquals("emails =[5,test] does not conform to [@string]\n",eval("p.validate()"));
    //Check Valid Array
    eval("p[0].info.emails[0]=\"fixed\"");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check JSON is updated
    Assert.assertEquals("{\"0\":{\"first_name\":\"jane\",\"age\":40," +
            "\"type\":\"supplier\",\"info\":{\"emails\":[\"fixed\",\"test\"]," +
            "\"phone_number\":{\"home\":1234567,\"cell\":1234567}," +
            "\"addresses\":[{\"address\":\"123 test street, ca\"}]}}," +
            "\"1\":{\"first_name\":\"jane\",\"age\":20,\"type\":\"customer\"," +
            "\"info\":{\"emails\":[\"jane@test.com\"],\"phone_number\":{\"home\":1234567," +
            "\"cell\":1234567},\"addresses\":[{\"address\":\"123 test street, ca\"}]}}," +
            "\"2\":{\"first_name\":\"jane\",\"age\":20,\"type\":\"supplier\"," +
            "\"info\":{\"emails\":[\"jane@test.com\"],\"phone_number\":{\"home\":1234567," +
            "\"cell\":1234567},\"addresses\":[{\"address\":\"123 test street, " +
            "ca\"}]}}}",eval("p.toJSON()"));

  }

  @Test
  public void exampleSampleDataTest() throws IOException
  {
    String invoice1 = jsonString( loadFile("/samples/invoice1.json") );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Invoice1.js" );
    eval("var inv=Invoice1.parse(\"" + invoice1  + "\");");
    //check basic JSON correctly parsed
    Assert.assertEquals("1234",eval("inv.id"));
    Assert.assertEquals(100,eval("inv.subtotal"));
    //Check field is set
    eval("inv.subtotal=200.00");
    Assert.assertEquals(200.00,eval("inv.subtotal"));
    //Check JSON Object correctly parsed
    Assert.assertEquals("joe@test.com",eval("inv.customer.email"));
    //Check field is set
    Assert.assertEquals("123 Main Stree",eval("inv.to_address.address1"));
    eval("inv.to_address.address1=\"1234 Mulberry Street\"");
    Assert.assertEquals("1234 Mulberry Street",eval("inv.to_address.address1"));
    //Check JSON array of objects correctly parsed
    Assert.assertEquals("S12T-Wid-GG",eval("inv.line_items[0].sku"));
    //Check field is set
    Assert.assertEquals("F34JJ-Wid-HH",eval("inv.line_items[1].sku"));
    eval("inv.line_items[1].sku=\"12345-abc-de\"");
    Assert.assertEquals("12345-abc-de",eval("inv.line_items[1].sku"));
    Assert.assertEquals("Valid",eval("inv.validate()"));
    String invoice2 = jsonString( loadFile("/samples/invoice2.json") );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Invoice1.js" );
    eval("var inv2=Invoice1.parse(\"" + invoice2  + "\");");
    //check basic JSON correctly parsed
    Assert.assertEquals("2",eval("inv2.id"));
    //call without strict flag
    Assert.assertEquals("Valid",eval("inv2.validate()"));
    //call with strict flag
    Assert.assertEquals("created_at=undefined does not conform to @date\n" +
            "updated_at=undefined does not conform to @date\n" +
            "subtotal=undefined does not conform to @int\n" +
            "tax=undefined does not conform to @int\n" +
            "notes=undefined does not conform to @string\n" +
            "customer =undefined does not conform to [object Object]\n" +
            "to_address =undefined does not conform to [object Object]\n" +
            "line_items=undefined does not conform to [array]\n",eval("inv2.validate(1)"));
    eval("inv2.created_at=\"2013-05-14T16:09:35-04:00\"");
    eval("inv2.updated_at=\"2013-05-14T16:09:35-04:00\"");
    //call with strict flag
    Assert.assertEquals("subtotal=undefined does not conform to @int\n" +
            "tax=undefined does not conform to @int\n" +
            "notes=undefined does not conform to @string\n" +
            "customer =undefined does not conform to [object Object]\n" +
            "to_address =undefined does not conform to [object Object]\n" +
            "line_items=undefined does not conform to [array]\n",eval("inv2.validate(1)"));
    eval("inv2.subtotal=200.00");
    eval("inv2.tax=10");
    eval("inv2.notes=\"none\"");
    eval("inv2.customer={}");
    eval("inv2.customer.email=\"jane@test.com\"");
    eval("inv2.customer.first_name=\"jane\"");
    eval("inv2.customer.last_name=\"doe\"");
    //call with strict flag
    Assert.assertEquals("to_address =undefined does not conform to [object Object]\n" +
            "line_items=undefined does not conform to [array]\n",eval("inv2.validate(1)"));
    //call without strict flag
    Assert.assertEquals("Valid",eval("inv2.validate(0)"));
    eval("inv2.to_address={\"address\":\"test\",\"zip\":\"12345\",\"state\":\"ca\",\"country\":\"USA\"}");
    eval("inv2.line_items=[{\"sku\":\"test\",\"description\":\"nothing\",\"count\":10,\"price\":5.0,\"subtotal\":100}]");
    //call with strict flag
    Assert.assertEquals("Valid",eval("inv2.validate(1)"));
    //call without strict flag
    Assert.assertEquals("Valid",eval("inv2.validate(0)"));
    //test tojson method
    Assert.assertEquals("{\"id\":\"2\",\"email\":\"joe@test.com\"," +
            "\"total\":15,\"created_at\":\"2013-05-14T16:09:35-04:00\"," +
            "\"updated_at\":\"2013-05-14T16:09:35-04:00\",\"subtotal\":200," +
            "\"tax\":10,\"notes\":\"none\",\"customer\":{\"email\":\"jane@test.com\"," +
            "\"first_name\":\"jane\",\"last_name\":\"doe\"}," +
            "\"to_address\":{\"address\":\"test\",\"zip\":\"12345\",\"state\":\"ca\"," +
            "\"country\":\"USA\"},\"line_items\":[{\"sku\":\"test\",\"description\":\"nothing\"," +
            "\"count\":10,\"price\":5,\"subtotal\":100}]}",eval("inv2.toJSON()"));
  }

  private String jsonString( String s )
  {
    ////System.out.println(s);
    String temp=s.replace( "\"", "\\\"" );
    temp=temp.replace(System.getProperty("line.separator"), " ");
    return temp;
  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }
  @Test
  public void testNestedArrays() throws IOException{
    String list1 = jsonString( loadFile("/samples/shoppinglist1.json") );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Shoppinglist1.js" );
    eval("var list=Shoppinglist1.parse(\"" + list1  + "\");");
    //check basic JSON correctly parsed
    Assert.assertEquals("Costco",eval("list.storeName"));
    Assert.assertEquals("apples",eval("list.itemsToBuy[0][0]"));
    //Check field is set
    eval("list.itemsToBuy[0]=[\"peaches\",\"pears\"]");
    Assert.assertEquals("peaches",eval("list.itemsToBuy[0][0]"));
    Assert.assertEquals("pears",eval("list.itemsToBuy[0][1]"));
    //Check invalid state
    eval("list.itemsToBuy[0]=[\"pears\",5]");
    Assert.assertEquals("itemsToBuy =[pears,5] does not conform to [@string]\n",eval("list.validate()"));
    eval("list.itemsToBuy=[[\"pears\"],5]");
    Assert.assertEquals("itemsToBuy=pears does not conform to [array]\n",eval("list.validate()"));
    //Check valid state
    eval("list.itemsToBuy=[[\"pears\"]]");
    Assert.assertEquals("Valid",eval("list.validate()"));
  }
  @Test
  public void nestedObjectTest() throws IOException{
    String person = jsonString( loadFile("/samples/person.json") );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Person.js" );
    eval("var p=Person.parse(\"" + person  + "\");");
    //Check Valid String
    Assert.assertEquals("jane",eval("p.first_name"));
    //Check field is set
    eval("p.age=21");
    Assert.assertEquals(21,eval("p.age"));
    //Check Invalid String
    eval("p.hobbies.sports=5");
    Assert.assertEquals("hobbies =[object Object],[object Object] does not conform to [[object Object]]\n",eval("p.validate()"));
    //Check Valid Name
    eval("p.hobbies=[{\"sports\":[\"basketball\"],\"crafts\":\"painting\"}]");
    Assert.assertEquals("Valid",eval("p.validate()"));
    Assert.assertEquals("jane@test.com",eval("p.info.email[0]"));
    //append bad value to array
    eval("p.info.email[1]=5");
    Assert.assertEquals("email =[jane@test.com,5] does not conform to [@string]\n",eval("p.validate()"));
    //append to array
    eval("p.info.email[1]=\"jane2@test.com\"");
    Assert.assertEquals("jane2@test.com",eval("p.info.email[1]"));
    //append object to array
    eval("p.info.addresses[1]={\"address\":\"12345 Santa Cruz, CA\"}");
    Assert.assertEquals("123 test street, ca",eval("p.info.addresses[0].address"));
    Assert.assertEquals("12345 Santa Cruz, CA",eval("p.info.addresses[1].address"));
    //append bad val
    eval("p.info.addresses[1]={\"address\":5}");
    Assert.assertEquals("address=5 does not conform to @string\n",eval("p.validate()"));
    eval("p.info.addresses[1]=\"12345 Santa Cruz, CA\"");
    Assert.assertEquals("addresses =[object Object],12345 Santa Cruz, CA does not conform to [[object Object]]\n",eval("p.validate()"));
    eval("p.info.addresses[1]={\"address\":\"12345 Santa Cruz, CA\"}");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //check strict validation
    eval("p.favorite_food=\"pizza\"");
    Assert.assertEquals("Valid",eval("p.validate(true)"));
  }

  @Test
  public void testMultipleNest() throws IOException{

    String cars = jsonString( loadFile("/samples/cars.json") );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Cars.js" );
    eval("var car=Cars.parse(\"" + cars  + "\");");
    //Check Valid String
    Assert.assertEquals("Tesla",eval("car[0].make"));
    //Check Valid Nesting
    Assert.assertEquals(false,eval("car[0].interior.packages.base.heated_seats"));
    Assert.assertEquals(true,eval("car[0].interior.packages.luxury.heated_seats"));
    //check Invalid Value
    eval("car[0].interior.packages.base.heated_seats=5");
    Assert.assertEquals("heated_seats=5 does not conform to @boolean\n",eval("car.validate()"));
    eval("car[0].interior.packages.base.heated_seats=true");
    Assert.assertEquals("Valid",eval("car.validate()"));
    //check [*]
    Assert.assertEquals("blind_spot_assist",eval("car[1].accessories[0]"));
    //check invalid for *
    eval("car[1].accessories[0]=true");
    Assert.assertEquals("accessories =[true,aux,radio,bluetooth,navigation] does not conform to [*]\n",eval("car.validate()"));
    eval("car[1].accessories[0]=\"dvd_player\"");
    //check strict flag
    eval("car[1].sunroof=true");
    Assert.assertEquals("Valid",eval("car.validate()"));
    Assert.assertEquals("",eval("car.validate(true)"));
  }
}
