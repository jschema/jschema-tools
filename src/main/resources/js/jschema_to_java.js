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

//Generates Java Object Based on jSchema input. Object name will be className.
function generateObject(jSchema, className){
  var parsed_schema = JSON.parse(jSchema);
  var obj = "";

  //creates constructor. the for loop loops through each key/value pair. Each parsed_schema[i]
  //is the value corresponding to its key i.
  obj += "public " + className + "(";
  for (var i in parsed_schema){
    obj += jschema_parser(parsed_schema[i]);
    obj += i + ", ";
  }

  //populates constructor
  obj = obj.substring(0, obj.length - 2);
  obj += "){";
  for(var i in parsed_schema){
    obj += "_" + i + " = " + i + ";";
  }
  obj += "}";
  return obj;
}

function generateFields(jschema){
  var parsed_schema = JSON.parse(jschema);
  var String = "";

  //loops through each key/value pair. Each parsed_schema[i]
  //is the value corresponding to its key i.
  for(var i in parsed_schema){
    String += "private " + jschema_parser(parsed_schema[i]);
    if(i != 0){
      String += "_" + i + ";\n";
    }
  }
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


//Main jschema parser method. Takes in a jschema array, and checks the
//first character.
function jschema_parser(str){
  str_1 = str.toString().charAt(0);
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

//Checks for core types, returns appropriate value.
function parse_core_type(str){
  var val = "";

  //Case 1: if str is an array, return appropriate Java variable
  if(str instanceof Array){
    switch(str.toString()){
      case "@String" : return "String[] ";
                       break;
      case "@boolean": return "boolean[] ";
                       break;
      case "@date"   : return "Date[] ";
                       break;
      case "@uri"    : return "uri[] ";
                       break;
      case "@int"    : return "int[] ";
                       break;
      case "@number" : return "double[] ";
                       break;
      default        : return typeof str + " ";
    }
  }

  //Generic case: if type is just a variable, return the variable name
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
    default        : return typeof str + " ";
  }
}

function parse_struct_type(str){
  return generateObject(str, str);
}

//Parses a jschema array. For loop behaves like those above.
function parse_array_type(str){
  var String = "";
  for(var i in str){
    String += jschema_parser(str[i]);
    String += "_" + i + ";\n";
  }
  return String;
}
