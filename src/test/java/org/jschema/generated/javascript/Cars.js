var Cars = {
  create: function(){
    return{
      jschema: {
        make: "@string",
        model: "@string",
        year: "@int",
        miles: "@int",
        accessories:[],
        interior: {
          seats: "@int",
          parking_sensors: "@boolean",
          colors: ["red", "blue", "green", "white", "silver"],
          packages: {
            sport: {
              heated_seats: "@boolean",
              big_rims: "@boolean",
              awd: "@boolean"
              },
            luxury: {
              heated_seats: "@boolean",
              big_rims: "@boolean",
              awd: "@boolean"
              },
            base: {
              heated_seats: "@boolean",
              big_rims: "@boolean",
              awd: "@boolean"
              }
            }
          }
      },
      validate: function(strict){
        var validators = {};
        var msg = "";
         for(var index in this){
        if(Object.prototype.toString.call(this[index]).slice(8, -1) === 'Object' && index !=='jschema'){
        validators["make"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.make = value;
            return "";
          }
          return "make=" + value + " does not conform to @string\n";
        };
        validators["model"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.model = value;
            return "";
          }
          return "model=" + value + " does not conform to @string\n";
        };
        validators["year"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
            this.year = value;
            return "";
          }
          return "year=" + value + " does not conform to @int\n";
        };
        validators["miles"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
            this.miles = value;
            return "";
          }
          return "miles=" + value + " does not conform to @int\n";
        };
        validators["accessories"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
            var type=Object.prototype.toString.call(value[0]).slice(8, -1);            for (var elem in value){
              if(Object.prototype.toString.call(value[elem]).slice(8, -1)!==type){
                return "accessories =[" + value + "] does not conform to [*]\n";
              }
            }
            this.accessories = value;
            return "";
          }else{
            return "accessories=" + value + " does not conform to [*]\n";
          }
        };
          validators["interior"] = function(value){
            var validators={};
            var msg="";
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
              this.interior = value;
            }else{
              return "interior =" + value + " does not conform to [object Object]\n";
            }
            validators["seats"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
                this.seats = value;
                return "";
              }
              return "seats=" + value + " does not conform to @int\n";
            };
            validators["parking_sensors"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                this.parking_sensors = value;
                return "";
              }
              return "parking_sensors=" + value + " does not conform to @boolean\n";
            };
          validators["colors"] = function(value){
            switch(value){
              case "red" : break;
              case "blue" : break;
              case "green" : break;
              case "white" : break;
              case "silver" : break;
              default: return "colors =" + value + " does not conform to [red,blue,green,white,silver]\n";
            }
            this.colors = value;
            return "";
          };
            validators["packages"] = function(value){
              var validators={};
              var msg="";
              if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
                this.packages = value;
              }else{
                return "packages =" + value + " does not conform to [object Object]\n";
              }
              validators["sport"] = function(value){
                var validators={};
                var msg="";
                if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
                  this.sport = value;
                }else{
                  return "sport =" + value + " does not conform to [object Object]\n";
                }
                validators["heated_seats"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.heated_seats = value;
                    return "";
                  }
                  return "heated_seats=" + value + " does not conform to @boolean\n";
                };
                validators["big_rims"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.big_rims = value;
                    return "";
                  }
                  return "big_rims=" + value + " does not conform to @boolean\n";
                };
                validators["awd"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.awd = value;
                    return "";
                  }
                  return "awd=" + value + " does not conform to @boolean\n";
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
              validators["luxury"] = function(value){
                var validators={};
                var msg="";
                if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
                  this.luxury = value;
                }else{
                  return "luxury =" + value + " does not conform to [object Object]\n";
                }
                validators["heated_seats"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.heated_seats = value;
                    return "";
                  }
                  return "heated_seats=" + value + " does not conform to @boolean\n";
                };
                validators["big_rims"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.big_rims = value;
                    return "";
                  }
                  return "big_rims=" + value + " does not conform to @boolean\n";
                };
                validators["awd"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.awd = value;
                    return "";
                  }
                  return "awd=" + value + " does not conform to @boolean\n";
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
              validators["base"] = function(value){
                var validators={};
                var msg="";
                if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
                  this.base = value;
                }else{
                  return "base =" + value + " does not conform to [object Object]\n";
                }
                validators["heated_seats"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.heated_seats = value;
                    return "";
                  }
                  return "heated_seats=" + value + " does not conform to @boolean\n";
                };
                validators["big_rims"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.big_rims = value;
                    return "";
                  }
                  return "big_rims=" + value + " does not conform to @boolean\n";
                };
                validators["awd"] = function(value){
                  if(Object.prototype.toString.call(value).slice(8, -1) === 'Boolean'){
                    this.awd = value;
                    return "";
                  }
                  return "awd=" + value + " does not conform to @boolean\n";
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
              if(value[key]){
                msg += validators[key](value[key]);
              }
            }
            if(msg === ""){
              return "";
            }
            return msg;
          };
        if (strict){
          for(var key in this[index]){
            if(!this.jschema[key] && Object.prototype.toString.call(this[index][key]).slice(8, -1) !== 'Function' && key!="jschema"){
              msg += "Key "+key+" not defined in JSchema. Strict flag only allows keys defined in JSchema.";
            }
          }
        }
        for(var key in validators){
            if(this.jschema[key] && this[index][key]){
              msg += validators[key](this[index][key]);
          }
        }
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
