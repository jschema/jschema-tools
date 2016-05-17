# jschema-to-javascript.js

## Input

jschema: `{\"info\" : {\"name\":{\"first\":\"@string\",\"last\":\"@string\"}}}`

class name: `Person`

## Output
``` javascript
var Person = {
  create: function(){
    return{
      jschema: {
        info: {
        name: {
        first: "@string",
        last: "@string",
        },
        },
      },
      validate: function(){
        var validators = {};
        var msg = "";
        validators["info"] = function(value){
          var validators={};
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
            this.info = value;
          }else{
            return "info =" + value + " does not conform to [object Object]\n";
          }
        validators["name"] = function(value){
          var validators={};
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
            this.name = value;
          }else{
            return "name =" + value + " does not conform to [object Object]\n";
          }
        validators["first"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.first = value;
            return "";
          }
          return "first=" + value + " does not conform to @string\n";
        };
        validators["last"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.last = value;
            return "";
          }
          return "last=" + value + " does not conform to @string\n";
        };
        for(var key in validators){
          if(value[key]){
            msg += validators[key](value[key]);
          }
        }
        if(msg === ""){
          return "Valid";
        }
        return msg;
      };
        for(var key in validators){
          if(value[key]){
            msg += validators[key](value[key]);
          }
        }
        if(msg === ""){
          return "Valid";
        }
        return msg;
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
      json = JSON.parse(jsonData);
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
var person=Person.create();
person.info={};
console.log(person.validate()); //Valid
person.info=5;
console.log(person.validate()); //info=5 does not conform to [object Object]
person.info={};
person.info.name="hi";
console.log(person.validate()); //name=5 does not conform to [object Object]
person.info={name:{first:"jane",last:"doe"}};
console.log(person.validate()); //Valid
person.info.name.last=5;
console.log(person.validate()); //last=5 does not conform to @string
```

## TO DO

* array of objects
* strict validation: `p.validate(true)` 
* file input/output
