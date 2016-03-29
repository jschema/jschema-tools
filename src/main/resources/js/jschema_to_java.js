/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/

//for errors, throw runtime exception
//Generates the Class Header line. Does not currently close the object with a "}".
function generateClass(classname){
  var className = "Class " + classname + "{\n";
  return className;
}

//gets keys from input jschema. May not be necessary
function makeKeys(jschema){
  var parsed_schema = JSON.parse(jschema);
  return Object.keys(parsed_schema).toString();
}

//gets values from input jschema. May not be necessary
function makeValues(jschema){
  var parsed_schema = JSON.parse(jschema);
  var str = "";
  for(var i in parsed_schema){
    str += parsed_schema[i] + ", ";
  }
  return str;
}

//Generates Java Object Based on jSchema input. Object name will be className.
function generateObject(jSchema, className){
  var parsed_schema = JSON.parse(jSchema);

  var keys = [];
  var count = 0;
  var obj = "";

  //creates constructor
  obj += "public " + className + "(";
  for (var i in parsed_schema){
  keys.push(i);
    var str = parsed_schema[i];
    obj += keys[count] + ", ";
    count++;
  }
  count = 0;

  //populates constructor
  obj = obj.substring(0, obj.length - 2);
  obj += "){";
  for(var i in parsed_schema){
    obj += "_" + keys[count] + " = " + keys[count] + ";";
    count++;
  }
  obj += "}";
  return obj;
}

function generateFields(jschema){
  var parsed_schema = JSON.parse(jschema);
  var String = "";
  for(var i in parsed_schema){
    var str = parsed_schema[i];
    String += "private " + jschema_parser(str);
    String += "_" + Object.keys(parsed_schema);
  }
  String += ";\n";
  return String;

}
//Generate Get methods for the created Java Object
function generateGET(Jschema){
  return Jschema;
}

//Generate Set methods for the created Java Object
function generateSET(Jschema){
  return Jschema;
}

function generateError(jschema){
  return jschema;
}

function jschema_parser(str){
  str_1 = str.charAt(0);
  switch(str_1){
    case '@' : return parse_core_type(str);
               break;
    case '[' : return parse_array_type(str);
               break;
    case '{' : return parse_struct_type(str);
               break;
    default  : return "* ";
  }
}

function parse_core_type(str){
  switch(str){
    case "@String" : return "String ";
                     break;
    case "@boolean": return "boolean ";
                     break;
    case "@date"   : return "Date ";
                     break;
    case "@uri"    : return "uri ";
                     break;
    case "@int"    : return "int ";
                     break;
    case "@number" : return "double ";
                     break;
    default        : return "* ";
  }
}

function parse_struct_type(str){
  return "Struct";
}

function parse_array_type(str){
  return "array";
}
