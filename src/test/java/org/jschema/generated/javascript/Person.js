var Person = {
  create: function(){
    return{
      jschema: {
        name: "@string",
        age: {
          month: "@string",
          day: "@int",
          year: {
            decade: "@int",
            },
          },
      },
      validate: function(){
        var validators = {};
        var msg = "";
        validators["name"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.name = value;
            return "";
          }
          return "name=" + value + " does not conform to @string\n";
        };
          validators["age"] = function(value){
            var validators={};
            var msg="";
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
              this.age = value;
            }else{
              return "age =" + value + " does not conform to [object Object]\n";
            }
            validators["month"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.month = value;
                return "";
              }
              return "month=" + value + " does not conform to @string\n";
            };
            validators["day"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
                this.day = value;
                return "";
              }
              return "day=" + value + " does not conform to @int\n";
            };
            validators["year"] = function(value){
              var validators={};
              var msg="";
              if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
                this.year = value;
              }else{
                return "year =" + value + " does not conform to [object Object]\n";
              }
              validators["decade"] = function(value){
                if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
                  this.decade = value;
                  return "";
                }
                return "decade=" + value + " does not conform to @int\n";
              };
              for(var key in validators){
                if(value[key]){
                  msg += validators[key](value[key]);
                }
              }
              if(msg === ""){
                return "";
              }
              return msg;
            };
            for(var key in validators){
              if(value[key]){
                msg += validators[key](value[key]);
              }
            }
            if(msg === ""){
              return "";
            }
            return msg;
          };
        for(var key in validators){
          if(this.jschema[key]){
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
          if (this.hasOwnProperty(key) && key!=="jschema" &&
            Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {
            toJson[key] = this[key];
          }
        }
        return JSON.stringify(toJson);
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
      var obj=this.create();
      for (var key in obj){
        if(obj.hasOwnProperty(key)) json[key]=obj[key];
      }
      return json;
    }
  }
};
