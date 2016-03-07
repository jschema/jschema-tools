package org.jschema.generators;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

public class JSchemaToJavascriptRunner
{
  public static void main( String[] args ) throws Exception
  {
    String test1 = "{ \"name\" : \"@string\", \"age\" : \"@int\", \"birthday\" : \"@date\", \"website\" : \"@uri\", \"student\" : \"@boolean\", \"favorite_number\":\"@number\" }";
    String test2 = "{ \"name\" : \"@string\", \"age\" : \"@int\"}";
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/jschema_to_javascript.js"));
    Invocable invocable = (Invocable) engine;
    Object result = invocable.invokeFunction("generateJavascriptForJSchema", test2, "Person");
    System.out.println(result);
  }
}
