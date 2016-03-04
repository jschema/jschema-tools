# jschema-to-javascript.js

## Input

jschema: `{"name" : "@string", "age" : "@int"}`

class name: `Person`

## Output
TO BE UPDATED

## Usage
```javascript
  var p = Person.parse();
  p.name = 4; // "4 does not conform to @string"
  p.name = "Ashley";
  console.log(p.name); // "Ashley"
```
