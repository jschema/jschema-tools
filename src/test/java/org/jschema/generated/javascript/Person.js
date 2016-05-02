var Person = {
  create: function(){
    return{
      jschema: {
        first_name: "@string",
        last_name: "@string",
        age: "@int",
        hobbies: [
        {
        sports:["@string"],
        crafts: "@string",
        },
        ]
      info: {
        email:["@string"],
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
    dob: "@date",
    gender:["@string"]
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
        validators["last_name"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.last_name = value;
            return "";
          }
          return "last_name=" + value + " does not conform to @string\n";
        };
        validators["age"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
            this.age = value;
            return "";
          }
          return "age=" + value + " does not conform to @int\n";
        };
        validators["hobbies"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
            for (var elem in value){
          var validators={};
          var msg="";
          if(Object.prototype.toString.call(value[elem]).slice(8, -1) === 'Object'){
            this.hobbies = value;
          }else{
            return "hobbies =" + value + " does not conform to [[object Object]]\n";
          }
        validators["sports"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
            for (var elem in value){
              if(Object.prototype.toString.call(value[elem]).slice(8, -1) !== 'String'){
                return "sports =[" + value + "] does not conform to [@string]\n";
              }
            }
            this.sports = value;
            return "";
          }else{
            return "sports=" + value + " does not conform to [@string]\n";
          }
        };
          validators["crafts"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
              this.crafts = value;
              return "";
            }
            return "crafts=" + value + " does not conform to @string\n";
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
          this.hobbies = value;
          return "";
        }else{
          return "hobbies=" + value + " does not conform to [array]\n";
        }
      };
        validators["info"] = function(value){
          var validators={};
          var msg="";
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
            this.info = value;
          }else{
            return "info =" + value + " does not conform to [object Object]\n";
          }
        validators["email"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
            for (var elem in value){
              if(Object.prototype.toString.call(value[elem]).slice(8, -1) !== 'String'){
                return "email =[" + value + "] does not conform to [@string]\n";
              }
            }
            this.email = value;
            return "";
          }else{
            return "email=" + value + " does not conform to [@string]\n";
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
    validators["dob"] = function(value){
      if(Date.parse(value)===Date.parse(value)){
        this.dob = value;
        return "";
      }
      return "dob=" + value + " does not conform to @date\n";
    };
    validators["gender"] = function(value){
      if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
        for (var elem in value){
          if(Object.prototype.toString.call(value[elem]).slice(8, -1) !== 'String'){
            return "gender =[" + value + "] does not conform to [@string]\n";
          }
        }
        this.gender = value;
        return "";
      }else{
        return "gender=" + value + " does not conform to [@string]\n";
      }
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
