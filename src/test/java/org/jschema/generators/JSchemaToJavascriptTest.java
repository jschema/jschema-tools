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
    public void runCoreTest(){
        String expected=testCoreTypesExp();
        String actual=generate(testCoreTypes()).toString().replace("\n","");
        assertEquals(expected,actual);
    }
    @Test
    public void runNestedObjectTest(){
        String expected=testNestedObjectExp();
        String actual=generate(testNestedObject()).toString().replace("\n","");
        assertEquals(expected,actual);
    }

    /*****************************Test Cases and Their Expected Outputs**********************************/
    //test core types
    public String testCoreTypes(){
        return "{ \"name\" : \"@string\", \"age\" : \"@int\", \"birthday\" : \"@date\", \"website\" : \"@uri\", " +
                "\"student\" : \"@boolean\", \"favorite_number\":\"@number\" }";
    }
    public String testCoreTypesExp(){
        StringBuilder expected=new StringBuilder();
        expected.append(genHeader("Person"));
        expected.append(genVars("name","string"));
        expected.append(genVars("age","int"));
        expected.append(genVars("birthday","date"));
        expected.append(genVars("website","uri"));
        expected.append(genVars("student","boolean"));
        expected.append(genVars("favorite_number","number"));
        expected.append(genValidHeader());
        expected.append(genValid("name","string","        "));
        expected.append(genValid("age","int","        "));
        expected.append(genValid("birthday","date","        "));
        expected.append(genValid("website","uri","        "));
        expected.append(genValid("student","boolean","        "));
        expected.append(genValid("favorite_number","number","        "));
        expected.append(genValidLoop());
        expected.append(genToJSON());
        expected.append(genParse());
        return expected.toString();

    }


    //test basic arrays
    public String testArrayString(){
        return "{ \"name\" : [\"@string\"]}";
    }
    /*public String testArrayStringExp(){
        return (genHeader("Person")+
                genValid("name","string")+
                genValidLoop()+genToJSON()+genParse());
    }*/

    //nested object
    public String testNestedObject(){
        return "{ \"name\" : \"@string\", \"age\" : {\"month\" : \"@string\", \"day\" : \"@int\", \"year\" : {\"decade\":\"@int\"}} }";
    }

    public String testNestedObjectExp(){
        StringBuilder expected=new StringBuilder();
        expected.append(genHeader("Person"));
        expected.append(genVars("name","string"));
        expected.append(genNestedVars("age","{          month: \"@string\",          day: \"@int\",          year: {            decade: \"@int\",            },          },"));
        expected.append(genValidHeader());
        expected.append(genValid("name","string","        "));
        expected.append(genValid("age","[object Object]","          "));
        expected.append(genValidLoop());
        expected.append(genToJSON());
        expected.append(genParse());
        return expected.toString();
    }

    public String testNestedObject2(){
        return "{ \"name\" : \"@string\", \"age\" : {\"month\" : \"@string\", \"day\" : \"@int\", \"year\" : {\"decade\" :\"@int\"} }}";
    }



    public String testEnum(){
        return "{ \"name\" : [\"first\",\"last\"]}";

    }

    public String testAssort1(){
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
            engine.eval(new FileReader("src/main/resources/js/jschema_to_javascript.js"));
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
    private String genParse(){
        return ("  parse: function(jsonData){" +
                "    var json;" +
                "    if(typeof jsonData != \'undefined\'){" +
                "      try{" +
                "        json = JSON.parse(jsonData);"+
                "      }catch(e){" +
                "        return \"Invalid JSON format\";" +
                "      }" +
                "      return Object.assign(json, this.create());    }" +
                "  }"+
                "};");
    }
    private String genToJSON(){
        return("      toJSON: function(){" +
                "        var toJson = {};" +
                "        for (var key in this){" +
                "          if (this.hasOwnProperty(key) && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {" +
                "            toJson[key] = this[key];" +
                "          }        }        return toJson;      }    };  },");
    }
    private String genValidLoop(){
        return("        for(var key in validators){          if(this[key]){"+
                "            msg += validators[key](this[key]);          }        }" +
                "        if(msg === \"\"){          return \"Valid\";        }        return msg;      },");
    }
    private String genValid(String key,String type, String indent){
        StringBuilder valid=new StringBuilder(indent+"validators[\""+key+"\"] = function(value){          if(");
        switch(type){
            case "string":valid.append( "Object.prototype.toString.call(value).slice(8, -1) === \'String\'"); break;
            case "boolean" :valid.append("Object.prototype.toString.call(value).slice(8, -1) === \'Boolean\'");break;
            case "date" :valid.append("!isNaN(Date.parse(value))");break;
            case "uri" :valid.append(" /^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$/i.test( value )");break;
            case "int" :valid.append("Number.isInteger(value)");break;
            case "number" :valid.append("!Number.isNaN(value)");break;
            default:valid.append("True");
        }
        valid.append("){            this."+key+" = value;            return \"\";          }");
        if(type.compareTo("[object Object]")==0){
            valid.append("          return \"" + key + "=\" + value + \" does not conform to " + type + "\\n\";        };");
        }else {
            valid.append("          return \"" + key + "=\" + value + \" does not conform to @" + type + "\\n\";        };");
        }
        return valid.toString();
    }

    private String genHeader(String className){
        return("var Person = {  create: function(){    return{      jschema: {");
    }
    private String genVars(String name, String type){
        if(!type.contains("object") && !type.contains("{")) {
            return ("        " + name + ": " + "\"@" + type + "\",");
        }
        return ("        " + name + ": " + "\"" + type + "\",");
    }
    private String genNestedVars(String name, String type){

        return ("        " + name + ": "  + type + "");
    }
    private String genValidHeader(){
         return("      },      validate: function(){"+
                "        var validators = {};        var msg = \"\";");
    }
    private String genValidators(String indent){
        return indent+"var validators = {};        var msg = \"\";";
    }

}
