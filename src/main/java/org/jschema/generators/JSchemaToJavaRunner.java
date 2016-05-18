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

  private String _testObj;

  public JSchemaToJavaRunner(String Obj){
    _testObj = Obj;
  }

  public static Object generateAll(String classname, String jschema) throws Exception{
    Object generatedAll = runEngine().invokeFunction("generateAll", classname, jschema);
    return generatedAll;
  }

  public static Object generateClass(String classname) throws Exception{
    Object generatedClass = runEngine().invokeFunction("generateClass", classname);
    return generatedClass;
  }

  public static Object generateField() throws Exception{
    Object generatedField = runEngine().invokeFunction("generateField");
    return generatedField;
  }

  public static Object generateConstructor(String jschema, String classname) throws Exception{
    Object generatedConstructor = runEngine().invokeFunction("generateConstructor", classname, jschema);
    return generatedConstructor;
  }

  public static Object generateGET(String jschema) throws Exception{
    Object generatedGET = runEngine().invokeFunction("generateGet", jschema);
    return generatedGET;
  }
  public static Object generateSET(String jschema) throws Exception{
    Object generatedSET = runEngine().invokeFunction("generateSet", jschema);
    return generatedSET;
  }




  public static void main( String[] args ) throws Exception
  {

    //opens a .jschema file, reads it into Stringbuilder builder, and converts to String str.
    //if you get a "no such file or directory" error, verify that the FileReader path is correct for you
    StringBuilder builder = new StringBuilder();
    String Line;
    BufferedReader schema = new BufferedReader( new FileReader("src/test/java/schemas/basic.jschema"));
    while((Line = schema.readLine()) != null){
      builder.append(Line);
    }
    String str = builder.toString();
    System.out.print(str);
  }

  private static Invocable runEngine() throws Exception{
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/jschema_to_java.js"));
    Invocable invocable = (Invocable) engine;
    return invocable;
  }
}