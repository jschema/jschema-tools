/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/

function generateJavaForJSchema(jSchema, className) {
  var generatedSource = "package " + packageFor(className)
  return generatedSource
}

function packageFor(className) {
  return className
}













//Generates Java Object Based on jSchema input. Class will be ClassName
function generateObject(jSchema, className){
  return className
}

//Generate Get methods for the created Java Object
function generateGet(Object){
  return Object
}

//Generate Set methods for the created Java Object
function generateSet(Object){
  return Object
}
