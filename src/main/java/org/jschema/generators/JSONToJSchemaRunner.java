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
            "{\"string\":\"@string\",\"int\":\"@int\",\"boolean\":\"@boolean\",\"date\":\"@date\",\"uri\":\"@uri\"}", false);
    test("{ \"name\" : \"Joe\", \"age\" : 42 }",
            "{ \"name\" : \"@string\", \"age\" : \"@int\" }", false);
    test("[ { \"name\" : \"Joe\", \"age\" : 42 }, { \"name\" : \"Paul\", \"age\" : 28 }, { \"name\" : \"Mack\", \"age\" : 55 } ]",
            "[ { \"name\" : \"@string\", \"age\" : \"@int\"} ]", false);
    test("[\"red\", \"orange\", \"yellow\"]", "[\"@string\"]", false);
    test("[ { \"name\" : \"Joe\", \"age\" : 42, \"eye_color\" : \"brown\" }, " +
            "{ \"name\" : \"Paul\", \"age\" : 28, \"eye_color\" : \"brown\" }, " +
            "{ \"name\" : \"Mack\", \"age\" : 55, \"eye_color\" : \"blue\" } ]",
            "[ { \"name\" : [\"Joe\", \"Paul\", \"Mack\"], \"age\" : \"@int\", \"eye_color\" : [\"brown\", \"blue\"] } ]", true);
  }

  private static Boolean test(String json, String expectedJschema, Boolean preferEnums) throws Exception {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/json_to_jschema.js"));
    Invocable invocable = (Invocable) engine;
    Boolean result = (Boolean)invocable.invokeFunction("testEquals", json, expectedJschema, preferEnums);
    if (result == false) {
      String formattedJSON = (String)invocable.invokeFunction("formatJSONString", json);
      String formattedExpected = (String)invocable.invokeFunction("formatJSONString", expectedJschema, preferEnums);
      String actual = (String)invocable.invokeFunction("jsonToJSchemaString", json, preferEnums);
      System.out.println("Test failed: " + formattedJSON + "\n\tExpected: " + formattedExpected + "\n\tActual: " + actual + "\n");
    }
    return result;
  }
}
