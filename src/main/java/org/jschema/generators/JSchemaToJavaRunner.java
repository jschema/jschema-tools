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

  public static Object generateFields(String jschema) throws Exception{
    Object generatedFields = runEngine().invokeFunction("generateFields", jschema);
    return generatedFields;
  }

  public static Object generateObject(String jschema, String classname) throws Exception{
    Object generatedObject = runEngine().invokeFunction("generateObject", jschema, classname);
    return generatedObject;
  }

  public static Object generateGET(String jschema) throws Exception{
    Object generatedGET = runEngine().invokeFunction("generateGET", jschema);
    return generatedGET;
  }
  public static Object generateSET(String jschema) throws Exception{
    Object generatedSET = runEngine().invokeFunction("generateSET", jschema);
    return generatedSET;
  }

  public static Object generateError(String jschema) throws Exception{
    Object generatedError = runEngine().invokeFunction("generateError", jschema);
    return generatedError;
  }

  public static Object makeKeys(String jschema) throws Exception{
    Object makeKeys = runEngine().invokeFunction("makeKeys", jschema);
    return makeKeys;
  }
  public static Object makeValues(String jschema) throws Exception{
    Object makeValues = runEngine().invokeFunction("makeValues", jschema);
    return makeValues;
  }

  public static Object check_jschema_value(String jschema) throws Exception{
    Object checked_value = runEngine().invokeFunction("jschema_parser", jschema);
    return checked_value;
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

    Object obj = generateFields(str);
    System.out.print(obj);
  }

  private static Invocable runEngine() throws Exception{
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/jschema_to_java.js"));
    Invocable invocable = (Invocable) engine;
    return invocable;
  }
}