package org.jschema.generators;

import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by remin on 3/1/2016.
 */
public class JSchemaToJavascriptTest {

    @Test
    public void runTests(){
       // System.out.println(generate(testObject()));
        assertEquals(testCoreTypesExp(),generate(testCoreTypes()).toString().replace("\n",""));
       // System.out.println(testObjectExp());

    }

    /*****************************Test Cases and Their Expected Outputs**********************************/
    //test core types
    public static String testCoreTypes(){
        return "{ \"name\" : \"@string\", \"age\" : \"@int\", \"birthday\" : \"@date\", \"website\" : \"@uri\", " +
                "\"student\" : \"@boolean\", \"favorite_number\":\"@number\" }";
    }
    public static String testCoreTypesExp(){
        return (genHeader("Person")+
                genValid("name","string")+
                genValid("age","int")+
                genValid("birthday","date")+
                genValid("website","uri")+
                genValid("student","boolean")+
                genValid("favorite_number","number")+
                genValidLoop()+genToJSON()+genParse());

    }


    //test basic arrays
    public static String testArrayString(){
        return "{ \"name\" : [\"@string\"]}";
    }

    public static String testNestedObject(){
        return "{ \"name\" : \"@string\", \"age\" : {\"month\" : \"@string\", \"day\" : \"@int\", \"year\" : \"@int\"} }";
    }

    public static String testNestedObject2(){
        return "{ \"name\" : \"@string\", \"age\" : {\"month\" : \"@string\", \"day\" : \"@int\", \"year\" : {\"decade\" :\"@int\"} }}";
    }



    public static String testEnum(){
        return "{ \"name\" : [\"first\",\"last\"]}";

    }

    public static String testAssort1(){
        return "{  \"first_name\" : \"@string\",\n" +
                "  \"last_name\" : \"@string\",\n" +
                "  \"age\" : \"@int\",\n"+
                "  \"type\" : [\"friend\", \"customer\", \"supplier\"],\n" +
                "  \"emails\" : [\"@string\"]}";
    }
    private Object generate( String src )
    {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(new FileReader("../../src/main/resources/js/jschema_to_javascript.js"));
        }catch(Exception e){
            System.out.println("error finding js file: "+e.toString());
        }
        Invocable invocable = (Invocable) engine;
        try {
            return invocable.invokeFunction("generateJavascriptForJSchema", src, "Person");
        }catch(Exception e){
            System.out.println("cannot invoke function: "+e.toString());
        }
        return null;
    }

    /////////////////////////////Helper Functions for Testing//////////////////////////////
    //Generate start of output
    private static String genParse(){
        return ("parse: function(jsonData){var json;if(typeof jsonData != \'undefined\'){try{var json = JSON.parse(jsonData);"+
                "}catch(e){return \"Invalid JSON format\";}" +
                 "return Object.assign(json, this.create());}}};");
    }
    private static String genToJSON(){
        return("toJSON: function(){" +
                "var toJson = {};" +
                "for (var key in this){" +
                "if (this.hasOwnProperty(key) && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {" +
                "toJson[key] = this[key];" +
                "}}return toJson;}};},");
    }
    private static String genValidLoop(){
        return("for(var key in validators){if(this[key]){"+
                "msg += validators[key](this[key]);}}if(msg === \"\"){return \"Valid\"};return msg;},");
    }
    private static String genValid(String key,String type){
        StringBuilder valid=new StringBuilder("validators[\""+key+"\"] = function(value){if(");
        switch(type){
            case "string":valid.append( "Object.prototype.toString.call(value).slice(8, -1) === \'String\'"); break;
            case "boolean" :valid.append("Object.prototype.toString.call(value).slice(8, -1) === \'Boolean\'");break;
            case "date" :valid.append("!isNaN(Date.parse(value))");break;
            case "uri" :valid.append(" /^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$/i.test( value )");break;
            case "int" :valid.append("Number.isInteger(value)");break;
            case "number" :valid.append("!Number.isNaN(value)");break;
            default:valid.append("True");
        }
        valid.append("){this."+key+" = value;return \"\"}");
        valid.append("return \"" +key+"=\" + value + \" does not conform to @" + type + "\\n\";};");
        return valid.toString();
    }

    private static String genHeader(String className){
        return("var Person = {create: function(){return{validate : function(){"+
                "var validators = {};var msg = \"\";");
    }

}
