/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/

// TODO: array, enum, struct, nested schema
function generateJavascriptForJSchema(jSchema, className) {
  try{
    var schema = JSON.parse(jSchema);
  } catch(e){
    return "Invalid jSchema format"
  }

  var generatedVariables = "";
  var generatedFunctions = "";
  var generatedSetters = "";
  var generateParser = "if(typeof jsonData != 'undefined'){\n" +
                       "try{ var json = JSON.parse(jsonData);\n" +
                       "}catch(e){\n" +
                       "console.log(\"Invalid JSON format\");\n" +
                       "return;\n}\n" +
                       "for(var key in json){\n" +
                       "if(json.hasOwnProperty(key)){\n" +
                       "try{validators[key](json[key]);\n" +
                       "}catch(e){\n" +
                       "console.log('\"' + key + '\" does not conform to schema ');\n" +
                       "return;}}}}";

  for(var key in schema){
    if (schema.hasOwnProperty(key)){
      generatedVariables +=
          "var _" + key + ";\n";

      generatedSetters += generateSetter(key, schema[key]) + "\n";
      generatedFunctions += generateFunctions(key) + ",\n";
    }
  }

  return  "var " + className + " = {\n" +
          "parse: function(jsonData){\n" +
          "var validators = {};\n" +
          generatedVariables +
          generatedSetters + "\n" +
          generateParser + "\n" +
          "return {\n  " +
          generatedFunctions +
          "};\n}};";
}

function generateValidator(type){
  switch(type){
  case "@string" :
    // https://toddmotto.com/understanding-javascript-types-and-reliable-type-checking/
    return "Object.prototype.toString.call(value).slice(8, -1) === 'String'";
  case "@boolean" :
    return "Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'";
  case "@date" :
    return "!isNaN(Date.parse(value))";
  case "@uri" :
    // json_to_schema.js
    return " /^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$/i.test( value )"
  case "@int" :
    return "Number.isInteger(value)";
  case "@number" :
    return "!Number.isNaN(value)";
  default: // wildcard
    return "True";
  }
}
function generateFunctions(key){
  return "get " + key + "(){return _" + key + ";}," + "\n" +
         "set " + key + "(value){return validators[\"" + key + "\"](value)}";
}

function generateSetter(key, type) {
  return "validators[\"" + key + "\"] = function(value){\n" +
      "if (" + generateValidator(type) + "){\n" +
      "_" + key + " = value;\n" +
      "}else{\n" +
      "console.log(value + \" does not conform to " + type + "\");\n" +
      "}return;\n" +
      "}";
}
