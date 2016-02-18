package org.jschema.generators;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

public class JSONToJSchemaRunner
{
  public static void main( String[] args ) throws Exception
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/json_to_jschema.js"));
    Invocable invocable = (Invocable) engine;
    Object result = invocable.invokeFunction("jsonToJSchema", "{}");
    System.out.println(result);
  }
}
