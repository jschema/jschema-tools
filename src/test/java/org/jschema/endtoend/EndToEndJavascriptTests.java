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
  public void coreTest(){
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Contact.js" );
    eval("var p=Contact.parse(\"{\\\"first_name\\\" : \\\"@string\\\"," +
            "\\\"last_name\\\" : \\\"@string\\\",\\\"age\\\" : \\\"@int\\\"," +
            "\\\"type\\\" : [\\\"friend\\\", \\\"customer\\\", \\\"supplier\\\"]," +
            "\\\"emails\\\" : [\\\"@string\\\"]}\");");
    Assert.assertEquals( "@string", eval( "p.first_name" ) );
    //Check Valid String
    eval("p.first_name=\"jane\"");
    //Check field is set
    Assert.assertEquals( "jane", eval( "p.first_name" ) );
    eval("p.last_name=\"doe\"");
    //Check JSON is updated
    Assert.assertEquals("{\"first_name\":\"jane\"," +
            "\"last_name\":\"doe\",\"age\":\"@int\"," +
            "\"type\":[\"friend\",\"customer\",\"supplier\"]," +
            "\"emails\":[\"@string\"]}",eval("p.toJSON()"));
    eval("p.type=\"friend\";");
    //Check Invalid Int
    Assert.assertEquals("age=@int does not conform to @int\n",eval("p.validate()"));
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
    eval("p.emails[0]=\"test\"");
    eval("p.emails[1]=5");
    Assert.assertEquals("emails =[test,5] does not conform to [@string]\n",eval("p.validate()"));
    //Check Valid Array
    eval("p.emails[1]=\"fixed\"");
    Assert.assertEquals("Valid",eval("p.validate()"));
    //Check JSON is updated
    Assert.assertEquals("{\"first_name\":\"jane\"," +
            "\"last_name\":\"doe\",\"age\":40," +
            "\"type\":\"supplier\"," +
            "\"emails\":[\"test\",\"fixed\"]}",eval("p.toJSON()"));

  }

  @Test
  public void exampleSampleDataTest() throws IOException
  {
    String invoice1 = jsonString( loadFile( "/samples/invoice-1.json" ) );
    load( RunGenerators.JAVASCRIPT_GENERATED_DIR + "/Invoice.js" );
    eval("var inv=Invoice.parse(" + invoice1  + ");");
    //TODO make it work...
  }

  private String jsonString( String s )
  {
    return "\"" + s.replace( "\"", "\\\"" ) + "\"";
  }

  private String loadFile( String path ) throws IOException
  {
    File resource = new File("src/test/java" + path );
    return new String( Files.readAllBytes( Paths.get( resource.getPath() ) ) );
  }

}
