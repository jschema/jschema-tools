function jsonToJSchema(doc1, doc2, doc3, doc4, doc5) {
  var schema = null;
  for(var arg = 0; arg < arguments.length; ++ arg)
  {
    schema = updateSchema(schema, arguments[arg])
  }

  return printSchema(schema)
}

function updateSchema(document, schema) {
  return schema
}

// JSON -> JSchema generator
function generateJSchemaFromJSON(json) {
    var jschema = JSON.parse(json);
    for (key in jschema) {
        jschema[key] = getType(jschema[key]);
    }
    return jschema;
}

function getType(value) {
    var nativeJSType = typeof value;
    if (nativeJSType == "string") {
        return "@string";
    } else if (nativeJSType == "number") {
        if (value % 1 === 0) {
            return "@int";
        } else {
            return "@number";
        }
    } else if (nativeJSType == "boolean") {
        return "@boolean";
    } else if (nativeJSType == "object") {
        return "@object";
    } else {
        return "*";
    }
}

// Debugging
function generateJSchemaStringFromJSON(json) {
    return JSON.stringify(generateJSchemaFromJSON(json));
}