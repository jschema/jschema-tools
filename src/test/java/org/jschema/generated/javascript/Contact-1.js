var Contact-1 = {
  create: function(){
    return{
      jschema: {
        first_name: "@string",
        age: "@int",
        type: "@string",
        info: {
          emails:["@string"],
          phone_number: {
            home: "@int",
            cell: "@int",
            },
          addresses: [
          {
          address: "@string",
          },
          ]
        },
    },
      validate: function(strict){
        var validators = {};
        var msg = "";
        validators["first_name"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.first_name = value;
            return "";
          }
          return "first_name=" + value + " does not conform to @string\n";
        };
        validators["age"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
            this.age = value;
            return "";
          }
          return "age=" + value + " does not conform to @int\n";
        };
        validators["type"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.type = value;
            return "";
          }
          return "type=" + value + " does not conform to @string\n";
        };
          validators["info"] = function(value){
            var validators={};
            var msg="";
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
              this.info = value;
            }else{
              return "info =" + value + " does not conform to [object Object]\n";
            }
          validators["emails"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
              for (var elem in value){
                if(Object.prototype.toString.call(value[elem]).slice(8, -1) !== 'String'){
                  return "emails =[" + value + "] does not conform to [@string]\n";
                }
              }
              this.emails = value;
              return "";
            }else{
              return "emails=" + value + " does not conform to [@string]\n";
            }
          };
            validators["phone_number"] = function(value){
              var validators={};
              var msg="";
              if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
                this.phone_number = value;
              }else{
                return "phone_number =" + value + " does not conform to [object Object]\n";
              }
              validators["home"] = function(value){
                if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
                  this.home = value;
                  return "";
                }
                return "home=" + value + " does not conform to @int\n";
              };
              validators["cell"] = function(value){
                if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
                  this.cell = value;
                  return "";
                }
                return "cell=" + value + " does not conform to @int\n";
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
          validators["addresses"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
              for (var elem in value){
            var validators={};
            var msg="";
            if(Object.prototype.toString.call(value[elem]).slice(8, -1) === 'Object'){
              this.addresses = value;
            }else{
              return "addresses =" + value + " does not conform to [[object Object]]\n";
            }
            validators["address"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.address = value;
                return "";
              }
              return "address=" + value + " does not conform to @string\n";
            };
            for(var key in validators){
              if(value[elem][key]|| Object.prototype.toString.call(value[elem][key] ).slice(8, -1) === 'Boolean'){
                msg += validators[key](value[elem][key]);
              }
            }
            if(msg !== ""){
              return msg;
            }
            }
          if(msg === ""){
            return "";
          }
          return msg;
            this.addresses = value;
            return "";
          }else{
            return "addresses=" + value + " does not conform to [array]\n";
          }
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
          if(strict){
            if(this.jschema[key]){
              msg += validators[key](this[key]);
            }
          }else{
            if(this[key]){
              msg += validators[key](this[key]);
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