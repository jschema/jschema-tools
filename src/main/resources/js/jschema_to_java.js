
function generateAll(classname, jschema){
  var String = "";
  var indent = "  ";
  String += "package org.jschema.generated.java;\n"; //import line for testing, should be deleted before release
  String += "import java.util.Map;\n";
  String += generateClass(classname);
  String += indent + generateField();
  //String += indent + generateConstructor(classname, jschema);
  //String += indent + generateParse(classname, jschema);
  String += indent + generateToJson();
  String += generateGet(jschema);
  String += generateSet(jschema);
  String += "\n}\n";
  return String;
}
function generateClass(classname){
  var className = "public class " + classname + "{\n";
  return className;
}

function generateField(){
  var String = "private Map<String, Object> _fields;\n";
  return String;
}

function generateConstructor(classname, jschema){
  //todo --make based off Carson's example
  return "";
}

function generateParse(classname, jschema){
  //todo --make based off Carson's example
  var String = "";
  return String;
}

function generateToJson(){
  var String = "public String toJSON(){return _fields.toString();}\n";
  return String;
}

function generateGet(jschema){
    var parsed_schema = JSON.parse(jschema);
    var String = "";
    var indent = "  ";
    for(var key in parsed_schema){
      String += indent;
      String += "public Object get" + key + "(){return _fields.get(\"" + key + "\");}\n";
    }
    return String;
}

function generateSet(jschema){
    var parsed_schema = JSON.parse(jschema);
    var String = "";
    var indent = "  ";
    for(var key in parsed_schema){
      String += indent;
      String += "public void set" + key + "(Object " + key + "){_fields.put(\"" + key + "\", " + key +  ");}\n";
    }
    return String;
}


