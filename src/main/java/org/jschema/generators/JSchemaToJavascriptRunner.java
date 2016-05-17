package org.jschema.generators;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSchemaToJavascriptRunner
{
  public static void main( String[] args ) throws Exception
  {
    String test1 = "{ \"name\" : \"@string\", \"age\" : \"@int\", \"birthday\" : \"@date\", \"website\" : \"@uri\", \"student\" : \"@boolean\", \"favorite_number\":\"@number\" }";
    String test2 = "[{ \"name\" : \"@string\", \"age\" : \"@int\"}]";
    String test8="{ \"name\" : \"@string\", \"age\" : \"@int\" }";
    String test3="{\"name\" : [\"@boolean\"]}";
    String test4="{\"name\" : [\"brown\",\"blue\",\"green\"]}";
    String test5="{\"info\" : {\"firstName\":\"@string\",\"lastName\":\"@string\"}}";
    String test7="{\"info\" : {\"name\":{\"first\":\"@string\",\"last\":\"@string\"}}}";
    String test6="{\"info\" : {\"firstName\":\"@string\"}}";
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval( new FileReader( "src/main/resources/js/jschema_to_javascript.js" ) );
    Invocable invocable = (Invocable) engine;
    Object result = invocable.invokeFunction("generateJavascriptForJSchema", test4, "Person");
    System.out.println(result);
  }

  public static Object generateAll( String fixedName, String jSchema ) throws ScriptException, NoSuchMethodException, FileNotFoundException
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval( new FileReader( "src/main/resources/js/jschema_to_javascript.js" ) );
    Invocable invocable = (Invocable) engine;
    return invocable.invokeFunction("generateJavascriptForJSchema", jSchema, fixedName);
  }

}
