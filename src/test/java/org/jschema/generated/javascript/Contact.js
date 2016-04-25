var Contact = {
  create: function(){
    return{
      jschema: {
        first_name: "@string",
        last_name: "@string",
        age: "@int",
        type: ["friend", "customer", "supplier"],
        emails:["@string"]
      },
      validate: function(){
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
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
            this.age = value;
            return "";
          }
          return "age=" + value + " does not conform to @int\n";
        };
        validators["type"] = function(value){
          switch(value){
            case "friend" : break;
            case "customer" : break;
            case "supplier" : break;
            default: return "type =" + value + " does not conform to [friend,customer,supplier]\n";
          }
          this.type = value;
          return "";
        };
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
