/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/
//Generates the Class Header line. Does not currently close the object with a "}".
function generateClass(classname){
  var className = "Class " + classname + "{\n";
  return className;
}

//Generates Java Object Based on jSchema input. Object name will be className.
function generateObject(jSchema, className){
  var parsed_schema = JSON.parse(jSchema);

  var keys = [];
  var count = 0;
  var obj = "";

  //creates constructor
  obj += "  public " + className + "(";
  for (var i in parsed_schema){
  keys.push(i);
    obj += parsed_schema[i] + " ";
    obj += keys[count] + ", ";
    count++;
  }
  count = 0;

  //populates constructor
  obj = obj.substring(0, obj.length - 2);
  obj += "){\n";
  for(var i in parsed_schema){
    obj += "    _" + keys[count] + " = " + keys[count] + ";\n";
    count++;
  }
  obj += "  }\n";
  return obj;
}

function generateFields(Jschema){
 return Jschema;
}
//Generate Get methods for the created Java Object
function generateGET(Jschema){
  return Jschema;
}

//Generate Set methods for the created Java Object
function generateSET(Jschema){
  return Jschema;
}
