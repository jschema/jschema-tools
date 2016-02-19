/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/

function generateJavascriptForJSchema(jSchema, className) {
  var generatedSource = "var " + className + " = function(){}"
  return generatedSource
}