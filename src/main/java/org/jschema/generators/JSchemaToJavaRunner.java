package org.jschema.generators;

import org.jschema.parser.Parser;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class JSchemaToJavaRunner
{
  public static void main( String[] args ) throws Exception
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/jschema_to_java.js"));
    Invocable invocable = (Invocable) engine;

    //opens a .jschema file, reads it into Stringbuilder builder, and converts to String str.
    //if you get a "no such file or directory" error, verify that the FileReader path is correct for you
    StringBuilder builder = new StringBuilder();
    String Line;
    BufferedReader schema = new BufferedReader( new FileReader("src/test/java/schemas/contact.jschema"));
    while((Line = schema.readLine()) != null){
      builder.append(Line);
    }
    String str = builder.toString();

    //str is parsed into the approprite object
    Object parsed_schema = parse(str);

    //pass the parsed jschema into a javascript function. Currently only returns itself.
    Object attempt = invocable.invokeFunction("generateObject", parsed_schema, "Temporary Classname");
    System.out.print(attempt);
  }

  private static Object parse( String src )
  {
    return new Parser( src ).parse();
  }
}