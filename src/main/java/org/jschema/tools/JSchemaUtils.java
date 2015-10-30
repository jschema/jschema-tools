package org.jschema.tools;


import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Source;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class JSchemaUtils
{

  public static Object evalJS( String script )
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    try
    {
      return engine.eval(script);
    }
    catch( ScriptException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static Node parseJSON(String json)
  {
    JSONParser jp = new JSONParser( Source.sourceFor( "anonymous", json ),
                                    new Context.ThrowErrorManager());
    return jp.parse();
  }

  public static String convertJSONToJSchema(String json)
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    try
    {
      engine.eval( new InputStreamReader( JSchemaUtils.class.getResourceAsStream( "/js/json_to_jschema.js" )) );
      Invocable invocable = (Invocable) engine;
      return invocable.invokeFunction( "jsonToJSchema", evalJS( json ) ).toString();
    }
    catch( ScriptException | NoSuchMethodException e )
    {
      throw new RuntimeException( e );
    }
  }

}
