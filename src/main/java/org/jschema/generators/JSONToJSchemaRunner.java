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

    // Generate a JSchema from Foursquare API results
    print("{\"meta\":{\"code\":200,\"requestId\":\"5706b191498edbac4f50b79d\"},\"notifications\":[{\"type\":\"notificationTray\",\"item\":{\"unreadCount\":1}}],\"response\":{\"venues\":[{\"id\":\"430d0a00f964a5203e271fe3\",\"name\":\"Brooklyn Bridge Park\",\"contact\":{\"phone\":\"2128033822\",\"formattedPhone\":\"(212) 803-3822\",\"twitter\":\"nycparks\",\"facebook\":\"104475634308\",\"facebookUsername\":\"BartowPell\",\"facebookName\":\"Bartow-Pell Mansion Museum\"},\"location\":{\"address\":\"Main St\",\"crossStreet\":\"Plymouth St\",\"lat\":40.70227697066692,\"lng\":-73.9965033531189,\"distance\":389,\"postalCode\":\"11201\",\"mayNotNeedAddress\":false,\"cc\":\"US\",\"city\":\"Brooklyn\",\"state\":\"NY\",\"country\":\"United States\",\"formattedAddress\":[\"Main St (Plymouth St)\",\"Brooklyn, NY 11201\"]},\"categories\":[{\"id\":\"4bf58dd8d48988d163941735\",\"name\":\"Park\",\"pluralName\":\"Parks\",\"shortName\":\"Park\",\"icon\":{\"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/park_\",\"suffix\":\".png\"},\"primary\":true}],\"verified\":true,\"stats\":{\"checkinsCount\":40392,\"usersCount\":24513,\"tipCount\":293},\"url\":\"http:\\/\\/nyc.gov\\/parks\",\"beenHere\":{\"count\":1,\"marked\":true},\"specials\":{\"count\":0,\"items\":[]},\"listed\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"hereNow\":{\"count\":1,\"summary\":\"One other person is here\",\"groups\":[{\"type\":\"others\",\"name\":\"Other people here\",\"count\":1,\"items\":[]}]},\"referralId\":\"v-1460056465\",\"venueChains\":[{\"id\":\"556df7e1a7c82e6b724d822e\"}]},{\"id\":\"4c3c76bd7f49c9b65de16be3\",\"name\":\"Michael\\u2019s Food Cart\",\"contact\":{\"phone\":\"7189029951\",\"formattedPhone\":\"(718) 902-9951\"},\"location\":{\"address\":\"South Street\",\"crossStreet\":\"Corner of South St and Wall St\",\"lat\":40.70013381479425,\"lng\":-73.99983798076666,\"distance\":20,\"mayNotNeedAddress\":false,\"cc\":\"US\",\"city\":\"New York\",\"state\":\"NY\",\"country\":\"United States\",\"formattedAddress\":[\"South Street (Corner of South St and Wall St)\",\"New York, NY\"]},\"categories\":[{\"id\":\"4bf58dd8d48988d1cb941735\",\"name\":\"Food Truck\",\"pluralName\":\"Food Trucks\",\"shortName\":\"Food Truck\",\"icon\":{\"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/food\\/streetfood_\",\"suffix\":\".png\"},\"primary\":true}],\"verified\":false,\"stats\":{\"checkinsCount\":51,\"usersCount\":40,\"tipCount\":1},\"allowMenuUrlEdit\":true,\"specials\":{\"count\":0,\"items\":[]},\"hereNow\":{\"count\":0,\"summary\":\"Nobody here\",\"groups\":[]},\"referralId\":\"v-1460056465\",\"venueChains\":[]}],\"confident\":false}}",
            false);
  }

  private static void test(String json, String expectedJschema, Boolean preferEnums) throws Exception {
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
  }

  private static void print(String json, Boolean preferEnums) throws Exception {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new FileReader("src/main/resources/js/json_to_jschema.js"));
    Invocable invocable = (Invocable) engine;

    String formattedJSON = (String)invocable.invokeFunction("formatJSONString", json);
    String result = (String)invocable.invokeFunction("jsonToJSchemaString", json, preferEnums);
    System.out.println("JSON input: " + formattedJSON + "\nJSchema result: " + result + "\n");
  }
}
