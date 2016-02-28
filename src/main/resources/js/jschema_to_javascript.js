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

  var generatedSource =
      "var " + className + " = {\n" +
      "parse: function(jsonData){\n";

  for(var key in schema){
    if (schema.hasOwnProperty(key)){
      generatedSource +=
        "var _" + key + ";\n" +
        "var _type = '" + schema[key] + "';\n" +
        "var validateType = function(elem) {\n" +
          generateValidator(schema[key]) +
        "};\n" +
        "return {\n  " +
          generateGetter(key) +
          ",\n" +
          generateSetter(key) +
          "\n"  +
        "};\n"
    }
  }

  generatedSource +=
      "}};";
  return generatedSource;
}

// https://toddmotto.com/understanding-javascript-types-and-reliable-type-checking/
function generateIsJSType(type){
    return "return Object.prototype.toString.call(elem).slice(8, -1) === '" + type + "';";
}

// TODO: date, uri, int, number
function generateValidator(type){
    switch(type){
        case "@string" :
            return generateIsJSType('String');
        case "@boolean" :
            return generateIsJSType('Boolean');
        default:
            return "return True";
    }
}
function generateGetter(key){
  return "get " + key + "(){\n" +
           "return _" + key + ";\n" +
         "}";
}

function generateSetter(key){
  return "set " + key + "(value){\n" +
         "if (validateType(value)){\n" +
         "return _" + key + " = value;\n" +
         "}\n" +
         "return console.log(value + \" does not conform to \" + _type);\n" +
         "}";
}