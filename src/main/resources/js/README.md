# jschema-to-javascript.js

## Input

jschema: `{"name" : "@string", "age" : "@int"}`

class name: `Person`

## Output
```javascript
var Person = {
  parse: function(jsonData){
    var validators = {};
    var _name;
    var _age;
    
    validators["name"] = function(value){
      if (Object.prototype.toString.call(value).slice(8, -1) === 'String'){
        _name = value;
      }else{
        console.log(value + " does not conform to @string");
      }
      return;
    }
    validators["age"] = function(value){
      if (Number.isInteger(value)){
        _age = value;
      }else{
        console.log(value + " does not conform to @int");
      }
      return;
    }
    
    if(typeof jsonData != 'undefined'){
      try{ 
        var json = JSON.parse(jsonData);
      }catch(e){
        console.log("Invalid JSON format");
      return;
      }
      for(var key in json){
        if(json.hasOwnProperty(key)){
          try{
            validators[key](json[key]);
          }catch(e){
            console.log('"' + key + '" does not conform to schema ');
            return;
          }
        }
      }
    }
    
    return {
      get name(){return _name;},
      set name(value){return validators["name"](value)},
      get age(){return _age;},
      set age(value){return validators["age"](value)},
    };
  }
};
```
*formatted for human readability

## Usage
```javascript
  var p = Person.parse();
  p.name = 4; // "4 does not conform to @string"
  p.name = "Ashley";
  console.log(p.name); // "Ashley"
```
