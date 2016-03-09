/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/
//Generates the Class Header line. Does not currently close the object with a "}".
function myTest(jSchema){
    //var parsed_schema = JSON.parse(jschema);
    //return parsed_schema[0];

var parsed_schema = JSON.parse(jSchema);
var aux = "hh_ " + parsed_schema;
return aux;
  var keys = [];
  var count = 0;
  var obj = "";

  //creates constructor
  obj += "public " + " ALGO " + "(";
  for (var i in parsed_schema){
  keys.push(i);
    obj += parsed_schema[i] + " ";
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

function generateClass(classname){
  var className = "class " + classname + "{\n";
  return className;
}

function generateFields(Jschema){
 return Jschema;
}


//gets keys from input jschema. May not be necessary
function makeKeys(jschema){
  var parsed_schema = JSON.parse(jschema);
  return Object.keys(parsed_schema).toString();
  }

//gets values from input jschema. May not be necessary
function makeValues(jschema){
  var parsed_schema = JSON.parse(jschema);
  return parsed_schema;
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
    obj += parsed_schema[i] + " ";
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
