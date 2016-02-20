/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/

function generateJavaForJSchema(jSchema, className) {
  var generatedSource = "package " + packageFor(jSchema)
  return generatedSource
}

function packageFor(className) {
  return className
}

//Generates Java Object Based on jSchema input. Class will be ClassName.
//We will need to parse the jSchema object to return the correct object
function generateObject(jSchema, className){
  var parsed_schema = jSchema;
  var keys = [];
  var count = 0;

  var obj = "Class " + className + "{\n";
  for(var i in parsed_schema){
    keys.push(i);
    obj += "  " + parsed_schema[i] + " ";
    obj += "_" + keys[count] + ";\n";
    count++;
  }
  obj += "}\n"
  count = 0;

  obj += "public " + className + "(";
  for (var i in parsed_schema){
    obj += parsed_schema[i] + " ";
    obj += keys[count] + ", ";
    count++;
  }
  count = 0;
  obj = obj.substring(0, obj.length - 2);
  obj += "){\n";
  for(var i in parsed_schema){
    obj += "  _" + keys[count] + " = " + keys[count] + ";\n";
    count++;
  }
  obj += "}\n";

  return obj;
}

//Generate Get methods for the created Java Object
function generateGet(Object){
  return "Get Methods here"
}

//Generate Set methods for the created Java Object
function generateSet(Object){
  return "Set Methods here"
}
