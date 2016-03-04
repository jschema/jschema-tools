package org.jschema.generators;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

public class JSONToJSchemaRunner
{
  public static void main( String[] args ) throws Exception {
    test("{\"string\":\"String\",\"int\":20,\"boolean\":true,\"date\":\"2016-02-25\",\"uri\":\"http://google.com\"}",
            "{\"string\":\"@string\",\"int\":\"@int\",\"boolean\":\"@boolean\",\"date\":\"@date\",\"uri\":\"@uri\"}");
    test("{ \"name\" : \"Joe\", \"age\" : 42 }",
            "{ \"name\" : \"@string\", \"age\" : \"@int\" }");
    test("[ { \"name\" : \"Joe\", \"age\" : 42 }, { \"name\" : \"Paul\", \"age\" : 28 }, { \"name\" : \"Mack\", \"age\" : 55 } ]",
            "[ { \"name\" : \"@string\", \"age\" : \"@int\"} ]");
  }

  private static Boolean test(String json, String expectedJschema) throws Exception {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/json_to_jschema.js"));
    Invocable invocable = (Invocable) engine;
    Boolean result = (Boolean)invocable.invokeFunction("testEquals", json, expectedJschema);
    if (result == false) {
      String formattedJSON = (String)invocable.invokeFunction("formatJSONString", json);
      String formattedExpected = (String)invocable.invokeFunction("formatJSONString", expectedJschema);
      String actual = (String)invocable.invokeFunction("jsonToJSchemaString", json);
      System.out.println("Test failed: " + formattedJSON + "\n\tExpected: " + formattedExpected + "\n\tActual: " + actual + "\n");
    }
    return result;
  }
}
