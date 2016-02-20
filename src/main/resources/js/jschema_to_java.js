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
  return parsed_schema
}

//Generate Get methods for the created Java Object
function generateGet(Object){
  return "Get Methods here"
}

//Generate Set methods for the created Java Object
function generateSet(Object){
  return "Set Methods here"
}
