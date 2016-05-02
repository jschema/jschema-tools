package org.jschema.endtoend;

import org.jschema.generators.RunGenerators;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
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
    String contact1 = jsonString( loadFile( "/samples/contact-1.json" ) );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Contact.js" );
    eval("var p=Contact.parse(\"" + contact1  + "\");");
    Assert.assertEquals( "jane", eval( "p.first_name" ) );
    //Check Valid String
    eval("p.first_name=\"joe\"");
    //Check field is set
    Assert.assertEquals( "joe", eval( "p.first_name" ) );
    eval("p.type=\"supplier\";");
    //Check Valid Int
    eval("p.age=\"test\";");
    //Check Invalid Int
    Assert.assertEquals("age=test does not conform to @int\n",eval("p.validate()"));
    //Check Valid Int
    eval("p.age=40;");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check Invalid enum
    eval("p.type=\"enemy\";");
    Assert.assertEquals("type =enemy does not conform to [friend,customer,supplier]\n",eval("p.validate()"));
    //Check Valid enum
    eval("p.type=\"supplier\";");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check Invalid Array
    eval("p.info.emails[0]=5");
    eval("p.info.emails[1]=\"test\"");
    Assert.assertEquals("emails =[5,test] does not conform to [@string]\n",eval("p.validate()"));
    //Check Valid Array
    eval("p.info.emails[0]=\"fixed\"");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check JSON is updated
    Assert.assertEquals("{\"first_name\":\"joe\"," +
            "\"age\":40," +
            "\"type\":\"supplier\"," +
            "\"info\":{\"emails\":[\"fixed\",\"test\"]," +
                    "\"phone_number\":{\"home\":1234567,\"cell\":1234567},"+
                  "\"addresses\":[{\"address\":\"123 test street, ca\"}]" +
                    "}}",eval("p.toJSON()"));

  }

  @Test
  public void exampleSampleDataTest() throws IOException
  {
    String invoice1 = jsonString( loadFile( "/samples/invoice-1.json" ) );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Invoice.js" );
    eval("var inv=Invoice.parse(\"" + invoice1  + "\");");
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
    String invoice2 = jsonString( loadFile( "/samples/invoice-2.json" ) );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Invoice.js" );
    eval("var inv2=Invoice.parse(\"" + invoice2  + "\");");
    //check basic JSON correctly parsed
    Assert.assertEquals("2",eval("inv2.validate()"));



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
    String list1 = jsonString( loadFile( "/samples/shoppinglist-1.json" ) );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Shoppinglist.js" );
    eval("var list=Shoppinglist.parse(\"" + list1  + "\");");
    //check basic JSON correctly parsed
    Assert.assertEquals("Costco",eval("list.storeName"));
    Assert.assertEquals("apples",eval("list.itemsToBuy[0][0]"));
    //Check field is set
    eval("list.itemsToBuy[0]=[\"peaches\",\"pears\"]");
    Assert.assertEquals("peaches",eval("list.itemsToBuy[0][0]"));
    Assert.assertEquals("pears",eval("list.itemsToBuy[0][1]"));
    //Check invalid state
    eval("list.itemsToBuy[0]=\"pears\"");
    Assert.assertEquals("itemsToBuy=pears,forks,knives,spoons,hot sauce,bbq sauce,rance does not conform to [array]\n",eval("list.validate()"));
    eval("list.itemsToBuy[0]=[\"pears\",5]");
    Assert.assertEquals("itemsToBuy =[pears,5] does not conform to [@string]\n",eval("list.validate()"));
    eval("list.itemsToBuy=[[\"pears\"],5]");
    Assert.assertEquals("itemsToBuy=pears does not conform to [array]\n",eval("list.validate()"));
    //Check valid state
    eval("list.itemsToBuy=[[\"pears\"]]");
    Assert.assertEquals("Valid",eval("list.validate()"));
  }
  @Test
  public void nestedObjectTest(){
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Person.js" );
    eval ("var obj=Person.parse(\"{ \\\"name\\\" : \\\"@string\\\", \\\"age\\\" : {\\\"month\\\" : \\\"@string\\\", \\\"day\\\" : \\\"@int\\\", \\\"year\\\" : {\\\"decade\\\":\\\"@int\\\"}}}\")");
    Assert.assertEquals("{\"name\":\"@string\",\"age\":{\"month\":\"@string\",\"day\":\"@int\",\"year\":{\"decade\":\"@int\"}}}",eval("obj.toJSON()"));
    //Check Valid String
    Assert.assertEquals("@string",eval("obj.age.month"));
    //Check field is set
    eval("obj.age.month=\"April\"");
    Assert.assertEquals("April",eval("obj.age.month"));
    //Check JSON is updated
    Assert.assertEquals("{\"name\":\"@string\",\"age\":{\"month\":\"April\",\"day\":\"@int\",\"year\":{\"decade\":\"@int\"}}}",eval("obj.toJSON()"));
    //Check Invalid String
    eval("obj.age.day=7");
    eval("obj.age.year.decade=2015");
    eval("obj.name=4");
    Assert.assertEquals(4,eval("obj.name"));
    Assert.assertEquals("name=4 does not conform to @string\n",eval("obj.validate()"));
    //Check Valid Name
    eval("obj.name=\"jane\"");
    Assert.assertEquals("Valid",eval("obj.validate()"));
    //Check Invalid Decade
    eval("obj.age.year.decade=\"bad\"");
    Assert.assertEquals("decade=bad does not conform to @int\n",eval("obj.validate()"));
    //Check Invalid Month
    eval("obj.age.month=5");
    Assert.assertEquals("month=5 does not conform to @string\ndecade=bad does not conform to @int\n",eval("obj.validate()"));
    //Check Valid Month and Decade
    eval("obj.age.month=\"June\"");
    eval("obj.age.year.decade=2015");
    Assert.assertEquals("Valid",eval("obj.validate()"));
  }

}
