# jschema-to-javascript.js

## Input

jschema: `{"name" : "@string", "age" : "@int"}`

class name: `Person`

## Output
```javascript
var Person = {
  parse: function(jsonData){
    var _name;
    var _age;
    return {
      get name(){
        return _name;
      },
      set name(value){
        if (Object.prototype.toString.call(value).slice(8, -1) === 'String'){
          _name = value;
        } else{
          console.log(value + " does not conform to @string");
        }
        return;
      },
      get age(){
        return _age;
      },
      set age(value){
        if (Number.isInteger(value)){
          _age = value;
        } else{
          console.log(value + " does not conform to @int");
        }
        return;
      },
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
## Issues
The parse method does not initialize with jsonData. I cannot figure out how to accomplish this while also returning getters and setters. We need to use our setter's validation. I have tried:

- Establishing the setter as a variable and then returning it later. 
- Creating a new setter function (`set_name`) and running that after reading its JSON key as a string, so somehow creating the string `"set_name"` and then running it with something like `window["set_name"]`. This doesn't seem to be possible because this function is in a closed space.