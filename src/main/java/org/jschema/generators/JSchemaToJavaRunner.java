package org.jschema.generators;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

public class JSchemaToJavaRunner
{
  public static void main( String[] args ) throws Exception
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/jschema_to_java.js"));
    Invocable invocable = (Invocable) engine;
    Object result = invocable.invokeFunction("generateJavaForJSchema", "{}", "foo.barl.Demo");
    System.out.println(result);
  }
}