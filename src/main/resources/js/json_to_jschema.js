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

function printSchema(schema) {
  return "{}"
}
