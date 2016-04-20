
function generateAll(classname, jschema){
var parsed_schema = JSON.parse(jschema);
  var String = "";
  var indent = "  ";
  String += "package org.jschema.generated.java;\n"; //import line for testing, should be deleted before release
  String += "import java.util.*;\n";
  String += generateClass(classname);
  String += indent + generateField();
  //String += indent + generateConstructor(classname, jschema);
  //String += indent + generateParse(classname, jschema);
  String += indent + generateToJson();
  for(var key in parsed_schema){
    String += indent + generateGet(key, parsed_schema[key]);
    String += generateSet(key);
  }
  String += "\n}\n";
  return String;
}
function generateClass(classname){
  var className = "public class " + classname + "{\n";
  return className;
}

function generateField(){
  var String = "private Map<String, Object> _fields = new HashMap<String, Object>();\n";
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

function generateGet(key, value){
    var String = "";
    var indent = "  ";
    String += "public ";
    String += CheckValue(key, value);
    String += " get" + capitalize(key) + "(){return (" + CheckValue(key, value)  + ") _fields.get(\"" + key + "\");}\n";
    return String;
}

function generateSet(key){
    var String = "";
    var indent = "  ";
    String += indent;
    String += "public void set" + capitalize(key) + "(Object " + key + "){_fields.put(\"" + key + "\", " + key +  ");}\n";
    return String;
}

function CheckValue(key, value){
  if(isArray(value)){
    return CheckArrays(key, value);
  }
  else if(isObject(value)){
    return capitalize(key);
  }
  else if(isString){
    return CheckString(value);
  }
}

function CheckArrays(key, value){
  String = "List<" + getListType(key, value) + "> ";
  return String;
}

function CheckString(value){
    switch(value){
      case "@string" : return "String";
                       break;
      case "@boolean": return "boolean";
                       break;
      case "@date"   : return "Date";
                       break;
      case "@uri"    : return "uri";
                       break;
      case "@int"    : return "int";
                       break;
      case "@number" : return "double";
                       break;
      default        : return "BAD";
    }
}

//
//Helper Methods
//

function getListType(key, value){
  var type = "";
  if(isObject(value)){
    if(isArray(value)){
      type = CheckString(value[0]);
      if(type === "BAD"){             //for enum-like arrays
        return key;
      }
      return type;
    }
    else{
      return key;
    }
  }
  return "toDo";
}

function isArray(value) {
    return Object.prototype.toString.call(value) === "[object Array]";
}

function isObject(value) {
    return typeof value === "object";
}

function isString(value){
  return typeof value === "string";
}

function capitalize(str){
  return str.charAt(0).toUpperCase() + str.slice(1);
}