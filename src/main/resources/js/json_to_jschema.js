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
    if (Object.prototype.toString.call(jschema) == "[object Array]") {
        parseArray(jschema);
    } else {
        parseMember(jschema);
    }
    return jschema;
}

function parseArray(array) {
    for (member in array) {
        parseMember(member);
    }
}

function parseMember(member) {
    for (key in member) {
        member[key] = getType(member[key]);
    }
}

function getType(value) {
    var nativeJSType = typeof value;
    if (nativeJSType == "string") {
        if (!isNaN(Date.parse(value))) {
            return "@date";
        } else if (testURI(value)) {
            return "@uri";
        }
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
        if (Object.prototype.toString.call(value) == "[object Array]") {
            parseArray(value);
        } else {
            return "@object";
        }
    } else {
        return "*";
    }
}

function testURI(value) {
    // http://stackoverflow.com/questions/1303872/trying-to-validate-url-using-javascript
    // https://github.com/jzaefferer/jquery-validation/blob/master/src/core.js#L1306
    return /^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test( value );
}

// Debugging
function generateJSchemaStringFromJSON(json) {
    return JSON.stringify(generateJSchemaFromJSON(json));
}