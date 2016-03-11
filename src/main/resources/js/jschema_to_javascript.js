/*/*
 This javascript file provides functionality for generating java source code for working with
 JSON documents that satisfy a given jSchema
 */

// TODO: array, enum, struct
function generateJavascriptForJSchema(jSchema, className) {
  var parseFunction = "  parse: function(jsonData){\n" +
                      "    var json;\n" +
                      "    if(typeof jsonData != 'undefined'){\n" +
                      "    try{\n" +
                      "      json = JSON.parse(jsonData);\n" +
                      "    }catch(e){\n" +
                      "      return \"Invalid JSON format\";\n" +
                      "    }\n" +
                      "    return Object.assign(json, this.create());\n" +
                      "    }\n" +
                      "  }\n";

  try{
    var schema = JSON.parse(jSchema);
  } catch(e){
    return "Invalid jSchema format";
  }

  return  "var " + className + " = {\n" +
          generateCreate(schema) +
          parseFunction +
          "};\n";
}

function generateCreate(schema){
  var generatedSetters = "";

  for(var key in schema){
    if (schema.hasOwnProperty(key)){
      generatedSetters += generateSetter(key, schema[key]);
    }
  }
  return  "  create: function(){\n" +
          "    return{\n" +
          "      validate: function(){\n" +
          "        var validators = {};\n" +
          "        var msg = \"\";\n" +
          generatedSetters +
          "        for(var key in validators){\n" +
          "          if(this[key]){\n" +
          "            msg += validators[key](this[key]);\n" +
          "          }\n" +
          "        }\n" +
          "        if(msg === \"\"){\n" +
          "          return \"Valid\";\n"+
          "        }\n" +
          "        return msg;\n"+
          "      },\n" +
          "      toJSON: function(){\n" +
          "        var toJson = {};\n" +
          "        for (var key in this){\n" +
          "          if (this.hasOwnProperty(key) && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {\n" +
          "            toJson[key] = this[key];\n" +
          "          }\n" +
          "        }\n" +
          "        return toJson;\n" +
          "      }\n" +
          "    };\n" +
          "  },\n";
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
  return  "        validators[\"" + key + "\"] = function(value){\n" +
          "          if(" + generateValidator(type) + "){\n" +
          "            this." + key + " = value;\n" +
          "            return \"\";\n" +
          "          }\n" +
          "          return \"" + key + "=\" + value + \" does not conform to " + type + "\\n\";\n" +
          "        };\n";
}