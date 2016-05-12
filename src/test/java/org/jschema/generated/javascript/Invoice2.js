var Invoice2 = {
  create: function(){
    return{
      jschema: {
        id: "@date",
        email: "@string",
        total: "@int"
      },
      validate: function(strict){
        var validators = {};
        var msg = "";
        validators["id"] = function(value){
          if(Date.parse(value)===Date.parse(value)){
            this.id = value;
            return "";
          }
          return "id=" + value + " does not conform to @date\n";
        };
        validators["email"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.email = value;
            return "";
          }
          return "email=" + value + " does not conform to @string\n";
        };
        validators["total"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
            this.total = value;
            return "";
          }
          return "total=" + value + " does not conform to @int\n";
        };
        if (strict){
          for(var key in this){
            if(!this.jschema[key] && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function' && key!="jschema"){
              msg += "Key "+key+" not defined in JSchema. Strict flag only allows keys defined in JSchema.";
            }
          }
        }
        for(var key in validators){
            if(this.jschema[key] && this[key]){
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
