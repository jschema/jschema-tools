
var indent = "";
var counter = 0;

function generateAll(classname, jschema){
  var parsed_schema;
  var String = "";
  if(Object.prototype.toString.call(jschema) === "[object Object]" || isArray(jschema)){  //for recursive calls
    parsed_schema = jschema;
  }
  else{
    parsed_schema = JSON.parse(jschema);
    String += "package org.jschema.generated.java;\n"; //import line for testing, should be deleted before release
    String += "import java.util.*;\n\n";
    String += "import org.jschema.parser.*;\n"
  }
  String += indent + generateClass(classname);
  indent += "  ";
  String += indent + generateField() + "\n";
  if(counter == 0){
    String += indent + generateParse(classname, 0);
    String += indent + generateInner(classname, parsed_schema);
  }
  String += indent + generateToJson() + "\n";
  for(var key in parsed_schema){
    String += indent + generateGet(key, parsed_schema[key]);
    String += indent + generateSet(key, parsed_schema[key]) + "\n";

    if(isObject(parsed_schema[key]) && !isArray(parsed_schema[key])){      //if value is an object
      counter++;
      String += generateAll(capitalize(key), parsed_schema[key]);
    }
    if(isArray(parsed_schema[key])){                                       //if value is an array
      if(isArray(parsed_schema[key][0])){
        counter++;
        String += generateAll(capitalize(key), parsed_schema[key][0]);
      }
      else if(isObject(parsed_schema[key][0])){
        counter++;
        String += generateAll(capitalize(key), parsed_schema[key][0]);
      }
    }
    String += generateEnums(capitalize(key), parsed_schema[key]);              //generate Enums, if present
  }
  indent = indent.slice(0, indent.length() - 2);
  String += "\n" + indent + "}\n";
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

function generateParse(classname, static){
  var newName = "new" + capitalize(classname);
  if(static == 0){
    var String = "public static " + classname +  " parse(String jsonString){\n";
  }
  else{
    var String = "public " + classname +  " parse(String jsonString){\n";
  }
  indent += "  ";
  String += indent + classname + " " + newName + " = new " + classname + "();\n";
  String += indent + "Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();\n";

  String += indent + "Iterator it = jsonObject.entrySet().iterator();\n";
  String += indent + "while(it.hasNext()){\n";
  indent += "  ";

  String += indent + "Map.Entry pair = (Map.Entry)it.next();\n";

  String += indent + "if(pair.getValue() instanceof Map){\n";
  indent += "  ";
  String += indent + "Object obj = makeObject(" + newName + ", (String)pair.getKey(), (Map)pair.getValue());\n";
  String += indent + newName + "._fields.put((String) pair.getKey(), obj);\n";
  indent = indent.slice(0, indent.length() - 2);
  String += indent + "}\n";
  String += indent + "else{\n";
  indent += "  ";
  String += indent + newName + "._fields.put((String) pair.getKey(), pair.getValue());\n";
  indent = indent.slice(0, indent.length() - 2);
  String += indent + "}\n";

  indent = indent.slice(0, indent.length() - 2);
  String += indent + "}\n";

  String += indent + "return " + newName + ";\n";
  indent = indent.slice(0, indent.length() - 2);
  String += indent + "}\n";
  return String;
}



function generateInner(classname, parsed_schema){
  var newName = "new" + capitalize(classname);
  var String = "public static Object makeObject(" + classname + " " + newName + ", String key, Map value){\n";
  indent += "  ";
  for(key in parsed_schema){
    if((!isArray(parsed_schema[key]) && isObject(parsed_schema[key])) ||
        (isArray(parsed_schema[key]) && isObject(parsed_schema[key][0]))){
      String += indent + "if(key.equals(\"" + key + "\")){\n";
      indent += "  ";
      String += indent + classname + "." + capitalize(key) + " " + key.charAt(0) + " = " + newName + ".new " +capitalize(key) + "();\n";
      String += indent + key.charAt(0) + "._fields = value;\n";
      String += indent + "return " + key.charAt(0) + ";\n";
      indent = indent.slice(0, indent.length() - 2);
      String += indent + "}\n";
    }
  }
  String += indent + "return null;\n";
  indent = indent.slice(0, indent.length() - 2);
  String += "}\n";
  return String;
}

/*
function generateInnerList(classname, parsed_schema){
  var newName = "new" + capitalize(classname);
  String = "public static List parseInnerList(" + classname + " " +  newName + ", Object key, List value){\n";
  indent += "  ";
  String += indent + "List<Object> list = new ArrayList<>();\n";
  String += indent + "for(int i = 0; i < value.size(); i++) {\n";
  indent += "  ";
  String += indent + "Object result = parseInnerMap(" + newName + ", key, (Map) value.get(i));\n";
  String += indent + "list.add(result);\n";
  indent = indent.slice(0, indent.length() - 2);
  String += indent + "}\n";
  String += indent + "return list;\n";
  indent = indent.slice(0, indent.length() - 2);
  String += indent + "}\n";
  return String;

}

*/
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

function generateSet(key, value){
    var String = "";
    String += "public void set" + capitalize(key) + "(" + CheckValue(key, value) + " " + key + "){_fields.put(\"" + key + "\", " + key +  ");}\n";
    return String;
}

function generateEnums(key, value){
  if(!isArray(value)){
    return "";
  }
  else{
    if(value[0].toString().charAt(0) == '@' || isObject(value[0])){
      return "";
    }
    var String = indent + "public enum " + key + "{\n";
    indent += "  ";
    for(var i = 0; i < value.length; i++){
      String += indent + value[i].toUpperCase() + ",\n";
    }
    String = String.slice(0, String.length - 2) + "\n";
    indent = indent.slice(0, indent.length() - 2);
    String += indent + "}\n\n";
    return String;
  }
}

//
//Helper Methods
//

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
  String = "List<" + getListType(capitalize(key), value) + ">";
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

function getListType(key, value){
  var type = "";
  if(isArray(value)){
    type = CheckString(value[0]);
    if(type === "BAD"){             //for enum-like arrays
      return key;
    }
    return type;
  }
  return key;
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
