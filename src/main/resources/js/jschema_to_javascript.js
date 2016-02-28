/*
  This javascript file provides functionality for generating java source code for working with
  JSON documents that satisfy a given jSchema
*/

function generateJavascriptForJSchema(jSchema, className) {
  var schema = JSON.parse(jSchema);
  var generatedSource =
      "var " + className + " = {\n" +
        "parse: function(jsonData){\n";

  for(var key in schema){
    if (schema.hasOwnProperty(key)){
      generatedSource +=
        "var _" + key + ";\n" +
        "return {\n" +
          "get " + key + "(){\n" +
            "return _" + key + ";\n" +
          "},\n" +
          "set " + key + "(value){\n" +  /* TODO: type check */
            "return _" + key + " = value;\n" +
          "}" +
        "};\n"
    }
  }

  generatedSource +=
      "}};";
  return generatedSource;
}