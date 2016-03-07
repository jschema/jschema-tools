# jschema-to-javascript.js

## Input

jschema: `{"name" : "@string", "age" : "@int"}`

class name: `Person`

## Output
``` javascript
var Person = {
  create: function(){
    return{
      validate: function(){
        var validators = {};
        var msg = "";
        validators["name"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
              this.name = value;
              return ""
          } 
          return "name=" + value + " does not conform to @string\n";
        };
        validators["age"] = function(value){
          if(Number.isInteger(value)){
            this.age = value;
            return ""
          } 
          return "age=" + value + " does not conform to @int\n";
        };
        for(var key in validators){
          if(this[key]){
            msg += validators[key](this[key]);
          } 
        }
        if(msg === ""){
          return "Valid";
        }
        return msg;
      },
      toJSON: function(){
        var toJson = {};
        for (var key in this){
          if (this.hasOwnProperty(key) && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {
            toJson[key] = this[key];
          }
        }
        return toJson;
      }
    };
  },
  parse: function(jsonData){
    var json;
    if(typeof jsonData != 'undefined'){
      try{
        var json = JSON.parse(jsonData);
      }catch(e){
        return "Invalid JSON format";
      }
      return Object.assign(json, this.create());
    }
  }
};
```

## Usage
```javascript
  var p = Person.parse();
  p.name = 4; // "4 does not conform to @string"
  p.name = "Ashley";
  console.log(p.name); // "Ashley"
```
