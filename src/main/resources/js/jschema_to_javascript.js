/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/



//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Lexical_grammar#Keywords
var RESERVED_KEYS = [
    "break",
    "case",
    "class",
    "catch",
    "const",
    "continue",
    "debugger",
    "default",
    "delete",
    "do",
    "else",
    "export",
    "extends",
    "finally",
    "for",
    "function",
    "if",
    "import",
    "in",
    "instanceof",
    "new",
    "return",
    "super",
    "switch",
    "this",
    "throw",
    "try",
    "typeof",
    "var",
    "while",
    "with",
    "yield",
    "_jschemaVal",
    "_jschemaMsg",
];


/* this function will check if the string is a valid keyword per ECMAScript 6
   https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Grammar_and_types
*/
function isValidKey(key){

    return true
}

// TODO: array, enum, struct, nested schema
function generateJavascriptForJSchema(jSchema, className) {
  var parseFunction =  "parse: function(jsonData){" +
                       "var json;" +
                       "if(typeof jsonData != 'undefined'){\n" +
                       "try{var json = JSON.parse(jsonData);\n" +
                       "}catch(e){\n" +
                       "return \"Invalid JSON format\";\n" +
                       "}return Object.assign(json, this.create());}}"


  try{
    var schema = JSON.parse(jSchema);
  } catch(e){
    return "Invalid jSchema format";
  }

  return  "var " + className + " = {\n" +
          generateCreate(schema) +
          parseFunction +
          "};";
}

function generateCreate(schema){
  var generatedVariables = "";
  var generatedSetters = "";

  for(var key in schema){
    if (schema.hasOwnProperty(key)){
      // TODO: check valid key
      generatedVariables += "var " + key + ";\n";
      generatedSetters += generateSetter(key, schema[key]) + "\n";
    }
  }
      return  "create: function(){" +
              "return{" +
              "validate : function(){" +
              "var _jschemaVal = {};" +
              "var _jschemaMsg = \"\";" +
              generatedVariables +
              generatedSetters +
              "for(var key in _jschemaVal){" +
              "if (this[key]){" +
              "_jschemaMsg += _jschemaVal[key](this[key]);" +
              "}}"+
              "if(_jschemaMsg === \"\") return \"Valid\";" +
              "return _jschemaMsg;}," +
              "toJSON : function(){" +
              "var toJson = {};" +
              "for (var key in this){\n" +
              "if (this.hasOwnProperty(key) && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {\n" +
              "toJson[key] = this[key];\n" +
              "}}return toJson;}};},\n"

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


function generateSetter(key, type) {
  return "_jschemaVal[\"" + key + "\"] = function(value){\n" +
    "if (" + generateValidator(type) + "){\n" +
    key + " = value;\n" +
    "return \"\"\n" +
    "} else{\n" +
    "return value + \" does not conform to " + type + "\";\n" +
    "}return;\n" +
    "};";
}